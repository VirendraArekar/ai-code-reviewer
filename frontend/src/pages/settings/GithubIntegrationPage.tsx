import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import {
  CheckCircle2,
  Copy,
  ExternalLink,
  Sparkles,
  AlertTriangle,
  HelpCircle,
  Download,
  Info,
  Radio,
  RefreshCw,
  Key,
} from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import {
  useGetGithubAppConfigQuery,
  useConvertGithubAppCodeMutation,
} from '@/api/settingsApi';

export const GithubIntegrationPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const { data: config, refetch } = useGetGithubAppConfigQuery();
  const [convertCode, { isLoading: isConverting }] = useConvertGithubAppCodeMutation();

  const [copiedWebhook, setCopiedWebhook] = useState(false);
  const [copiedSecret, setCopiedSecret] = useState(false);
  const [copiedEnv, setCopiedEnv] = useState(false);
  const [createdAppResult, setCreatedAppResult] = useState<{
    appId?: string;
    appName?: string;
    clientId?: string;
    clientSecret?: string;
    webhookSecret?: string;
    pem?: string;
    htmlUrl?: string;
  } | null>(null);

  const localHost = window.location.hostname;
  const isLocalhost = localHost === 'localhost' || localHost === '127.0.0.1';

  // Webhook mode: 'none' (recommended for localhost) or 'tunnel' (for ngrok/smee.io)
  const [webhookMode, setWebhookMode] = useState<'none' | 'tunnel'>('none');
  const [publicTunnelUrl, setPublicTunnelUrl] = useState('');
  const [urlError, setUrlError] = useState('');

  const defaultWebhookUrl = `${window.location.protocol}//${localHost}:8080/api/v1/webhooks/github`;
  const webhookSecret = 'development_webhook_secret_key_123456';

  // Handle GitHub App creation redirect code exchange
  useEffect(() => {
    const code = searchParams.get('code');
    if (code) {
      convertCode({ code })
        .unwrap()
        .then((res) => {
          setCreatedAppResult(res);
          refetch();
          // Clear query params cleanly without reloading
          window.history.replaceState({}, document.title, window.location.pathname);
        })
        .catch((err) => {
          console.error('Failed to convert GitHub manifest code:', err);
        });
    }
  }, [searchParams, convertCode, refetch]);

  const copyWebhook = () => {
    navigator.clipboard.writeText(
      webhookMode === 'tunnel' && publicTunnelUrl ? publicTunnelUrl : defaultWebhookUrl
    );
    setCopiedWebhook(true);
    setTimeout(() => setCopiedWebhook(false), 2000);
  };

  const copySecret = () => {
    navigator.clipboard.writeText(webhookSecret);
    setCopiedSecret(true);
    setTimeout(() => setCopiedSecret(false), 2000);
  };

  const copyEnvSnippet = () => {
    if (!createdAppResult) return;
    const snippet = [
      `GITHUB_APP_ID=${createdAppResult.appId || ''}`,
      `GITHUB_APP_NAME=${createdAppResult.appName || 'ai-code-reviewer'}`,
      `GITHUB_APP_CLIENT_ID=${createdAppResult.clientId || ''}`,
      `GITHUB_APP_CLIENT_SECRET=${createdAppResult.clientSecret || ''}`,
      `GITHUB_WEBHOOK_SECRET=${createdAppResult.webhookSecret || webhookSecret}`,
    ].join('\n');
    navigator.clipboard.writeText(snippet);
    setCopiedEnv(true);
    setTimeout(() => setCopiedEnv(false), 2500);
  };

  const downloadPrivateKey = () => {
    if (!createdAppResult?.pem) return;
    const blob = new Blob([createdAppResult.pem], { type: 'application/x-pem-file' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${createdAppResult.appName || 'ai-code-reviewer'}.private-key.pem`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  const handleCreateAppViaManifest = () => {
    setUrlError('');

    // If tunnel mode is chosen, validate that the URL is public and valid
    if (webhookMode === 'tunnel') {
      const trimmed = publicTunnelUrl.trim();
      if (!trimmed) {
        setUrlError('Please enter a public HTTPS tunnel URL (e.g. from ngrok or smee.io)');
        return;
      }
      if (!trimmed.startsWith('https://')) {
        setUrlError('Public webhook URL must use https:// protocol');
        return;
      }
      if (trimmed.includes('localhost') || trimmed.includes('127.0.0.1')) {
        setUrlError('GitHub rejects localhost. Use an ngrok or smee.io public URL instead.');
        return;
      }
    }

    const appUniqueId = Math.random().toString(36).substring(2, 7);
    const redirectUrl = `${window.location.origin}/settings/github`;

    // Construct valid manifest per GitHub API specifications
    const manifest: Record<string, any> = {
      name: `AI Code Reviewer - ${appUniqueId}`,
      url: window.location.origin,
      redirect_url: redirectUrl,
      callback_urls: [`${window.location.origin}/api/v1/auth/github/callback`],
      public: false,
      default_permissions: {
        pull_requests: 'write',
        issues: 'write',
        contents: 'read',
        checks: 'write',
        metadata: 'read',
      },
    };

    // GitHub rejects localhost in hook_attributes.
    // If webhookMode is tunnel and a valid public URL is provided, include hook_attributes and default_events.
    // If in local mode, omitting hook_attributes avoids the GitHub "Hook url is not supported (localhost)" error!
    if (webhookMode === 'tunnel' && publicTunnelUrl.trim()) {
      manifest.hook_attributes = {
        url: publicTunnelUrl.trim(),
        active: true,
      };
      manifest.default_events = [
        'pull_request',
        'pull_request_review',
        'check_run',
        'check_suite',
      ];
    }

    // Submit form via POST to GitHub's manifest creation endpoint
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'https://github.com/settings/apps/new';
    form.target = '_blank';

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'manifest';
    input.value = JSON.stringify(manifest);

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
  };

  const isConfigured = Boolean(config?.clientId || config?.hasToken);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          GitHub App & Webhook Integration <GithubIcon className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Configure GitHub App credentials, HMAC webhook verification, and permissions to review real repositories
        </p>
      </div>

      {/* Code conversion loading banner */}
      {isConverting && (
        <div className="p-4 rounded-xl bg-indigo-500/10 border border-indigo-500/30 flex items-center gap-3 text-indigo-300 text-sm">
          <RefreshCw className="w-5 h-5 animate-spin" />
          <span>Exchanging temporary GitHub App registration code for credentials...</span>
        </div>
      )}

      {/* Newly Created App Credentials Banner */}
      {createdAppResult && (
        <Card className="p-6 border-emerald-500/40 bg-slate-900/90 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-400 flex items-center justify-center">
                <CheckCircle2 className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-bold text-white">
                  GitHub App Successfully Registered!
                </h3>
                <p className="text-xs text-slate-400">
                  App Name: <span className="text-emerald-300 font-mono">{createdAppResult.appName}</span>
                </p>
              </div>
            </div>
            <div className="flex gap-2">
              <Button
                variant="primary"
                size="sm"
                onClick={copyEnvSnippet}
                leftIcon={<Copy className="w-3.5 h-3.5" />}
              >
                {copiedEnv ? 'Copied to Clipboard!' : 'Copy .env Snippet'}
              </Button>
              {createdAppResult.pem && (
                <Button
                  variant="outline"
                  size="sm"
                  onClick={downloadPrivateKey}
                  leftIcon={<Download className="w-3.5 h-3.5" />}
                >
                  Download .pem
                </Button>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs font-mono bg-slate-950 p-4 rounded-xl border border-slate-800">
            <div>
              <span className="text-slate-500">App ID:</span>{' '}
              <span className="text-white font-bold">{createdAppResult.appId}</span>
            </div>
            <div>
              <span className="text-slate-500">Client ID:</span>{' '}
              <span className="text-white font-bold">{createdAppResult.clientId}</span>
            </div>
            <div>
              <span className="text-slate-500">Client Secret:</span>{' '}
              <span className="text-emerald-400 font-bold">
                {createdAppResult.clientSecret ? '••••••••••••••••••••' : 'None'}
              </span>
            </div>
            <div>
              <span className="text-slate-500">Webhook Secret:</span>{' '}
              <span className="text-white">
                {createdAppResult.webhookSecret || webhookSecret}
              </span>
            </div>
          </div>

          <p className="text-xs text-slate-400">
            Paste these values into your <code>.env</code> file to persist them permanently across container rebuilds.
          </p>
        </Card>
      )}

      {/* Explanatory Banner about Localhost vs GitHub Webhooks */}
      <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 flex items-start gap-3">
        <Info className="w-5 h-5 text-indigo-400 shrink-0 mt-0.5" />
        <div className="text-xs space-y-1 text-slate-300">
          <p className="font-semibold text-white">
            Why GitHub Rejects "localhost" as a Webhook URL:
          </p>
          <p className="text-slate-400 leading-relaxed">
            GitHub servers deliver webhooks over the public internet. Because GitHub cannot reach your machine's internal <code>localhost</code> loopback, GitHub App registration will fail if given a localhost webhook.
            With the updated 1-click flow below, you can register your app immediately in <strong>Localhost Mode</strong> without webhooks, or use a free public tunnel (such as <strong>Smee.io</strong> or <strong>Ngrok</strong>). You can always review PRs on demand via GitHub REST API without webhooks!
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* App Status & Manifest Creation Card */}
        <Card className="p-6 space-y-5">
          <CardTitle>GitHub App Registration (1-Click Manifest)</CardTitle>

          <div className="flex items-center gap-3 p-4 rounded-xl bg-slate-950/60 border border-slate-800">
            <div
              className={`w-10 h-10 rounded-xl flex items-center justify-center font-bold ${
                isConfigured
                  ? 'bg-emerald-500/10 text-emerald-400'
                  : 'bg-amber-500/10 text-amber-400'
              }`}
            >
              {isConfigured ? (
                <CheckCircle2 className="w-5 h-5" />
              ) : (
                <AlertTriangle className="w-5 h-5" />
              )}
            </div>
            <div>
              <h4 className="font-semibold text-white text-sm">
                {isConfigured ? 'GitHub App Active' : 'Configuration Pending'}
              </h4>
              <p className="text-xs text-slate-400 font-mono">
                Client ID: {config?.clientId ? `${config.clientId.substring(0, 8)}...` : 'Not set in .env'}
              </p>
            </div>
          </div>

          <div className="space-y-3">
            <label className="block text-xs font-semibold text-slate-300">
              Select Registration Mode:
            </label>

            {/* Mode selection radio */}
            <div className="space-y-2">
              <label
                onClick={() => setWebhookMode('none')}
                className={`flex items-start gap-3 p-3 rounded-xl border cursor-pointer transition-colors ${
                  webhookMode === 'none'
                    ? 'bg-indigo-600/10 border-indigo-500/40 text-white'
                    : 'bg-slate-950/50 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <input
                  type="radio"
                  name="webhookMode"
                  checked={webhookMode === 'none'}
                  onChange={() => setWebhookMode('none')}
                  className="mt-1"
                />
                <div>
                  <div className="font-semibold text-xs text-white">
                    Localhost Mode (No Webhook in Manifest) — Recommended
                  </div>
                  <p className="text-[11px] text-slate-400 mt-0.5">
                    Registers the GitHub App instantly with zero errors. Uses GitHub REST API to fetch PRs and post reviews. Webhooks can be configured later.
                  </p>
                </div>
              </label>

              <label
                onClick={() => setWebhookMode('tunnel')}
                className={`flex items-start gap-3 p-3 rounded-xl border cursor-pointer transition-colors ${
                  webhookMode === 'tunnel'
                    ? 'bg-indigo-600/10 border-indigo-500/40 text-white'
                    : 'bg-slate-950/50 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <input
                  type="radio"
                  name="webhookMode"
                  checked={webhookMode === 'tunnel'}
                  onChange={() => setWebhookMode('tunnel')}
                  className="mt-1"
                />
                <div>
                  <div className="font-semibold text-xs text-white">
                    Public Tunnel / Smee.io Mode
                  </div>
                  <p className="text-[11px] text-slate-400 mt-0.5">
                    Provide a public HTTPS tunnel URL (e.g. from <code>smee.io</code> or <code>ngrok http 8080</code>) to receive live webhook pushes.
                  </p>
                </div>
              </label>
            </div>

            {/* Public tunnel URL input if tunnel mode selected */}
            {webhookMode === 'tunnel' && (
              <div className="pt-1 space-y-1">
                <label className="block text-xs font-semibold text-slate-300">
                  Public Tunnel Webhook URL (https://)
                </label>
                <Input
                  value={publicTunnelUrl}
                  onChange={(e) => {
                    setPublicTunnelUrl(e.target.value);
                    setUrlError('');
                  }}
                  placeholder="https://smee.io/your-unique-key or https://xyz.ngrok-free.app/api/v1/webhooks/github"
                  className="font-mono text-xs"
                />
                {urlError && <p className="text-xs text-rose-400">{urlError}</p>}
                <p className="text-[11px] text-slate-400">
                  Tip: Create a free channel on <a href="https://smee.io" target="_blank" rel="noreferrer" className="text-indigo-400 underline">smee.io</a> and run <code>smee --url &lt;url&gt; --target http://localhost:8080/api/v1/webhooks/github</code>.
                </p>
              </div>
            )}
          </div>

          <div className="space-y-2 text-xs text-slate-400 pt-2 border-t border-slate-800">
            <div className="flex justify-between py-1 border-b border-slate-800/60">
              <span>Client ID:</span>
              <span className="text-white font-mono font-semibold">
                {config?.clientId || 'None'}
              </span>
            </div>
            <div className="flex justify-between py-1 border-b border-slate-800/60">
              <span>Client Secret:</span>
              <span className={config?.hasClientSecret ? 'text-emerald-400 font-semibold' : 'text-slate-500'}>
                {config?.hasClientSecret ? 'Configured in .env' : 'Missing'}
              </span>
            </div>
            <div className="flex justify-between py-1 border-b border-slate-800/60">
              <span>Personal Access Token (GITHUB_TOKEN):</span>
              <span className={config?.hasToken ? 'text-emerald-400 font-semibold' : 'text-slate-500'}>
                {config?.hasToken ? 'Active' : 'Optional (Recommended for private repos)'}
              </span>
            </div>
            <div className="flex justify-between py-1">
              <span>Permissions Requested:</span>
              <span className="text-white font-mono">Pull Requests, Contents, Checks</span>
            </div>
          </div>

          <div className="pt-2 flex flex-wrap items-center gap-3">
            <Button
              variant="primary"
              size="sm"
              onClick={handleCreateAppViaManifest}
              leftIcon={<Sparkles className="w-3.5 h-3.5" />}
            >
              Create GitHub App with 1-Click
            </Button>
            <a
              href="https://github.com/settings/apps"
              target="_blank"
              rel="noreferrer"
            >
              <Button variant="outline" size="sm" rightIcon={<ExternalLink className="w-3.5 h-3.5" />}>
                Manage on GitHub.com
              </Button>
            </a>
          </div>
        </Card>

        {/* Webhook Configuration Card */}
        <Card className="p-6 space-y-4">
          <CardTitle>Webhook Delivery Details</CardTitle>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              Local Webhook Delivery Endpoint
            </label>
            <div className="flex items-center gap-2">
              <Input value={defaultWebhookUrl} readOnly className="font-mono text-xs" />
              <Button variant="secondary" size="md" onClick={copyWebhook}>
                {copiedWebhook ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
              </Button>
            </div>
            <p className="text-[11px] text-slate-400 mt-1">
              Local path: <code>POST /api/v1/webhooks/github</code>. Verified with HMAC-SHA256 signatures.
            </p>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              Webhook Secret (HMAC-SHA256)
            </label>
            <div className="flex items-center gap-2">
              <Input value={webhookSecret} readOnly className="font-mono text-xs" />
              <Button variant="secondary" size="md" onClick={copySecret}>
                {copiedSecret ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
              </Button>
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              Automatic Pull Request Events
            </label>
            <div className="flex flex-wrap gap-2 text-xs font-mono">
              <span className="px-2 py-0.5 rounded bg-slate-800 text-indigo-300 border border-slate-700">
                pull_request.opened
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-indigo-300 border border-slate-700">
                pull_request.synchronize
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-indigo-300 border border-slate-700">
                pull_request.reopened
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-indigo-300 border border-slate-700">
                pull_request.closed
              </span>
            </div>
          </div>

          <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 text-xs text-slate-400 space-y-1">
            <span className="font-semibold text-slate-200">How to receive webhooks locally:</span>
            <p>1. Forward with ngrok: <code>ngrok http 8080</code></p>
            <p>2. Copy your <code>https://....ngrok-free.app/api/v1/webhooks/github</code> into your GitHub App settings.</p>
          </div>
        </Card>
      </div>

      {/* Guide Card */}
      <Card className="p-6 space-y-3 bg-slate-900/60 border-indigo-500/20">
        <h4 className="text-sm font-semibold text-white flex items-center gap-2">
          <HelpCircle className="w-4 h-4 text-indigo-400" /> How Real GitHub Code Reviews Work
        </h4>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs text-slate-300">
          <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 space-y-1">
            <span className="font-bold text-white text-sm">1. Connect Repo</span>
            <p className="text-slate-400">
              Go to <strong>Repositories</strong> and enter your GitHub repository (e.g. <code>VirendraArekar/ai-code-reviewer</code>).
            </p>
          </div>
          <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 space-y-1">
            <span className="font-bold text-white text-sm">2. Sync Pull Requests</span>
            <p className="text-slate-400">
              The application automatically queries GitHub's REST API, imports real open pull requests, and downloads the unified diff patches.
            </p>
          </div>
          <div className="p-3 rounded-xl bg-slate-950 border border-slate-800 space-y-1">
            <span className="font-bold text-white text-sm">3. Autonomous AI Review</span>
            <p className="text-slate-400">
              Your configured AI model (Gemini 2.0 Flash or GPT-4o) evaluates the code diff for bugs, vulnerabilities, and quality scores in real-time.
            </p>
          </div>
        </div>
      </Card>
    </div>
  );
};

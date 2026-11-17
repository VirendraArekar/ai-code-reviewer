import React, { useState } from 'react';
import {
  CheckCircle2,
  Copy,
  ShieldCheck,
  Key,
  ExternalLink,
  Sparkles,
  AlertTriangle,
  HelpCircle,
  FolderGit2,
} from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { useGetGithubAppConfigQuery } from '@/api/settingsApi';

export const GithubIntegrationPage: React.FC = () => {
  const { data: config, refetch } = useGetGithubAppConfigQuery();
  const [copiedWebhook, setCopiedWebhook] = useState(false);
  const [copiedSecret, setCopiedSecret] = useState(false);

  const localHost = window.location.hostname;
  const webhookUrl = `${window.location.protocol}//${localHost}:8080/api/v1/webhooks/github`;
  const webhookSecret = 'development_webhook_secret_key_123456';

  const copyWebhook = () => {
    navigator.clipboard.writeText(webhookUrl);
    setCopiedWebhook(true);
    setTimeout(() => setCopiedWebhook(false), 2000);
  };

  const copySecret = () => {
    navigator.clipboard.writeText(webhookSecret);
    setCopiedSecret(true);
    setTimeout(() => setCopiedSecret(false), 2000);
  };

  const handleCreateAppViaManifest = () => {
    // Generate GitHub App manifest per GitHub API spec
    const manifest = {
      name: `AI Code Reviewer - ${Math.random().toString(36).substring(2, 7)}`,
      url: window.location.origin,
      hook_attributes: {
        url: webhookUrl,
        active: true,
      },
      redirect_url: `${window.location.origin}/settings/github`,
      callback_urls: [`${window.location.origin}/api/v1/auth/github/callback`],
      public: false,
      default_permissions: {
        pull_requests: 'write',
        issues: 'write',
        contents: 'read',
        checks: 'write',
        metadata: 'read',
      },
      default_events: ['pull_request', 'pull_request_review', 'check_run', 'check_suite'],
    };

    // Form submission to GitHub App Manifest creation endpoint
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

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* App Status Card */}
        <Card className="p-6 space-y-4">
          <CardTitle>Integration Status</CardTitle>
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
                {isConfigured ? 'GitHub Connected' : 'Configuration Pending'}
              </h4>
              <p className="text-xs text-slate-400 font-mono">
                Client ID: {config?.clientId ? `${config.clientId.substring(0, 8)}...` : 'Not set in .env'}
              </p>
            </div>
          </div>

          <div className="space-y-2 text-xs text-slate-400">
            <div className="flex justify-between py-1.5 border-b border-slate-800">
              <span>Client ID:</span>
              <span className="text-white font-mono font-semibold">
                {config?.clientId || 'None'}
              </span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-800">
              <span>Client Secret:</span>
              <span className={config?.hasClientSecret ? 'text-emerald-400 font-semibold' : 'text-slate-500'}>
                {config?.hasClientSecret ? 'Configured in .env' : 'Missing'}
              </span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-800">
              <span>Personal Access Token (GITHUB_TOKEN):</span>
              <span className={config?.hasToken ? 'text-emerald-400 font-semibold' : 'text-slate-500'}>
                {config?.hasToken ? 'Active' : 'Optional (Recommended for private repos)'}
              </span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-800">
              <span>HMAC Webhook Secret:</span>
              <span className="text-emerald-400 font-semibold">
                {config?.webhookSecretConfigured ? 'Active (HMAC-SHA256)' : 'Default'}
              </span>
            </div>
            <div className="flex justify-between py-1.5">
              <span>Permissions Granted:</span>
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
              Payload Delivery URL
            </label>
            <div className="flex items-center gap-2">
              <Input value={webhookUrl} readOnly className="font-mono text-xs" />
              <Button variant="secondary" size="md" onClick={copyWebhook}>
                {copiedWebhook ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
              </Button>
            </div>
            <p className="text-[11px] text-slate-400 mt-1">
              For local development, forward webhooks using <code>ngrok http 8080</code> or smee.io.
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
              Required Webhook Events
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

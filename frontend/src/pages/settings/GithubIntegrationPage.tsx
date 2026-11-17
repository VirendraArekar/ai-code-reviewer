import React, { useState } from 'react';
import { CheckCircle2, Copy, ShieldCheck, Key, ExternalLink } from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { useGetGithubAppConfigQuery } from '@/api/settingsApi';

export const GithubIntegrationPage: React.FC = () => {
  const { data: config } = useGetGithubAppConfigQuery();
  const [copied, setCopied] = useState(false);

  const webhookUrl = 'https://api.reviewai.dev/api/v1/webhooks/github';

  const copyWebhook = () => {
    navigator.clipboard.writeText(webhookUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          GitHub App & Webhooks <GithubIcon className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Configure GitHub App credentials, HMAC webhook verification, and permissions
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* App Status Card */}
        <Card className="p-6 space-y-4">
          <CardTitle>GitHub App Status</CardTitle>
          <div className="flex items-center gap-3 p-4 rounded-xl bg-slate-950/60 border border-slate-800">
            <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-400 flex items-center justify-center font-bold">
              <CheckCircle2 className="w-5 h-5" />
            </div>
            <div>
              <h4 className="font-semibold text-white text-sm">ReviewAI GitHub App</h4>
              <p className="text-xs text-slate-400">App ID: 104291 • Connected & Healthy</p>
            </div>
          </div>

          <div className="space-y-2 text-xs text-slate-400">
            <div className="flex justify-between py-1 border-b border-slate-800">
              <span>Webhook Signature Verification:</span>
              <span className="text-emerald-400 font-semibold">HMAC-SHA256 (Active)</span>
            </div>
            <div className="flex justify-between py-1 border-b border-slate-800">
              <span>Pull Request Comments:</span>
              <span className="text-white font-semibold">Enabled</span>
            </div>
            <div className="flex justify-between py-1">
              <span>GitHub Checks API:</span>
              <span className="text-white font-semibold">Enabled</span>
            </div>
          </div>

          <div className="pt-2">
            <a
              href="https://github.com/apps/reviewai-code-reviewer"
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
          <CardTitle>Webhook URL & Secret</CardTitle>
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Payload Delivery URL
            </label>
            <div className="flex items-center gap-2">
              <Input value={webhookUrl} readOnly className="font-mono text-xs" />
              <Button variant="secondary" size="md" onClick={copyWebhook}>
                {copied ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
              </Button>
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Subscribed Events
            </label>
            <div className="flex flex-wrap gap-2 text-xs font-mono">
              <span className="px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                pull_request.opened
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                pull_request.synchronize
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                pull_request.reopened
              </span>
              <span className="px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                pull_request.closed
              </span>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
};

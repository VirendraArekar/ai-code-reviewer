import React from 'react';
import { CreditCard, CheckCircle2, Zap, ArrowRight } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';

export const BillingPage: React.FC = () => {
  return (
    <div className="space-y-6 max-w-5xl">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          Subscription & Usage Limits <CreditCard className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Monitor monthly pull request review volume, AI token consumption, and seat allocation
        </p>
      </div>

      {/* Usage Progress Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card className="p-5 space-y-3">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>Monthly Reviews Used</span>
            <span className="font-mono text-white font-bold">89 / 500</span>
          </div>
          <div className="w-full bg-slate-800 rounded-full h-2">
            <div className="bg-indigo-500 h-2 rounded-full" style={{ width: '17.8%' }} />
          </div>
          <p className="text-[11px] text-slate-500">Resets on Dec 1, 2026</p>
        </Card>

        <Card className="p-5 space-y-3">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>Connected Repositories</span>
            <span className="font-mono text-white font-bold">8 / 25</span>
          </div>
          <div className="w-full bg-slate-800 rounded-full h-2">
            <div className="bg-emerald-500 h-2 rounded-full" style={{ width: '32%' }} />
          </div>
          <p className="text-[11px] text-slate-500">17 slots available</p>
        </Card>

        <Card className="p-5 space-y-3">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>AI Token Consumption</span>
            <span className="font-mono text-white font-bold">1.4M / 10M</span>
          </div>
          <div className="w-full bg-slate-800 rounded-full h-2">
            <div className="bg-purple-500 h-2 rounded-full" style={{ width: '14%' }} />
          </div>
          <p className="text-[11px] text-slate-500">8.6M tokens remaining</p>
        </Card>
      </div>

      {/* Plans Tier Comparison */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4">
        {[
          {
            name: 'Free Starter',
            price: '$0',
            period: '/month',
            desc: 'For individual developers and open source projects.',
            features: ['Up to 3 repositories', '50 monthly PR reviews', 'Community LLM support', 'Standard AST analysis'],
            current: false,
          },
          {
            name: 'Team Pro',
            price: '$49',
            period: '/seat/mo',
            desc: 'For high-velocity engineering squads wanting deep reviews.',
            features: ['Up to 25 repositories', '500 monthly PR reviews', 'GPT-4o & Gemini 2.0 Flash', 'Inline GitHub annotations', 'OWASP vulnerability scanning'],
            current: true,
          },
          {
            name: 'Enterprise',
            price: '$199',
            period: '/seat/mo',
            desc: 'Custom security policies, on-prem Ollama, and SLA guarantees.',
            features: ['Unlimited repositories', 'Unlimited PR reviews', 'Self-hosted Ollama integration', 'Custom audit log retention', 'Dedicated Slack review bot', 'SSO & SAML integration'],
            current: false,
          },
        ].map((plan, i) => (
          <Card
            key={i}
            className={`p-6 flex flex-col justify-between space-y-6 ${
              plan.current
                ? 'border-indigo-500/80 bg-slate-900/90 shadow-2xl ring-1 ring-indigo-500/30'
                : 'border-slate-800 bg-slate-950/60'
            }`}
          >
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h4 className="font-bold text-lg text-white">{plan.name}</h4>
                {plan.current && (
                  <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                    Active Plan
                  </span>
                )}
              </div>
              <p className="text-xs text-slate-400">{plan.desc}</p>
              <div className="flex items-baseline gap-1">
                <span className="text-3xl font-extrabold text-white font-mono">{plan.price}</span>
                <span className="text-xs text-slate-400">{plan.period}</span>
              </div>

              <ul className="space-y-2 text-xs text-slate-300 pt-2 border-t border-slate-800">
                {plan.features.map((feat, fi) => (
                  <li key={fi} className="flex items-center gap-2">
                    <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400 shrink-0" />
                    <span>{feat}</span>
                  </li>
                ))}
              </ul>
            </div>

            <Button
              variant={plan.current ? 'outline' : 'primary'}
              size="sm"
              className="w-full"
              disabled={plan.current}
            >
              {plan.current ? 'Current Subscription' : 'Upgrade Plan'}
            </Button>
          </Card>
        ))}
      </div>
    </div>
  );
};

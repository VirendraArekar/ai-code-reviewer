import React from 'react';
import { ShieldCheck, Users, Building, Activity, Cpu, Database, Server } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';

export const AdminDashboardPage: React.FC = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          System Administration <ShieldCheck className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Cluster health, tenant organizations, background job workers, and platform telemetry
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        <Card className="p-4 space-y-1">
          <span className="text-xs text-slate-400">Total Organizations</span>
          <div className="text-2xl font-bold font-mono text-white">124</div>
        </Card>
        <Card className="p-4 space-y-1">
          <span className="text-xs text-slate-400">Registered Developers</span>
          <div className="text-2xl font-bold font-mono text-white">852</div>
        </Card>
        <Card className="p-4 space-y-1">
          <span className="text-xs text-slate-400">Total Reviews Analyzed</span>
          <div className="text-2xl font-bold font-mono text-white">14,892</div>
        </Card>
        <Card className="p-4 space-y-1">
          <span className="text-xs text-slate-400">Active Job Workers</span>
          <div className="text-2xl font-bold font-mono text-emerald-400">8 / 8 Online</div>
        </Card>
      </div>

      {/* Services Health */}
      <Card className="p-6 space-y-4">
        <CardTitle>Core Service Components</CardTitle>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <Database className="w-5 h-5 text-indigo-400" />
              <div>
                <h4 className="text-sm font-semibold text-white">MySQL 8 Cluster</h4>
                <p className="text-xs text-slate-400">HikariCP: 24 active conns</p>
              </div>
            </div>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500" />
          </div>

          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <Server className="w-5 h-5 text-purple-400" />
              <div>
                <h4 className="text-sm font-semibold text-white">Async Review Queue</h4>
                <p className="text-xs text-slate-400">0 backlogged jobs</p>
              </div>
            </div>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500" />
          </div>

          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <Cpu className="w-5 h-5 text-amber-400" />
              <div>
                <h4 className="text-sm font-semibold text-white">AI Upstream Router</h4>
                <p className="text-xs text-slate-400">Gemini & OpenAI: 100% SLA</p>
              </div>
            </div>
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500" />
          </div>
        </div>
      </Card>
    </div>
  );
};

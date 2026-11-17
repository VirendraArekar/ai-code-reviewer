import React from 'react';
import { Building2, Users, FolderGit2, Shield, Plus } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { useAppSelector } from '@/store';

export const OrganizationPage: React.FC = () => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          Organization Settings <Building2 className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Manage organization workspace, member access roles, and connected repositories
        </p>
      </div>

      <Card className="p-6 space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-slate-800">
          <div>
            <h3 className="text-lg font-bold text-white">Acme Engineering</h3>
            <p className="text-xs text-slate-400 font-mono">Slug: acme-engineering • Enterprise Tier</p>
          </div>
          <span className="px-3 py-1 rounded-full bg-indigo-500/15 text-indigo-300 border border-indigo-500/30 font-bold text-xs">
            Pro Plan
          </span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-2">
          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800">
            <span className="text-xs text-slate-400">Total Members</span>
            <div className="text-xl font-bold font-mono text-white mt-1">14 Seats</div>
          </div>
          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800">
            <span className="text-xs text-slate-400">Connected Repositories</span>
            <div className="text-xl font-bold font-mono text-white mt-1">8 Repos</div>
          </div>
          <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800">
            <span className="text-xs text-slate-400">Current Role</span>
            <div className="text-xl font-bold font-mono text-indigo-400 mt-1">
              {user?.role || 'ORGANIZATION_OWNER'}
            </div>
          </div>
        </div>
      </Card>
    </div>
  );
};

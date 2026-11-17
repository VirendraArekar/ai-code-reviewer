import React, { useState } from 'react';
import { FileText, Search, Shield, Filter, Clock } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Input } from '@/components/ui/Input';
import { useGetAuditLogsQuery } from '@/api/settingsApi';
import { AuditLogItem } from '@/types';

export const AuditLogsPage: React.FC = () => {
  const [search, setSearch] = useState('');
  const { data: logsData } = useGetAuditLogsQuery({ search });

  const fallbackLogs: AuditLogItem[] = [
    {
      id: 'log-1',
      userId: 'u-1',
      userEmail: 'admin@aicode.com',
      action: 'REPOSITORY_REVIEW_TRIGGERED',
      entityType: 'PULL_REQUEST',
      entityId: 'pr-1',
      ipAddress: '192.168.1.45',
      details: 'Triggered DEEP review with GEMINI gemini-2.0-flash',
      createdAt: '2026-11-17T08:45:00Z',
    },
    {
      id: 'log-2',
      userId: 'u-1',
      userEmail: 'admin@aicode.com',
      action: 'AI_PROVIDER_KEY_UPDATED',
      entityType: 'AI_PROVIDER_CONFIG',
      entityId: 'p-1',
      ipAddress: '192.168.1.45',
      details: 'Updated Google Gemini API credentials (AES-256 encrypted)',
      createdAt: '2026-11-17T07:30:00Z',
    },
    {
      id: 'log-3',
      userId: 'u-2',
      userEmail: 'alex@aicode.com',
      action: 'FINDING_RESOLVED',
      entityType: 'REVIEW_FINDING',
      entityId: 'find-2',
      ipAddress: '10.0.4.18',
      details: 'Marked N+1 query issue as RESOLVED with applied batch fetch',
      createdAt: '2026-11-16T16:00:00Z',
    },
  ];

  const logs = logsData && logsData.length > 0 ? logsData : fallbackLogs;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          Security Audit Trail <FileText className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Immutable event log tracking authentication, role modifications, AI config changes, and review triggers
        </p>
      </div>

      <div className="max-w-md">
        <Input
          placeholder="Filter audit events..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          leftIcon={<Search className="w-4 h-4 text-slate-400" />}
        />
      </div>

      <Card className="overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-950/80 text-slate-400 border-b border-slate-800 uppercase font-semibold">
              <tr>
                <th className="py-3 px-4">Action</th>
                <th className="py-3 px-4">User</th>
                <th className="py-3 px-4">Entity</th>
                <th className="py-3 px-4">IP Address</th>
                <th className="py-3 px-4">Details</th>
                <th className="py-3 px-4 text-right">Timestamp</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800 text-slate-200">
              {logs.map((log) => (
                <tr key={log.id} className="hover:bg-slate-800/40">
                  <td className="py-3 px-4 font-mono font-semibold text-indigo-400">
                    {log.action}
                  </td>
                  <td className="py-3 px-4 font-mono text-slate-300">{log.userEmail}</td>
                  <td className="py-3 px-4 font-mono text-slate-400">{log.entityType}</td>
                  <td className="py-3 px-4 font-mono text-slate-400">{log.ipAddress}</td>
                  <td className="py-3 px-4 text-slate-300">{log.details}</td>
                  <td className="py-3 px-4 text-right font-mono text-slate-500">
                    {new Date(log.createdAt).toLocaleString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
};

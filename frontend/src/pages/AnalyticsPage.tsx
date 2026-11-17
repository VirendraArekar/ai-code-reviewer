import React from 'react';
import {
  BarChart3,
  TrendingUp,
  ShieldCheck,
  Zap,
  Clock,
  Sparkles,
} from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { useGetDashboardStatsQuery } from '@/api/repositoryApi';
import {
  AreaChart,
  Area,
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';

export const AnalyticsPage: React.FC = () => {
  const { data: stats } = useGetDashboardStatsQuery();

  const monthlyTrend = [
    { month: 'Jun', reviews: 45, securityIssues: 12, qualityScore: 81 },
    { month: 'Jul', reviews: 68, securityIssues: 15, qualityScore: 83 },
    { month: 'Aug', reviews: 84, securityIssues: 9, qualityScore: 85 },
    { month: 'Sep', reviews: 110, securityIssues: 7, qualityScore: 88 },
    { month: 'Oct', reviews: 142, securityIssues: 5, qualityScore: 91 },
    { month: 'Nov', reviews: 165, securityIssues: 3, qualityScore: 93 },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          Engineering Analytics & Trends <BarChart3 className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Historical code quality progression, defect density reduction, and review velocity trends
        </p>
      </div>

      {/* High-level KPIs */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card className="p-5 space-y-2">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>Overall Quality Improvement</span>
            <TrendingUp className="w-4 h-4 text-emerald-400" />
          </div>
          <div className="text-2xl font-bold font-mono text-white">+14.8%</div>
          <p className="text-xs text-emerald-400">Consistent upward trend over the last 6 months</p>
        </Card>

        <Card className="p-5 space-y-2">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>Vulnerability Defect Density</span>
            <ShieldCheck className="w-4 h-4 text-purple-400" />
          </div>
          <div className="text-2xl font-bold font-mono text-white">-75%</div>
          <p className="text-xs text-purple-400">Decreased from 12 criticals/mo to 3/mo</p>
        </Card>

        <Card className="p-5 space-y-2">
          <div className="flex items-center justify-between text-xs text-slate-400">
            <span>Median Review Turnaround</span>
            <Clock className="w-4 h-4 text-amber-400" />
          </div>
          <div className="text-2xl font-bold font-mono text-white">18.4s</div>
          <p className="text-xs text-amber-400">99.4% sub-minute autonomous completion</p>
        </Card>
      </div>

      {/* Main Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-6 space-y-4">
          <CardTitle>Code Quality Score Trend (Last 6 Months)</CardTitle>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={monthlyTrend}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="month" stroke="#64748b" fontSize={11} />
                <YAxis domain={[70, 100]} stroke="#64748b" fontSize={11} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#0f172a',
                    borderColor: '#334155',
                    borderRadius: '0.75rem',
                    fontSize: '12px',
                  }}
                />
                <Line
                  type="monotone"
                  dataKey="qualityScore"
                  stroke="#10b981"
                  strokeWidth={3}
                  dot={{ r: 4, fill: '#10b981' }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card className="p-6 space-y-4">
          <CardTitle>Security Vulnerabilities Identified</CardTitle>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={monthlyTrend}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="month" stroke="#64748b" fontSize={11} />
                <YAxis stroke="#64748b" fontSize={11} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#0f172a',
                    borderColor: '#334155',
                    borderRadius: '0.75rem',
                    fontSize: '12px',
                  }}
                />
                <Bar dataKey="securityIssues" fill="#ef4444" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>
    </div>
  );
};

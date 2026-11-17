import React, { useState } from 'react';
import { NavLink, useSearchParams } from 'react-router-dom';
import {
  GitPullRequest,
  Search,
  Filter,
  CheckCircle2,
  Clock,
  Sparkles,
  Play,
  ArrowUpRight,
  GitBranch,
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Input, Select } from '@/components/ui/Input';
import { useGetPullRequestsQuery, useTriggerReviewMutation } from '@/api/repositoryApi';
import { PullRequest } from '@/types';
import { formatRelativeTime } from '@/lib/utils';

export const PullRequestsPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const repoIdParam = searchParams.get('repoId') || '';
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'OPEN' | 'CLOSED' | 'MERGED'>('ALL');

  const { data: pullRequests, isLoading, refetch } = useGetPullRequestsQuery({
    repoId: repoIdParam || undefined,
    state: statusFilter === 'ALL' ? undefined : statusFilter,
    search: searchQuery,
  });

  const [triggerReview, { isLoading: isTriggering }] = useTriggerReviewMutation();

  const fallbackPrs: PullRequest[] = [
    {
      id: 'pr-1',
      repositoryId: 'repo-1',
      repositoryName: 'acme/api-gateway',
      githubPrNumber: 42,
      title: 'feat: add OAuth2 refresh token rotation & revocation endpoints',
      description: 'Implements RFC 6749 refresh token rotation with single-use detection and replay mitigation.',
      authorUsername: 'alexdev',
      authorAvatarUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=64&h=64&fit=crop&crop=faces',
      state: 'OPEN',
      sourceBranch: 'feat/oauth2-rotation',
      targetBranch: 'main',
      htmlUrl: 'https://github.com/acme/api-gateway/pull/42',
      additions: 342,
      deletions: 48,
      changedFilesCount: 7,
      latestReviewId: 'rev-1',
      latestReviewStatus: 'COMPLETED',
      qualityScore: 91,
      securityScore: 89,
      criticalCount: 0,
      highCount: 1,
      mediumCount: 3,
      lowCount: 2,
      infoCount: 1,
      createdAt: '2026-11-17T08:15:00Z',
      updatedAt: '2026-11-17T08:45:00Z',
    },
    {
      id: 'pr-2',
      repositoryId: 'repo-1',
      repositoryName: 'acme/api-gateway',
      githubPrNumber: 89,
      title: 'refactor: switch user permission check to batch SQL fetch to avoid N+1 queries',
      description: 'Replaces eager iteration over member permissions with single indexed query.',
      authorUsername: 'johndoe',
      authorAvatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=64&h=64&fit=crop&crop=faces',
      state: 'OPEN',
      sourceBranch: 'perf/batch-permissions',
      targetBranch: 'main',
      htmlUrl: 'https://github.com/acme/api-gateway/pull/89',
      additions: 120,
      deletions: 95,
      changedFilesCount: 4,
      latestReviewId: 'rev-2',
      latestReviewStatus: 'COMPLETED',
      qualityScore: 96,
      securityScore: 98,
      criticalCount: 0,
      highCount: 0,
      mediumCount: 1,
      lowCount: 1,
      infoCount: 0,
      createdAt: '2026-11-16T14:00:00Z',
      updatedAt: '2026-11-16T14:30:00Z',
    },
    {
      id: 'pr-3',
      repositoryId: 'repo-2',
      repositoryName: 'acme/auth-service',
      githubPrNumber: 15,
      title: 'fix: validate external webhook signature with HMAC-SHA256 constant time comparison',
      description: 'Fixes timing attack vulnerability in signature verification.',
      authorUsername: 'sarah_sec',
      authorAvatarUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=64&h=64&fit=crop&crop=faces',
      state: 'OPEN',
      sourceBranch: 'sec/fix-timing-attack',
      targetBranch: 'main',
      htmlUrl: 'https://github.com/acme/auth-service/pull/15',
      additions: 54,
      deletions: 12,
      changedFilesCount: 2,
      latestReviewId: 'rev-3',
      latestReviewStatus: 'COMPLETED',
      qualityScore: 74,
      securityScore: 70,
      criticalCount: 1,
      highCount: 2,
      mediumCount: 1,
      lowCount: 0,
      infoCount: 0,
      createdAt: '2026-11-15T11:20:00Z',
      updatedAt: '2026-11-15T12:00:00Z',
    },
  ];

  const prList = pullRequests && pullRequests.length > 0 ? pullRequests : fallbackPrs;
  const filteredPrs = prList.filter(
    (pr) =>
      pr.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      pr.repositoryName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      pr.authorUsername.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleTriggerReview = async (prId: string) => {
    try {
      await triggerReview({ prId, mode: 'STANDARD' }).unwrap();
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            Pull Requests <GitPullRequest className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Browse pull requests, view AI quality scores, and inspect detailed code review findings
          </p>
        </div>
      </div>

      {/* Filters Bar */}
      <div className="flex flex-col sm:flex-row items-center gap-4">
        <div className="relative flex-1 w-full">
          <Input
            placeholder="Search PR by title, repository, author..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            leftIcon={<Search className="w-4 h-4 text-slate-400" />}
          />
        </div>

        <div className="w-full sm:w-48">
          <Select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as any)}
          >
            <option value="ALL">All States</option>
            <option value="OPEN">Open Only</option>
            <option value="MERGED">Merged</option>
            <option value="CLOSED">Closed</option>
          </Select>
        </div>
      </div>

      {/* PR Cards / Table */}
      <div className="space-y-3">
        {filteredPrs.map((pr) => (
          <Card
            key={pr.id}
            glow
            className="p-5 flex flex-col md:flex-row items-start md:items-center justify-between gap-4 hover:border-indigo-500/40 transition-all"
          >
            <div className="space-y-2 flex-1">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="font-mono text-xs font-semibold px-2 py-0.5 rounded bg-indigo-500/10 text-indigo-300 border border-indigo-500/20">
                  {pr.repositoryName}
                </span>
                <span className="text-xs font-mono text-slate-400">
                  #{pr.githubPrNumber}
                </span>
                <span
                  className={`text-[10px] font-bold uppercase px-2 py-0.5 rounded-full ${
                    pr.state === 'OPEN'
                      ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30'
                      : 'bg-slate-800 text-slate-400'
                  }`}
                >
                  {pr.state}
                </span>
                <span className="text-xs text-slate-500">
                  opened {formatRelativeTime(pr.createdAt)} by @{pr.authorUsername}
                </span>
              </div>

              <NavLink
                to={`/pull-requests/${pr.id}`}
                className="text-base font-semibold text-white hover:text-indigo-400 transition-colors block"
              >
                {pr.title}
              </NavLink>

              <div className="flex items-center gap-4 text-xs text-slate-400 font-mono">
                <span className="flex items-center gap-1">
                  <GitBranch className="w-3.5 h-3.5 text-slate-500" />
                  {pr.sourceBranch} → {pr.targetBranch}
                </span>
                <span className="text-emerald-400 font-bold">+{pr.additions}</span>
                <span className="text-rose-400 font-bold">-{pr.deletions}</span>
                <span>{pr.changedFilesCount} files</span>
              </div>
            </div>

            {/* Right side: Findings, Quality score & Action button */}
            <div className="flex items-center gap-6 w-full md:w-auto justify-between md:justify-end pt-3 md:pt-0 border-t md:border-t-0 border-slate-800">
              {/* Severity Counts */}
              <div className="flex items-center gap-2 text-xs">
                {pr.criticalCount > 0 && (
                  <span className="px-2 py-0.5 rounded bg-rose-500/20 text-rose-400 font-mono font-bold border border-rose-500/30">
                    {pr.criticalCount} Critical
                  </span>
                )}
                {pr.highCount > 0 && (
                  <span className="px-2 py-0.5 rounded bg-orange-500/20 text-orange-400 font-mono font-bold border border-orange-500/30">
                    {pr.highCount} High
                  </span>
                )}
                {pr.criticalCount === 0 && pr.highCount === 0 && (
                  <span className="text-xs text-emerald-400 font-semibold flex items-center gap-1">
                    <CheckCircle2 className="w-3.5 h-3.5" /> Clean PR
                  </span>
                )}
              </div>

              {/* Quality Score */}
              {pr.qualityScore && (
                <div className="text-center">
                  <span className="text-[10px] text-slate-400 uppercase font-semibold block">
                    Quality
                  </span>
                  <span className="font-mono text-sm font-bold text-white">
                    {pr.qualityScore}/100
                  </span>
                </div>
              )}

              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleTriggerReview(pr.id)}
                  isLoading={isTriggering}
                  leftIcon={<Sparkles className="w-3.5 h-3.5 text-indigo-400" />}
                >
                  Re-analyze
                </Button>
                <NavLink to={`/pull-requests/${pr.id}`}>
                  <Button variant="primary" size="sm">
                    View Review
                  </Button>
                </NavLink>
              </div>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import {
  GitPullRequest,
  Search,
  Filter,
  ExternalLink,
  ChevronRight,
  ShieldAlert,
  Sparkles,
  RefreshCw,
  Clock,
  User,
  Plus,
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input, Select } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { notify } from '@/lib/notify';
import { useGetPullRequestsQuery, useTriggerReviewMutation, useSyncPullRequestMutation } from '@/api/repositoryApi';
import { PullRequest } from '@/types';

export const PullRequestsPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'OPEN' | 'MERGED' | 'CLOSED'>('ALL');
  const [syncingPrId, setSyncingPrId] = useState<string | null>(null);

  const { data: pullRequests, isLoading, refetch } = useGetPullRequestsQuery({
    state: statusFilter === 'ALL' ? undefined : statusFilter,
    search: searchQuery,
  });
  const [triggerReview, { isLoading: isReviewing }] = useTriggerReviewMutation();
  const [syncPullRequest] = useSyncPullRequestMutation();

  const prList: PullRequest[] = pullRequests || [];
  const filteredPrs = prList.filter(
    (pr) =>
      pr.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      pr.repositoryName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      pr.authorUsername.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleTriggerReview = async (prId: string) => {
    try {
      notify.info('AI Review Started', 'Analyzing code diff, checking security vulnerabilities, and evaluating best practices...');
      await triggerReview({ prId, mode: 'STANDARD' }).unwrap();
      notify.success('AI Review Complete', 'Pull request review analysis has finished successfully.');
      refetch();
    } catch (err: any) {
      console.error(err);
      notify.error('Review Failed', err?.data?.message || 'Failed to complete AI review. Check AI provider settings.');
    }
  };

  const handleSyncPr = async (prId: string) => {
    try {
      setSyncingPrId(prId);
      await syncPullRequest(prId).unwrap();
      notify.success('Pull Request Synced', 'Fetched latest commits and changed files from GitHub.');
      refetch();
    } catch (err: any) {
      console.error(err);
      notify.error('Sync Failed', err?.data?.message || 'Failed to sync pull request diff.');
    } finally {
      setSyncingPrId(null);
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

        <div className="flex items-center gap-3">
          <Button
            variant="outline"
            size="sm"
            onClick={() => refetch()}
            leftIcon={<RefreshCw className="w-3.5 h-3.5" />}
          >
            Refresh
          </Button>
          <NavLink to="/repositories">
            <Button variant="primary" size="sm" leftIcon={<Plus className="w-4 h-4" />}>
              Connect Repo
            </Button>
          </NavLink>
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

      {/* Empty State */}
      {filteredPrs.length === 0 && !isLoading && (
        <Card className="p-12 text-center flex flex-col items-center justify-center border-dashed border-slate-800">
          <div className="w-16 h-16 rounded-2xl bg-indigo-500/10 text-indigo-400 flex items-center justify-center mb-4">
            <GitPullRequest className="w-8 h-8" />
          </div>
          <h3 className="text-lg font-semibold text-white">No Pull Requests Found</h3>
          <p className="text-sm text-slate-400 max-w-md mt-1 mb-6">
            Connect a GitHub repository to automatically import open pull requests, or click "Sync PRs" on your connected repository.
          </p>
          <NavLink to="/repositories">
            <Button variant="primary" leftIcon={<Plus className="w-4 h-4" />}>
              View Connected Repositories
            </Button>
          </NavLink>
        </Card>
      )}

      {/* PR Cards / Table */}
      <div className="space-y-3">
        {filteredPrs.map((pr) => (
          <Card
            key={pr.id}
            glow
            className="p-4 sm:p-5 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 hover:border-indigo-500/40 transition-all"
          >
            <div className="space-y-2 flex-1">
              <div className="flex flex-wrap items-center gap-2">
                <span className="text-xs font-mono text-slate-400">
                  {pr.repositoryName}
                </span>
                <span className="text-xs font-mono text-slate-500">•</span>
                <span className="text-xs font-mono text-indigo-400 font-semibold">
                  #{pr.githubPrNumber}
                </span>
                <Badge status={pr.state as any}>
                  {pr.state}
                </Badge>
              </div>

              <div>
                <NavLink
                  to={`/pull-requests/${pr.id}`}
                  className="text-base font-semibold text-white hover:text-indigo-400 transition-colors line-clamp-1"
                >
                  {pr.title}
                </NavLink>
                <p className="text-xs text-slate-400 line-clamp-1 mt-0.5">
                  {pr.description || 'No description provided.'}
                </p>
              </div>

              <div className="flex flex-wrap items-center gap-4 text-xs text-slate-400">
                <span className="flex items-center gap-1">
                  <User className="w-3.5 h-3.5 text-slate-500" />
                  {pr.authorUsername}
                </span>
                <span className="font-mono text-slate-500">
                  {pr.sourceBranch} → {pr.targetBranch}
                </span>
                <span className="text-emerald-400 font-mono">+{pr.additions}</span>
                <span className="text-rose-400 font-mono">-{pr.deletions}</span>
                <span>{pr.changedFilesCount} files</span>
              </div>
            </div>

            {/* Right Side Quality Scores & Action Buttons */}
            <div className="flex items-center gap-4 self-end sm:self-center">
              {pr.qualityScore ? (
                <div className="flex items-center gap-3">
                  <div className="text-right hidden sm:block">
                    <p className="text-[10px] text-slate-500 uppercase tracking-wider font-semibold">
                      Quality / Security
                    </p>
                    <p className="text-sm font-bold text-white font-mono">
                      {pr.qualityScore}% / {pr.securityScore}%
                    </p>
                  </div>
                  <div className="w-10 h-10 rounded-xl bg-slate-800 flex items-center justify-center font-bold text-xs text-indigo-300 border border-slate-700">
                    {pr.qualityScore}
                  </div>
                </div>
              ) : (
                <Button
                  variant="primary"
                  size="sm"
                  isLoading={isReviewing}
                  onClick={() => handleTriggerReview(pr.id)}
                  leftIcon={<Sparkles className="w-3.5 h-3.5" />}
                >
                  Review
                </Button>
              )}

              <Button
                variant="outline"
                size="sm"
                isLoading={syncingPrId === pr.id}
                onClick={() => handleSyncPr(pr.id)}
                leftIcon={<RefreshCw className="w-3 h-3" />}
              >
                Sync Diff
              </Button>

              <NavLink to={`/pull-requests/${pr.id}`}>
                <Button variant="secondary" size="sm" rightIcon={<ChevronRight className="w-3.5 h-3.5" />}>
                  Details
                </Button>
              </NavLink>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

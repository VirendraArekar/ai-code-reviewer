import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import {
  FolderGit2,
  Plus,
  Search,
  RefreshCw,
  Sliders,
  CheckCircle2,
  XCircle,
  ExternalLink,
  ChevronRight,
  GitBranch,
  Shield,
  Sparkles,
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Modal } from '@/components/ui/Modal';
import { Input } from '@/components/ui/Input';
import {
  useGetRepositoriesQuery,
  useCreateRepositoryMutation,
  useSyncGithubRepositoriesMutation,
} from '@/api/repositoryApi';
import { Repository } from '@/types';

export const RepositoriesPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isConnectModalOpen, setIsConnectModalOpen] = useState(false);
  const [newRepoName, setNewRepoName] = useState('');
  const [newRepoUrl, setNewRepoUrl] = useState('');

  const { data: repositories, isLoading, refetch } = useGetRepositoriesQuery({
    search: searchQuery,
  });
  const [createRepository, { isLoading: isCreating }] = useCreateRepositoryMutation();
  const [syncGithub, { isLoading: isSyncing }] = useSyncGithubRepositoriesMutation();

  const fallbackRepos: Repository[] = [
    {
      id: 'repo-1',
      organizationId: 'org-1',
      githubRepoId: 101,
      name: 'api-gateway',
      fullName: 'acme/api-gateway',
      ownerLogin: 'acme',
      defaultBranch: 'main',
      privateRepo: true,
      htmlUrl: 'https://github.com/acme/api-gateway',
      primaryLanguage: 'Java',
      autoReviewEnabled: true,
      inlineCommentsEnabled: true,
      reviewMode: 'DEEP',
      minSeverity: 'MEDIUM',
      aiProvider: 'GEMINI',
      aiModel: 'gemini-2.0-flash',
      maxDiffSizeKb: 500,
      openPullRequestCount: 4,
      totalFindingsCount: 18,
      avgQualityScore: 92,
      createdAt: '2026-01-15T10:00:00Z',
      updatedAt: '2026-11-17T09:30:00Z',
    },
    {
      id: 'repo-2',
      organizationId: 'org-1',
      githubRepoId: 102,
      name: 'auth-service',
      fullName: 'acme/auth-service',
      ownerLogin: 'acme',
      defaultBranch: 'main',
      privateRepo: true,
      htmlUrl: 'https://github.com/acme/auth-service',
      primaryLanguage: 'TypeScript',
      autoReviewEnabled: true,
      inlineCommentsEnabled: true,
      reviewMode: 'SECURITY_FOCUSED',
      minSeverity: 'LOW',
      aiProvider: 'OPENAI',
      aiModel: 'gpt-4o',
      maxDiffSizeKb: 800,
      openPullRequestCount: 2,
      totalFindingsCount: 32,
      avgQualityScore: 88,
      createdAt: '2026-02-10T12:00:00Z',
      updatedAt: '2026-11-17T08:00:00Z',
    },
    {
      id: 'repo-3',
      organizationId: 'org-1',
      githubRepoId: 103,
      name: 'payment-service',
      fullName: 'acme/payment-service',
      ownerLogin: 'acme',
      defaultBranch: 'main',
      privateRepo: true,
      htmlUrl: 'https://github.com/acme/payment-service',
      primaryLanguage: 'Go',
      autoReviewEnabled: true,
      inlineCommentsEnabled: false,
      reviewMode: 'STANDARD',
      minSeverity: 'HIGH',
      aiProvider: 'GEMINI',
      aiModel: 'gemini-2.0-flash',
      maxDiffSizeKb: 600,
      openPullRequestCount: 1,
      totalFindingsCount: 12,
      avgQualityScore: 78,
      createdAt: '2026-03-01T15:00:00Z',
      updatedAt: '2026-11-16T14:20:00Z',
    },
    {
      id: 'repo-4',
      organizationId: 'org-1',
      githubRepoId: 104,
      name: 'frontend-web',
      fullName: 'acme/frontend-web',
      ownerLogin: 'acme',
      defaultBranch: 'main',
      privateRepo: false,
      htmlUrl: 'https://github.com/acme/frontend-web',
      primaryLanguage: 'TypeScript',
      autoReviewEnabled: true,
      inlineCommentsEnabled: true,
      reviewMode: 'PERFORMANCE_FOCUSED',
      minSeverity: 'MEDIUM',
      aiProvider: 'OPENAI',
      aiModel: 'gpt-4o-mini',
      maxDiffSizeKb: 1000,
      openPullRequestCount: 5,
      totalFindingsCount: 24,
      avgQualityScore: 86,
      createdAt: '2026-04-12T09:00:00Z',
      updatedAt: '2026-11-17T06:45:00Z',
    },
  ];

  const repoList = repositories && repositories.length > 0 ? repositories : fallbackRepos;
  const filteredRepos = repoList.filter(
    (r) =>
      r.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      r.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (r.primaryLanguage && r.primaryLanguage.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const handleCreateRepo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newRepoName) return;
    try {
      await createRepository({
        name: newRepoName,
        fullName: `acme/${newRepoName}`,
        htmlUrl: newRepoUrl || `https://github.com/acme/${newRepoName}`,
        defaultBranch: 'main',
        autoReviewEnabled: true,
        inlineCommentsEnabled: true,
        reviewMode: 'STANDARD',
        minSeverity: 'MEDIUM',
        aiProvider: 'GEMINI',
        aiModel: 'gemini-2.0-flash',
      }).unwrap();
      setIsConnectModalOpen(false);
      setNewRepoName('');
      setNewRepoUrl('');
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
            Connected Repositories <FolderGit2 className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Manage repository review policies, AI configurations, and automatic webhook reviews
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Button
            variant="outline"
            size="sm"
            onClick={() => syncGithub()}
            isLoading={isSyncing}
            leftIcon={<RefreshCw className="w-3.5 h-3.5" />}
          >
            Sync GitHub
          </Button>
          <Button
            variant="primary"
            size="sm"
            onClick={() => setIsConnectModalOpen(true)}
            leftIcon={<Plus className="w-4 h-4" />}
          >
            Connect Repository
          </Button>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex items-center gap-4">
        <div className="relative flex-1">
          <Input
            placeholder="Search by repository name, language, or owner..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            leftIcon={<Search className="w-4 h-4 text-slate-400" />}
          />
        </div>
      </div>

      {/* Repository Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {filteredRepos.map((repo) => (
          <Card
            key={repo.id}
            glow
            className="p-5 flex flex-col justify-between space-y-4 hover:border-indigo-500/40 transition-all"
          >
            <div className="space-y-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-2.5">
                  <div className="w-9 h-9 rounded-xl bg-slate-800 border border-slate-700/80 flex items-center justify-center text-indigo-400 font-bold">
                    <FolderGit2 className="w-4 h-4" />
                  </div>
                  <div>
                    <NavLink
                      to={`/repositories/${repo.id}`}
                      className="font-semibold text-white hover:text-indigo-400 text-base flex items-center gap-1.5"
                    >
                      {repo.name}
                      <ChevronRight className="w-4 h-4 text-slate-500" />
                    </NavLink>
                    <p className="text-xs text-slate-400 font-mono">{repo.fullName}</p>
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  {repo.autoReviewEnabled ? (
                    <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 text-[10px] font-semibold uppercase">
                      <CheckCircle2 className="w-3 h-3" /> Auto Review
                    </span>
                  ) : (
                    <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 border border-slate-700 text-[10px] font-semibold uppercase">
                      Paused
                    </span>
                  )}
                </div>
              </div>

              {/* Badges / Tech details */}
              <div className="flex flex-wrap items-center gap-2 text-xs">
                {repo.primaryLanguage && (
                  <span className="px-2 py-0.5 rounded-md bg-slate-800 text-slate-300 border border-slate-700 font-medium">
                    {repo.primaryLanguage}
                  </span>
                )}
                <span className="px-2 py-0.5 rounded-md bg-purple-500/10 text-purple-300 border border-purple-500/20 font-medium">
                  {repo.reviewMode}
                </span>
                <span className="px-2 py-0.5 rounded-md bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 font-mono text-[11px]">
                  {repo.aiProvider}: {repo.aiModel}
                </span>
              </div>
            </div>

            {/* Bottom Metrics Bar */}
            <div className="pt-3 border-t border-slate-800/80 flex items-center justify-between text-xs text-slate-400">
              <div className="flex items-center gap-4">
                <span>
                  <strong className="text-white font-mono">{repo.openPullRequestCount}</strong> open PRs
                </span>
                <span>
                  <strong className="text-rose-400 font-mono">{repo.totalFindingsCount}</strong> findings
                </span>
              </div>

              <div className="flex items-center gap-3">
                {repo.avgQualityScore && (
                  <div className="flex items-center gap-1">
                    <span className="text-[11px]">Health:</span>
                    <span className="font-mono font-bold text-emerald-400">
                      {repo.avgQualityScore}%
                    </span>
                  </div>
                )}
                <NavLink to={`/repositories/${repo.id}`}>
                  <Button variant="ghost" size="sm">
                    Configure
                  </Button>
                </NavLink>
              </div>
            </div>
          </Card>
        ))}
      </div>

      {/* Connect Modal */}
      <Modal
        isOpen={isConnectModalOpen}
        onClose={() => setIsConnectModalOpen(false)}
        title="Connect New GitHub Repository"
        description="Add a repository to enable continuous automated pull request scanning"
      >
        <form onSubmit={handleCreateRepo} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Repository Name
            </label>
            <Input
              placeholder="e.g. billing-microservice"
              value={newRepoName}
              onChange={(e) => setNewRepoName(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              GitHub URL (optional)
            </label>
            <Input
              placeholder="https://github.com/acme/billing-microservice"
              value={newRepoUrl}
              onChange={(e) => setNewRepoUrl(e.target.value)}
            />
          </div>

          <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-800">
            <Button variant="ghost" type="button" onClick={() => setIsConnectModalOpen(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" isLoading={isCreating}>
              Connect & Enable Auto-Review
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

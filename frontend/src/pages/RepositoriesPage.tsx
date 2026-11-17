import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import {
  FolderGit2,
  Plus,
  Search,
  ExternalLink,
  Shield,
  Bot,
  Sliders,
  ChevronRight,
  GitPullRequest,
  CheckCircle2,
  Clock,
  RefreshCw,
} from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import {
  useGetRepositoriesQuery,
  useCreateRepositoryMutation,
  useSyncRepositoryMutation,
} from '@/api/repositoryApi';
import { Repository } from '@/types';

export const RepositoriesPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isConnectModalOpen, setIsConnectModalOpen] = useState(false);
  const [newRepoInput, setNewRepoInput] = useState('');
  const [selectedProvider, setSelectedProvider] = useState<'GEMINI' | 'OPENAI' | 'OLLAMA'>('GEMINI');
  const [selectedMode, setSelectedMode] = useState<string>('STANDARD');

  const { data: repositories, isLoading, refetch } = useGetRepositoriesQuery({
    search: searchQuery,
  });
  const [createRepository, { isLoading: isCreating }] = useCreateRepositoryMutation();
  const [syncRepository, { isLoading: isSyncing }] = useSyncRepositoryMutation();
  const [syncingRepoId, setSyncingRepoId] = useState<string | null>(null);

  const repoList: Repository[] = repositories || [];

  const filteredRepos = repoList.filter(
    (r) =>
      r.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      r.fullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (r.primaryLanguage && r.primaryLanguage.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const handleCreateRepo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newRepoInput.trim()) return;

    let input = newRepoInput.trim();
    if (input.startsWith('https://github.com/')) {
      input = input.replace('https://github.com/', '');
    }
    if (input.endsWith('.git')) {
      input = input.substring(0, input.length - 4);
    }

    const parts = input.split('/');
    const repoName = parts.length > 1 ? parts[1] : parts[0];
    const fullName = parts.length > 1 ? input : `workspace/${repoName}`;

    try {
      await createRepository({
        name: repoName,
        fullName: fullName,
        htmlUrl: `https://github.com/${fullName}`,
        defaultBranch: 'main',
        autoReviewEnabled: true,
        inlineCommentsEnabled: true,
        reviewMode: selectedMode as any,
        minSeverity: 'MEDIUM',
        aiProvider: selectedProvider,
        aiModel: selectedProvider === 'GEMINI' ? 'gemini-2.0-flash' : selectedProvider === 'OPENAI' ? 'gpt-4o' : 'deepseek-coder:6.7b',
      }).unwrap();
      setIsConnectModalOpen(false);
      setNewRepoInput('');
      refetch();
    } catch (err) {
      console.error('Failed to connect repository', err);
    }
  };

  const handleSyncRepo = async (repoId: string) => {
    try {
      setSyncingRepoId(repoId);
      await syncRepository(repoId).unwrap();
      refetch();
    } catch (err) {
      console.error('Sync failed', err);
    } finally {
      setSyncingRepoId(null);
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
            Manage repository review policies, AI configurations, and pull request synchronization
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
            placeholder="Search by repository name, owner, or language..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            leftIcon={<Search className="w-4 h-4 text-slate-400" />}
          />
        </div>
      </div>

      {/* Empty State */}
      {filteredRepos.length === 0 && !isLoading && (
        <Card className="p-12 text-center flex flex-col items-center justify-center border-dashed border-slate-800">
          <div className="w-16 h-16 rounded-2xl bg-indigo-500/10 text-indigo-400 flex items-center justify-center mb-4">
            <FolderGit2 className="w-8 h-8" />
          </div>
          <h3 className="text-lg font-semibold text-white">No Repositories Connected</h3>
          <p className="text-sm text-slate-400 max-w-md mt-1 mb-6">
            Connect your GitHub repository to enable autonomous AI code reviews, detect security vulnerabilities, and sync pull requests.
          </p>
          <div className="flex items-center gap-3">
            <Button
              variant="primary"
              onClick={() => setIsConnectModalOpen(true)}
              leftIcon={<Plus className="w-4 h-4" />}
            >
              Connect GitHub Repository
            </Button>
            <NavLink to="/settings/github">
              <Button variant="outline" leftIcon={<ExternalLink className="w-4 h-4" />}>
                Configure GitHub App
              </Button>
            </NavLink>
          </div>
        </Card>
      )}

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
                    <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 text-[10px] font-semibold uppercase">
                      Manual Only
                    </span>
                  )}
                </div>
              </div>

              {/* Badges / Meta Info */}
              <div className="flex flex-wrap items-center gap-2 text-xs text-slate-400">
                <span className="px-2 py-0.5 rounded bg-slate-800/80 border border-slate-700/50 text-slate-300">
                  {repo.primaryLanguage || 'Java'}
                </span>
                <span className="px-2 py-0.5 rounded bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 font-medium">
                  {repo.reviewMode}
                </span>
                <span className="px-2 py-0.5 rounded bg-purple-500/10 text-purple-300 border border-purple-500/20">
                  {repo.aiProvider} • {repo.aiModel}
                </span>
              </div>
            </div>

            {/* Bottom Metrics Bar */}
            <div className="pt-3 border-t border-slate-800/80 flex items-center justify-between text-xs text-slate-400">
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-1.5">
                  <GitPullRequest className="w-3.5 h-3.5 text-blue-400" />
                  <span>
                    <strong className="text-white">{repo.openPullRequestCount}</strong> open PRs
                  </span>
                </div>
                <div className="flex items-center gap-1.5">
                  <Shield className="w-3.5 h-3.5 text-amber-400" />
                  <span>
                    <strong className="text-white">{repo.totalFindingsCount}</strong> issues
                  </span>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  isLoading={syncingRepoId === repo.id}
                  onClick={() => handleSyncRepo(repo.id)}
                  leftIcon={<RefreshCw className="w-3 h-3" />}
                >
                  Sync PRs
                </Button>
                <NavLink to={`/repositories/${repo.id}`}>
                  <Button variant="secondary" size="sm" rightIcon={<ChevronRight className="w-3 h-3" />}>
                    Settings
                  </Button>
                </NavLink>
              </div>
            </div>
          </Card>
        ))}
      </div>

      {/* Connect Repository Modal */}
      {isConnectModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm animate-fade-in">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-lg p-6 space-y-5 shadow-2xl">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-bold text-white flex items-center gap-2">
                Connect GitHub Repository <FolderGit2 className="w-5 h-5 text-indigo-400" />
              </h3>
              <button
                onClick={() => setIsConnectModalOpen(false)}
                className="text-slate-400 hover:text-white text-sm"
              >
                ✕
              </button>
            </div>

            <form onSubmit={handleCreateRepo} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">
                  GitHub Repository (URL or owner/repo)
                </label>
                <Input
                  required
                  placeholder="e.g. VirendraArekar/ai-code-reviewer or https://github.com/..."
                  value={newRepoInput}
                  onChange={(e) => setNewRepoInput(e.target.value)}
                />
                <p className="text-[11px] text-slate-400 mt-1">
                  Metadata, default branch, language, and open PRs will be synced from GitHub automatically.
                </p>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1">
                    AI Review Provider
                  </label>
                  <select
                    value={selectedProvider}
                    onChange={(e) => setSelectedProvider(e.target.value as any)}
                    className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-indigo-500"
                  >
                    <option value="GEMINI">Google Gemini (Flash 2.0)</option>
                    <option value="OPENAI">OpenAI (GPT-4o)</option>
                    <option value="OLLAMA">Ollama (Local LLM)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1">
                    Review Mode
                  </label>
                  <select
                    value={selectedMode}
                    onChange={(e) => setSelectedMode(e.target.value)}
                    className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-indigo-500"
                  >
                    <option value="STANDARD">Standard Review</option>
                    <option value="DEEP">Deep Review</option>
                    <option value="SECURITY_FOCUSED">Security-Focused</option>
                    <option value="PERFORMANCE_FOCUSED">Performance-Focused</option>
                    <option value="QUICK">Quick Review</option>
                  </select>
                </div>
              </div>

              <div className="p-3 rounded-lg bg-indigo-500/10 border border-indigo-500/20 text-xs text-indigo-300">
                💡 ReviewAI will immediately scan the repository for open pull requests and execute the configured AI review engine on all active diffs.
              </div>

              <div className="flex items-center justify-end gap-3 pt-2">
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={() => setIsConnectModalOpen(false)}
                >
                  Cancel
                </Button>
                <Button
                  type="submit"
                  variant="primary"
                  size="sm"
                  isLoading={isCreating}
                  leftIcon={<Plus className="w-4 h-4" />}
                >
                  Connect & Sync
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

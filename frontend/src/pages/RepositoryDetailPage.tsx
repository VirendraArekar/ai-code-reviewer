import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, NavLink } from 'react-router-dom';
import {
  FolderGit2,
  ArrowLeft,
  ExternalLink,
  Shield,
  Bot,
  Sliders,
  Save,
  Trash2,
  CheckCircle2,
  Clock,
  RefreshCw,
} from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input, Select } from '@/components/ui/Input';
import { Switch } from '@/components/ui/Switch';
import { Badge } from '@/components/ui/Badge';
import {
  useGetRepositoryByIdQuery,
  useUpdateRepositoryMutation,
  useDeleteRepositoryMutation,
  useSyncRepositoryMutation,
} from '@/api/repositoryApi';
import { Repository } from '@/types';

export const RepositoryDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: repository, isLoading, refetch } = useGetRepositoryByIdQuery(id || '');
  const [updateRepository, { isLoading: isSaving }] = useUpdateRepositoryMutation();
  const [deleteRepository, { isLoading: isDeleting }] = useDeleteRepositoryMutation();
  const [syncRepository, { isLoading: isSyncing }] = useSyncRepositoryMutation();

  const [formData, setFormData] = useState({
    autoReviewEnabled: true,
    inlineCommentsEnabled: true,
    reviewMode: 'STANDARD',
    minSeverity: 'MEDIUM',
    aiProvider: 'GEMINI',
    aiModel: 'gemini-2.0-flash',
    maxDiffSizeKb: 500,
    excludedPatterns: '',
    customInstructions: '',
  });

  const [isSavedToast, setIsSavedToast] = useState(false);

  useEffect(() => {
    if (repository) {
      setFormData({
        autoReviewEnabled: repository.autoReviewEnabled,
        inlineCommentsEnabled: repository.inlineCommentsEnabled,
        reviewMode: repository.reviewMode,
        minSeverity: repository.minSeverity,
        aiProvider: repository.aiProvider,
        aiModel: repository.aiModel,
        maxDiffSizeKb: repository.maxDiffSizeKb,
        excludedPatterns: repository.excludedPatterns || '',
        customInstructions: repository.customInstructions || '',
      });
    }
  }, [repository]);

  if (isLoading) {
    return (
      <div className="p-12 text-center text-slate-400">
        <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-3 text-indigo-400" />
        <p className="text-sm">Loading repository configuration...</p>
      </div>
    );
  }

  if (!repository) {
    return (
      <Card className="p-12 text-center max-w-lg mx-auto">
        <h3 className="text-lg font-bold text-white mb-2">Repository Not Found</h3>
        <p className="text-sm text-slate-400 mb-6">
          The requested repository does not exist or has been disconnected.
        </p>
        <NavLink to="/repositories">
          <Button variant="primary">Back to Repositories</Button>
        </NavLink>
      </Card>
    );
  }

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;
    try {
      await updateRepository({ id, data: formData as any }).unwrap();
      setIsSavedToast(true);
      setTimeout(() => setIsSavedToast(false), 3000);
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  const handleSync = async () => {
    if (!id) return;
    try {
      await syncRepository(id).unwrap();
      refetch();
    } catch (err) {
      console.error('Sync error', err);
    }
  };

  const handleDelete = async () => {
    if (confirm('Are you sure you want to disconnect this repository?')) {
      if (id) {
        await deleteRepository(id);
        navigate('/repositories');
      }
    }
  };

  return (
    <div className="space-y-6">
      {/* Top Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <NavLink to="/repositories">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="w-4 h-4" />
            </Button>
          </NavLink>
          <div>
            <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
              {repository.fullName}
              {repository.privateRepo && (
                <span className="text-[10px] font-semibold bg-slate-800 text-slate-400 px-2 py-0.5 rounded border border-slate-700">
                  Private
                </span>
              )}
            </h1>
            <p className="text-xs text-slate-400 font-mono flex items-center gap-2 mt-0.5">
              <span>Default Branch: {repository.defaultBranch}</span>
              <span>•</span>
              <span>Language: {repository.primaryLanguage}</span>
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <a href={repository.htmlUrl} target="_blank" rel="noreferrer">
            <Button variant="outline" size="sm" rightIcon={<ExternalLink className="w-3.5 h-3.5" />}>
              GitHub
            </Button>
          </a>
          <Button
            variant="outline"
            size="sm"
            onClick={handleSync}
            isLoading={isSyncing}
            leftIcon={<RefreshCw className="w-3.5 h-3.5" />}
          >
            Sync PRs
          </Button>
          <Button
            variant="primary"
            size="sm"
            onClick={handleSave}
            isLoading={isSaving}
            leftIcon={<Save className="w-3.5 h-3.5" />}
          >
            Save Settings
          </Button>
        </div>
      </div>

      {isSavedToast && (
        <div className="p-3 bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 rounded-xl text-xs flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4" />
          Repository review settings updated successfully!
        </div>
      )}

      {/* Main Settings Form */}
      <form onSubmit={handleSave} className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column: Automation & AI Engine */}
        <div className="lg:col-span-2 space-y-6">
          <Card className="p-6 space-y-6">
            <CardTitle>Autonomous AI Review Engine</CardTitle>

            <div className="space-y-4">
              <div className="flex items-center justify-between p-4 rounded-xl bg-slate-950/60 border border-slate-800">
                <div>
                  <h4 className="text-sm font-semibold text-white">Enable Automated Reviews</h4>
                  <p className="text-xs text-slate-400">
                    Automatically trigger AI analysis when a pull request is opened or updated
                  </p>
                </div>
                <Switch
                  checked={formData.autoReviewEnabled}
                  onChange={(v) => setFormData({ ...formData, autoReviewEnabled: v })}
                />
              </div>

              <div className="flex items-center justify-between p-4 rounded-xl bg-slate-950/60 border border-slate-800">
                <div>
                  <h4 className="text-sm font-semibold text-white">Publish Inline GitHub Comments</h4>
                  <p className="text-xs text-slate-400">
                    Post AI review findings and suggested fixes directly to GitHub Pull Request
                  </p>
                </div>
                <Switch
                  checked={formData.inlineCommentsEnabled}
                  onChange={(v) => setFormData({ ...formData, inlineCommentsEnabled: v })}
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-2">
              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">
                  Review Depth Mode
                </label>
                <Select
                  value={formData.reviewMode}
                  onChange={(e) => setFormData({ ...formData, reviewMode: e.target.value as any })}
                >
                  <option value="QUICK">Quick Review (Lightweight syntax & smells)</option>
                  <option value="STANDARD">Standard Review (Comprehensive checks)</option>
                  <option value="DEEP">Deep Review (Complete multi-agent AST reasoning)</option>
                  <option value="SECURITY_FOCUSED">Security-Focused (OWASP & threat model)</option>
                  <option value="PERFORMANCE_FOCUSED">Performance-Focused (N+1 queries & memory)</option>
                </Select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">
                  Minimum Reporting Severity
                </label>
                <Select
                  value={formData.minSeverity}
                  onChange={(e) => setFormData({ ...formData, minSeverity: e.target.value as any })}
                >
                  <option value="INFO">INFO & Above (Report everything)</option>
                  <option value="LOW">LOW & Above (Smells, naming, info)</option>
                  <option value="MEDIUM">MEDIUM & Above (Recommended default)</option>
                  <option value="HIGH">HIGH & CRITICAL Only</option>
                  <option value="CRITICAL">CRITICAL Only (Blockers)</option>
                </Select>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">
                  AI Provider
                </label>
                <Select
                  value={formData.aiProvider}
                  onChange={(e) => setFormData({ ...formData, aiProvider: e.target.value as any })}
                >
                  <option value="GEMINI">Google Gemini</option>
                  <option value="OPENAI">OpenAI</option>
                  <option value="OLLAMA">Ollama (Local LLM)</option>
                </Select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-300 mb-1">
                  AI Model Name
                </label>
                <Input
                  value={formData.aiModel}
                  onChange={(e) => setFormData({ ...formData, aiModel: e.target.value })}
                  placeholder="e.g. gemini-2.0-flash, gpt-4o"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">
                Custom Repository Coding Standards & Instructions
              </label>
              <textarea
                rows={4}
                value={formData.customInstructions}
                onChange={(e) => setFormData({ ...formData, customInstructions: e.target.value })}
                placeholder="e.g. Enforce immutability. Require Javadoc on all public methods. Prohibit raw SQL queries."
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-xs text-white focus:outline-none focus:border-indigo-500 font-mono"
              />
              <p className="text-[11px] text-slate-400 mt-1">
                These rules are injected directly into the LLM system prompt for this repository.
              </p>
            </div>
          </Card>
        </div>

        {/* Right Column: Repository Metadata & Danger Zone */}
        <div className="space-y-6">
          <Card className="p-6 space-y-4">
            <CardTitle>Repository Health & Stats</CardTitle>
            <div className="space-y-3 text-xs">
              <div className="flex justify-between py-1 border-b border-slate-800">
                <span className="text-slate-400">Open Pull Requests:</span>
                <span className="text-white font-bold">{repository.openPullRequestCount}</span>
              </div>
              <div className="flex justify-between py-1 border-b border-slate-800">
                <span className="text-slate-400">Total Findings:</span>
                <span className="text-white font-bold">{repository.totalFindingsCount}</span>
              </div>
              <div className="flex justify-between py-1 border-b border-slate-800">
                <span className="text-slate-400">Primary Language:</span>
                <span className="text-white font-bold">{repository.primaryLanguage}</span>
              </div>
              <div className="flex justify-between py-1">
                <span className="text-slate-400">Last Review:</span>
                <span className="text-white font-mono">
                  {repository.lastReviewAt ? new Date(repository.lastReviewAt).toLocaleString() : 'Never'}
                </span>
              </div>
            </div>
          </Card>

          <Card className="p-6 border-rose-500/20 space-y-4">
            <CardTitle className="text-rose-400">Danger Zone</CardTitle>
            <p className="text-xs text-slate-400">
              Disconnecting this repository will remove stored review history, pull request links, and stop automated webhook reviews.
            </p>
            <Button
              type="button"
              variant="outline"
              size="sm"
              isLoading={isDeleting}
              onClick={handleDelete}
              className="w-full border-rose-500/40 text-rose-400 hover:bg-rose-500/10"
              leftIcon={<Trash2 className="w-3.5 h-3.5" />}
            >
              Disconnect Repository
            </Button>
          </Card>
        </div>
      </form>
    </div>
  );
};

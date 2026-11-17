import React, { useState } from 'react';
import { useParams, NavLink, useNavigate } from 'react-router-dom';
import {
  FolderGit2,
  GitPullRequest,
  CheckCircle2,
  Sliders,
  Cpu,
  Trash2,
  ArrowLeft,
  ExternalLink,
  Shield,
  Save,
  Clock,
  Sparkles,
} from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input, Select } from '@/components/ui/Input';
import { ScoreGauge } from '@/components/ui/ScoreGauge';
import {
  useGetRepositoryByIdQuery,
  useUpdateRepositoryMutation,
  useDeleteRepositoryMutation,
} from '@/api/repositoryApi';
import { Repository, ReviewMode, FindingSeverity, AIProvider } from '@/types';

export const RepositoryDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: repository, refetch } = useGetRepositoryByIdQuery(id || '');
  const [updateRepository, { isLoading: isSaving }] = useUpdateRepositoryMutation();
  const [deleteRepository, { isLoading: isDeleting }] = useDeleteRepositoryMutation();

  const repo: Repository = repository || {
    id: id || 'repo-1',
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
    excludedPatterns: '*.min.js, package-lock.json, *.lock, dist/**',
    customInstructions:
      'Enforce Spring Boot best practices. Verify all endpoints authenticate with JWT. Flag any non-parameterized SQL queries.',
    openPullRequestCount: 4,
    totalFindingsCount: 18,
    avgQualityScore: 92,
    createdAt: '2026-01-15T10:00:00Z',
    updatedAt: '2026-11-17T09:30:00Z',
  };

  const [formData, setFormData] = useState({
    autoReviewEnabled: repo.autoReviewEnabled,
    inlineCommentsEnabled: repo.inlineCommentsEnabled,
    reviewMode: repo.reviewMode,
    minSeverity: repo.minSeverity,
    aiProvider: repo.aiProvider,
    aiModel: repo.aiModel,
    maxDiffSizeKb: repo.maxDiffSizeKb,
    excludedPatterns: repo.excludedPatterns || '',
    customInstructions: repo.customInstructions || '',
  });

  const [isSavedToast, setIsSavedToast] = useState(false);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;
    try {
      await updateRepository({ id, data: formData }).unwrap();
      setIsSavedToast(true);
      setTimeout(() => setIsSavedToast(false), 3000);
      refetch();
    } catch (err) {
      console.error(err);
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
              {repo.fullName}
              {repo.privateRepo && (
                <span className="text-[10px] font-semibold bg-slate-800 text-slate-400 px-2 py-0.5 rounded border border-slate-700">
                  Private
                </span>
              )}
            </h1>
            <p className="text-xs text-slate-400 font-mono flex items-center gap-2 mt-0.5">
              <span>Default Branch: {repo.defaultBranch}</span>
              <span>•</span>
              <a
                href={repo.htmlUrl}
                target="_blank"
                rel="noreferrer"
                className="text-indigo-400 hover:underline inline-flex items-center gap-1"
              >
                GitHub <ExternalLink className="w-3 h-3" />
              </a>
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <NavLink to={`/pull-requests?repoId=${repo.id}`}>
            <Button
              variant="outline"
              size="sm"
              leftIcon={<GitPullRequest className="w-4 h-4" />}
            >
              View Pull Requests ({repo.openPullRequestCount})
            </Button>
          </NavLink>
          <Button
            variant="danger"
            size="sm"
            onClick={handleDelete}
            isLoading={isDeleting}
            leftIcon={<Trash2 className="w-4 h-4" />}
          >
            Disconnect
          </Button>
        </div>
      </div>

      {isSavedToast && (
        <div className="p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-semibold flex items-center gap-2 animate-fade-in">
          <CheckCircle2 className="w-4 h-4" /> Repository settings updated successfully!
        </div>
      )}

      {/* Main Settings Form */}
      <form onSubmit={handleSave} className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          {/* General Review Configuration */}
          <Card className="p-6 space-y-5">
            <CardTitle>
              <Sliders className="w-5 h-5 text-indigo-400" /> Review Engine Settings
            </CardTitle>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  Review Depth Mode
                </label>
                <Select
                  value={formData.reviewMode}
                  onChange={(e) =>
                    setFormData({ ...formData, reviewMode: e.target.value as ReviewMode })
                  }
                >
                  <option value="QUICK">Quick Review (Fastest, summary + high risks)</option>
                  <option value="STANDARD">Standard Review (Balanced AST & LLM check)</option>
                  <option value="DEEP">Deep Review (Complete 6-pillar exhaustive audit)</option>
                  <option value="SECURITY_FOCUSED">Security Focused (OWASP & CVE priority)</option>
                  <option value="PERFORMANCE_FOCUSED">Performance Focused (Algorithmic & N+1)</option>
                  <option value="ARCHITECTURE">Architecture Review (SOLID & Boundaries)</option>
                  <option value="TESTING">Testing Review (Unit test completeness)</option>
                </Select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  Minimum Severity Threshold
                </label>
                <Select
                  value={formData.minSeverity}
                  onChange={(e) =>
                    setFormData({ ...formData, minSeverity: e.target.value as FindingSeverity })
                  }
                >
                  <option value="INFO">INFO (Report everything)</option>
                  <option value="LOW">LOW and higher</option>
                  <option value="MEDIUM">MEDIUM and higher (Recommended)</option>
                  <option value="HIGH">HIGH and higher</option>
                  <option value="CRITICAL">CRITICAL only</option>
                </Select>
              </div>
            </div>

            {/* Toggles */}
            <div className="space-y-3 pt-2">
              <label className="flex items-center justify-between p-3.5 rounded-xl bg-slate-950/60 border border-slate-800 cursor-pointer">
                <div>
                  <span className="text-sm font-semibold text-white block">
                    Automatic Webhook Reviews
                  </span>
                  <span className="text-xs text-slate-400">
                    Automatically trigger review jobs when PRs are opened or synchronized
                  </span>
                </div>
                <input
                  type="checkbox"
                  checked={formData.autoReviewEnabled}
                  onChange={(e) =>
                    setFormData({ ...formData, autoReviewEnabled: e.target.checked })
                  }
                  className="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500 bg-slate-900 border-slate-700"
                />
              </label>

              <label className="flex items-center justify-between p-3.5 rounded-xl bg-slate-950/60 border border-slate-800 cursor-pointer">
                <div>
                  <span className="text-sm font-semibold text-white block">
                    Publish Inline GitHub Comments
                  </span>
                  <span className="text-xs text-slate-400">
                    Post AI finding annotations directly on specific changed lines in the PR
                  </span>
                </div>
                <input
                  type="checkbox"
                  checked={formData.inlineCommentsEnabled}
                  onChange={(e) =>
                    setFormData({ ...formData, inlineCommentsEnabled: e.target.checked })
                  }
                  className="w-4 h-4 rounded text-indigo-600 focus:ring-indigo-500 bg-slate-900 border-slate-700"
                />
              </label>
            </div>
          </Card>

          {/* AI Model & Custom Prompt Rules */}
          <Card className="p-6 space-y-5">
            <CardTitle>
              <Cpu className="w-5 h-5 text-purple-400" /> AI Provider & Repository Rules
            </CardTitle>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  AI Provider
                </label>
                <Select
                  value={formData.aiProvider}
                  onChange={(e) =>
                    setFormData({ ...formData, aiProvider: e.target.value as AIProvider })
                  }
                >
                  <option value="GEMINI">Google Gemini</option>
                  <option value="OPENAI">OpenAI</option>
                  <option value="OLLAMA">Ollama (Local / Self-hosted)</option>
                </Select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  Model Identifier
                </label>
                <Input
                  value={formData.aiModel}
                  onChange={(e) => setFormData({ ...formData, aiModel: e.target.value })}
                  placeholder="e.g. gemini-2.0-flash or gpt-4o"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Excluded File Glob Patterns
              </label>
              <Input
                value={formData.excludedPatterns}
                onChange={(e) => setFormData({ ...formData, excludedPatterns: e.target.value })}
                placeholder="*.min.js, *.lock, package-lock.json, dist/**, target/**"
              />
              <p className="text-[11px] text-slate-400 mt-1">
                Comma-separated file patterns to skip during AI review
              </p>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Custom Repository Coding Guidelines & Prompt Instructions
              </label>
              <textarea
                rows={4}
                value={formData.customInstructions}
                onChange={(e) => setFormData({ ...formData, customInstructions: e.target.value })}
                className="w-full rounded-xl bg-slate-950/80 border border-slate-700/80 p-3 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
                placeholder="e.g. We require immutability in all entity models. Do not allow raw SQL queries. Enforce strict null-safety."
              />
            </div>

            <div className="flex items-center justify-end pt-3 border-t border-slate-800">
              <Button
                type="submit"
                variant="primary"
                isLoading={isSaving}
                leftIcon={<Save className="w-4 h-4" />}
              >
                Save Changes
              </Button>
            </div>
          </Card>
        </div>

        {/* Right Column: Health Score Card */}
        <div className="space-y-6">
          <Card className="p-6 text-center space-y-4">
            <CardTitle className="justify-center">Repository Quality Score</CardTitle>
            <div className="py-4">
              <ScoreGauge score={repo.avgQualityScore || 85} size="lg" label="Overall Code Health" />
            </div>
            <div className="text-xs text-slate-400 space-y-2 pt-2 border-t border-slate-800">
              <div className="flex justify-between">
                <span>Active Findings:</span>
                <span className="font-mono text-white font-bold">{repo.totalFindingsCount}</span>
              </div>
              <div className="flex justify-between">
                <span>Open PRs:</span>
                <span className="font-mono text-white font-bold">{repo.openPullRequestCount}</span>
              </div>
              <div className="flex justify-between">
                <span>Primary Language:</span>
                <span className="text-indigo-400 font-semibold">{repo.primaryLanguage}</span>
              </div>
            </div>
          </Card>
        </div>
      </form>
    </div>
  );
};

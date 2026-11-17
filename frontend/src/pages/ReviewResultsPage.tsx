import React from 'react';
import { useParams, NavLink } from 'react-router-dom';
import {
  Sparkles,
  ArrowLeft,
  CheckCircle2,
  Clock,
  Layers,
  Bug,
  ShieldAlert,
  Zap,
} from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { ScoreGauge } from '@/components/ui/ScoreGauge';
import { useGetReviewByIdQuery } from '@/api/repositoryApi';

export const ReviewResultsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { data: reviewData } = useGetReviewByIdQuery(id || '');

  const review = reviewData || {
    id: id || 'rev-1',
    pullRequestId: 'pr-1',
    jobStatus: 'COMPLETED' as const,
    summary:
      'Autonomous analysis completed across 4 changed files and 390 line additions/deletions. Identified 1 potential high-severity race condition and 2 performance improvements.',
    qualityScore: 89,
    securityScore: 84,
    performanceScore: 92,
    architectureScore: 88,
    testScore: 75,
    findings: [],
    durationMs: 14200,
    tokenCount: 4120,
    aiProvider: 'GEMINI' as const,
    aiModel: 'gemini-2.0-flash',
    createdAt: '2026-11-17T08:45:00Z',
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <NavLink to="/pull-requests">
          <Button variant="ghost" size="icon">
            <ArrowLeft className="w-4 h-4" />
          </Button>
        </NavLink>
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            AI Review Report <Sparkles className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs text-slate-400 font-mono mt-0.5">
            Review ID: {review.id} • Processed by {review.aiProvider} ({review.aiModel}) in{' '}
            {(review.durationMs / 1000).toFixed(1)}s
          </p>
        </div>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-5 gap-4">
        <Card className="p-4">
          <ScoreGauge score={review.qualityScore} size="md" label="Code Quality" />
        </Card>
        <Card className="p-4">
          <ScoreGauge score={review.securityScore} size="md" label="Security" />
        </Card>
        <Card className="p-4">
          <ScoreGauge score={review.performanceScore} size="md" label="Performance" />
        </Card>
        <Card className="p-4">
          <ScoreGauge score={review.architectureScore} size="md" label="Architecture" />
        </Card>
        <Card className="p-4">
          <ScoreGauge score={review.testScore} size="md" label="Tests" />
        </Card>
      </div>

      <Card className="p-6 space-y-3">
        <CardTitle>Summary & Remediation</CardTitle>
        <p className="text-sm text-slate-300 leading-relaxed">{review.summary}</p>
        <div className="pt-2">
          <NavLink to={`/pull-requests/${review.pullRequestId}`}>
            <Button variant="primary" size="sm">
              Open Full Interactive PR Review
            </Button>
          </NavLink>
        </div>
      </Card>
    </div>
  );
};

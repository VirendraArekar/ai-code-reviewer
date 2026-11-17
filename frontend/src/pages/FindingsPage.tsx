import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import {
  AlertOctagon,
  Search,
  Filter,
  CheckCircle2,
  XCircle,
  Clock,
  HelpCircle,
  Copy,
  ChevronDown,
  ChevronUp,
  FolderGit2,
  GitPullRequest,
} from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { Input, Select } from '@/components/ui/Input';
import { useGetFindingsQuery, useUpdateFindingStatusMutation } from '@/api/repositoryApi';
import { ReviewFinding, FindingSeverity, FindingCategory, FindingStatus } from '@/types';

export const FindingsPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [severityFilter, setSeverityFilter] = useState<string>('ALL');
  const [categoryFilter, setCategoryFilter] = useState<string>('ALL');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [expandedId, setExpandedId] = useState<string | null>('f-1');

  const { data: findings, refetch } = useGetFindingsQuery({
    severity: severityFilter === 'ALL' ? undefined : severityFilter,
    category: categoryFilter === 'ALL' ? undefined : categoryFilter,
    status: statusFilter === 'ALL' ? undefined : statusFilter,
    search: searchQuery,
  });

  const [updateFindingStatus] = useUpdateFindingStatusMutation();

  const fallbackFindings: ReviewFinding[] = [
    {
      id: 'f-1',
      reviewId: 'rev-1',
      repositoryId: 'repo-1',
      pullRequestId: 'pr-1',
      filePath: 'src/main/java/com/aicode/security/RefreshTokenService.java',
      lineNumber: 48,
      title: 'Potential Race Condition in Token Rotation Validation',
      description: 'Concurrent requests with the same refresh token may bypass the revocation check due to missing lock.',
      severity: 'HIGH',
      category: 'SECURITY',
      technicalExplanation: 'Parallel HTTP requests can read revoked = false before the transaction updates and commits.',
      whyItMatters: 'Allows session hijacking.',
      potentialImpact: 'Authentication compromise.',
      recommendedSolution: 'Apply pessimistic write lock or unique constraint.',
      originalCodeSnippet: 'Token token = tokenRepo.findByToken(rawToken);',
      correctedCodeSnippet: 'Token token = tokenRepo.findByTokenWithLock(rawToken);',
      confidenceScore: 0.94,
      status: 'OPEN',
      commentsCount: 2,
      createdAt: '2026-11-17T08:45:00Z',
      updatedAt: '2026-11-17T08:45:00Z',
    },
    {
      id: 'f-2',
      reviewId: 'rev-2',
      repositoryId: 'repo-1',
      pullRequestId: 'pr-2',
      filePath: 'src/main/java/com/aicode/controller/UserController.java',
      lineNumber: 112,
      title: 'Unnecessary N+1 Query in User Roles Serializer',
      description: 'Looping over collection invokes separate SELECT query per item.',
      severity: 'MEDIUM',
      category: 'PERFORMANCE',
      technicalExplanation: 'Calling `user.getRoles()` inside the loop triggers lazy proxy initialization.',
      whyItMatters: 'Slows endpoint response time significantly under high load.',
      potentialImpact: 'High database latency.',
      recommendedSolution: 'Use `@EntityGraph(attributePaths = {"roles"})` or JOIN FETCH in repository.',
      correctedCodeSnippet: '@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id IN :ids")\nList<User> findAllWithRoles(@Param("ids") List<Long> ids);',
      confidenceScore: 0.91,
      status: 'RESOLVED',
      commentsCount: 1,
      createdAt: '2026-11-16T14:30:00Z',
      updatedAt: '2026-11-16T16:00:00Z',
    },
    {
      id: 'f-3',
      reviewId: 'rev-3',
      repositoryId: 'repo-2',
      pullRequestId: 'pr-3',
      filePath: 'src/main/java/com/aicode/service/WebhookService.java',
      lineNumber: 64,
      title: 'Insecure String Comparison on Webhook Signature (Timing Attack)',
      description: 'Using `String.equals()` leaks timing information when validating cryptographic HMAC signature.',
      severity: 'CRITICAL',
      category: 'SECURITY',
      technicalExplanation: 'Standard string equality returns early upon the first mismatched byte.',
      whyItMatters: 'Attacker can deduce valid signature byte-by-byte via timing differences.',
      potentialImpact: 'Forging webhook events from unauthorized sources.',
      recommendedSolution: 'Use `MessageDigest.isEqual(expectedBytes, actualBytes)` which executes in constant time.',
      originalCodeSnippet: 'if (expectedSignature.equals(actualSignature))',
      correctedCodeSnippet: 'if (MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), actualSignature.getBytes(StandardCharsets.UTF_8)))',
      confidenceScore: 0.99,
      status: 'OPEN',
      commentsCount: 3,
      createdAt: '2026-11-15T12:00:00Z',
      updatedAt: '2026-11-15T12:00:00Z',
    },
  ];

  const items = findings && findings.length > 0 ? findings : fallbackFindings;

  const handleStatusChange = async (findingId: string, status: FindingStatus) => {
    try {
      await updateFindingStatus({ findingId, status }).unwrap();
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          Findings & Vulnerabilities <AlertOctagon className="w-5 h-5 text-rose-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Centralized management for all AI-flagged code quality, security, and performance findings
        </p>
      </div>

      {/* Filter Toolbar */}
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-3">
        <div className="sm:col-span-1">
          <Input
            placeholder="Search findings..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            leftIcon={<Search className="w-4 h-4 text-slate-400" />}
          />
        </div>

        <div>
          <Select
            value={severityFilter}
            onChange={(e) => setSeverityFilter(e.target.value)}
          >
            <option value="ALL">All Severities</option>
            <option value="CRITICAL">Critical Only</option>
            <option value="HIGH">High</option>
            <option value="MEDIUM">Medium</option>
            <option value="LOW">Low</option>
            <option value="INFO">Info</option>
          </Select>
        </div>

        <div>
          <Select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
          >
            <option value="ALL">All Categories</option>
            <option value="SECURITY">Security</option>
            <option value="BUG">Bug</option>
            <option value="PERFORMANCE">Performance</option>
            <option value="CODE_QUALITY">Code Quality</option>
            <option value="ARCHITECTURE">Architecture</option>
            <option value="TESTING">Testing</option>
          </Select>
        </div>

        <div>
          <Select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="ALL">All Statuses</option>
            <option value="OPEN">Open</option>
            <option value="RESOLVED">Resolved</option>
            <option value="FALSE_POSITIVE">False Positive</option>
            <option value="IGNORED">Ignored</option>
          </Select>
        </div>
      </div>

      {/* Findings List */}
      <div className="space-y-4">
        {items.map((item) => {
          const isExpanded = expandedId === item.id;
          return (
            <Card
              key={item.id}
              className={`overflow-hidden transition-all border ${
                item.severity === 'CRITICAL' || item.severity === 'HIGH'
                  ? 'border-rose-500/30'
                  : 'border-slate-800'
              }`}
            >
              <div
                onClick={() => setExpandedId(isExpanded ? null : item.id)}
                className="p-5 flex items-center justify-between cursor-pointer hover:bg-slate-800/40"
              >
                <div className="flex items-center gap-3">
                  <Badge severity={item.severity} />
                  <Badge category={item.category} />
                  <div>
                    <h4 className="text-sm font-semibold text-white">{item.title}</h4>
                    <p className="text-xs text-slate-400 font-mono mt-0.5">
                      {item.filePath} • Line {item.lineNumber}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <Badge status={item.status} />
                  {isExpanded ? (
                    <ChevronUp className="w-4 h-4 text-slate-400" />
                  ) : (
                    <ChevronDown className="w-4 h-4 text-slate-400" />
                  )}
                </div>
              </div>

              {isExpanded && (
                <div className="p-5 pt-0 space-y-4 border-t border-slate-800/80 bg-slate-950/60 text-xs">
                  <div>
                    <h5 className="font-semibold text-slate-300 text-xs uppercase tracking-wider mb-1">
                      Technical Explanation
                    </h5>
                    <p className="text-slate-400 leading-relaxed">{item.technicalExplanation}</p>
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                    <div className="p-3 rounded-xl bg-slate-900 border border-slate-800">
                      <span className="font-bold text-amber-400 block mb-1">Why It Matters:</span>
                      <p className="text-slate-400">{item.whyItMatters}</p>
                    </div>
                    <div className="p-3 rounded-xl bg-slate-900 border border-slate-800">
                      <span className="font-bold text-rose-400 block mb-1">Potential Impact:</span>
                      <p className="text-slate-400">{item.potentialImpact}</p>
                    </div>
                  </div>

                  {item.correctedCodeSnippet && (
                    <div>
                      <div className="flex items-center justify-between mb-1">
                        <span className="font-semibold text-emerald-400">
                          Recommended Solution & Code Fix:
                        </span>
                        <button
                          onClick={() =>
                            navigator.clipboard.writeText(item.correctedCodeSnippet || '')
                          }
                          className="text-slate-400 hover:text-white flex items-center gap-1"
                        >
                          <Copy className="w-3 h-3" /> Copy fix
                        </button>
                      </div>
                      <pre className="p-3 rounded-xl bg-slate-950 border border-emerald-500/30 text-emerald-300 font-mono overflow-x-auto">
                        {item.correctedCodeSnippet}
                      </pre>
                    </div>
                  )}

                  <div className="pt-3 border-t border-slate-800 flex items-center justify-between">
                    <NavLink
                      to={`/pull-requests/${item.pullRequestId}`}
                      className="text-indigo-400 hover:underline flex items-center gap-1"
                    >
                      <GitPullRequest className="w-3.5 h-3.5" /> View related Pull Request
                    </NavLink>

                    <div className="flex items-center gap-2">
                      <Button
                        variant={item.status === 'RESOLVED' ? 'primary' : 'outline'}
                        size="sm"
                        onClick={() => handleStatusChange(item.id, 'RESOLVED')}
                      >
                        Mark as Resolved
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleStatusChange(item.id, 'FALSE_POSITIVE')}
                      >
                        False Positive
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleStatusChange(item.id, 'IGNORED')}
                      >
                        Ignore
                      </Button>
                    </div>
                  </div>
                </div>
              )}
            </Card>
          );
        })}
      </div>
    </div>
  );
};

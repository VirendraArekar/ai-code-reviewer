export type Role =
  | 'SUPER_ADMIN'
  | 'ADMIN'
  | 'ORGANIZATION_OWNER'
  | 'TEAM_LEAD'
  | 'DEVELOPER'
  | 'VIEWER';

export type FindingSeverity = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO';

export type FindingCategory =
  | 'BUG'
  | 'SECURITY'
  | 'PERFORMANCE'
  | 'CODE_QUALITY'
  | 'ARCHITECTURE'
  | 'TESTING';

export type FindingStatus = 'OPEN' | 'RESOLVED' | 'IGNORED' | 'FALSE_POSITIVE';

export type ReviewJobStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';

export type ReviewMode =
  | 'QUICK'
  | 'STANDARD'
  | 'DEEP'
  | 'SECURITY_FOCUSED'
  | 'PERFORMANCE_FOCUSED'
  | 'ARCHITECTURE'
  | 'TESTING';

export type AIProvider = 'OPENAI' | 'GEMINI' | 'OLLAMA';

export interface User {
  id: string;
  email: string;
  fullName: string;
  avatarUrl?: string;
  role: Role;
  organizationId?: string;
  organizationName?: string;
  emailVerified: boolean;
  githubUsername?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  user: User;
}

export interface Organization {
  id: string;
  name: string;
  slug: string;
  plan: 'FREE' | 'PRO' | 'TEAM' | 'ENTERPRISE';
  ownerId: string;
  memberCount: number;
  repositoryCount: number;
  createdAt: string;
}

export interface OrganizationMember {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  avatarUrl?: string;
  role: Role;
  joinedAt: string;
}

export interface Team {
  id: string;
  organizationId: string;
  name: string;
  description?: string;
  memberCount: number;
  repositoryCount: number;
  createdAt: string;
}

export interface Repository {
  id: string;
  organizationId: string;
  githubRepoId: number;
  name: string;
  fullName: string;
  ownerLogin: string;
  defaultBranch: string;
  privateRepo: boolean;
  htmlUrl: string;
  primaryLanguage?: string;
  autoReviewEnabled: boolean;
  inlineCommentsEnabled: boolean;
  reviewMode: ReviewMode;
  minSeverity: FindingSeverity;
  aiProvider: AIProvider;
  aiModel: string;
  maxDiffSizeKb: number;
  excludedPatterns?: string;
  customInstructions?: string;
  lastReviewAt?: string;
  openPullRequestCount: number;
  totalFindingsCount: number;
  avgQualityScore?: number;
  createdAt: string;
  updatedAt: string;
}

export interface PullRequest {
  id: string;
  repositoryId: string;
  repositoryName: string;
  githubPrNumber: number;
  title: string;
  description?: string;
  authorUsername: string;
  authorAvatarUrl?: string;
  state: 'OPEN' | 'CLOSED' | 'MERGED';
  sourceBranch: string;
  targetBranch: string;
  htmlUrl: string;
  additions: number;
  deletions: number;
  changedFilesCount: number;
  latestReviewId?: string;
  latestReviewStatus?: ReviewJobStatus;
  qualityScore?: number;
  securityScore?: number;
  criticalCount: number;
  highCount: number;
  mediumCount: number;
  lowCount: number;
  infoCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface PullRequestFile {
  id: string;
  pullRequestId: string;
  filename: string;
  oldPath?: string;
  status: 'ADDED' | 'MODIFIED' | 'REMOVED' | 'RENAMED';
  additions: number;
  deletions: number;
  patch?: string;
  findingsCount: number;
}

export interface ReviewFinding {
  id: string;
  reviewId: string;
  repositoryId: string;
  pullRequestId: string;
  filePath: string;
  lineNumber: number;
  title: string;
  description: string;
  severity: FindingSeverity;
  category: FindingCategory;
  technicalExplanation: string;
  whyItMatters: string;
  potentialImpact: string;
  recommendedSolution: string;
  correctedCodeSnippet?: string;
  originalCodeSnippet?: string;
  confidenceScore: number;
  status: FindingStatus;
  assignedToUserId?: string;
  assignedToName?: string;
  commentsCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface ReviewFindingComment {
  id: string;
  findingId: string;
  userId: string;
  authorName: string;
  authorAvatarUrl?: string;
  comment: string;
  createdAt: string;
}

export interface AIReviewDetail {
  id: string;
  pullRequestId: string;
  jobStatus: ReviewJobStatus;
  summary: string;
  qualityScore: number;
  securityScore: number;
  performanceScore: number;
  architectureScore: number;
  testScore: number;
  findings: ReviewFinding[];
  durationMs: number;
  tokenCount: number;
  aiProvider: AIProvider;
  aiModel: string;
  createdAt: string;
}

export interface DashboardStats {
  totalRepositories: number;
  totalPullRequests: number;
  totalReviews: number;
  totalIssuesDetected: number;
  criticalVulnerabilities: number;
  avgQualityScore: number;
  avgReviewDurationSeconds: number;
  issuesBySeverity: { severity: FindingSeverity; count: number }[];
  issuesByCategory: { category: FindingCategory; count: number }[];
  reviewsTrend: { date: string; reviewsCount: number; issuesCount: number }[];
  qualityByRepository: { repoName: string; qualityScore: number; issueCount: number }[];
}

export interface NotificationItem {
  id: string;
  userId: string;
  title: string;
  message: string;
  type: 'REVIEW_COMPLETED' | 'CRITICAL_VULNERABILITY' | 'REVIEW_FAILED' | 'TEAM_INVITATION' | 'FINDING_ASSIGNED';
  read: boolean;
  actionUrl?: string;
  createdAt: string;
}

export interface AIProviderConfig {
  id: string;
  provider: AIProvider;
  modelName: string;
  hasApiKey: boolean;
  baseUrl?: string;
  isDefault: boolean;
  enabled: boolean;
}

export interface AuditLogItem {
  id: string;
  userId: string;
  userEmail: string;
  action: string;
  entityType: string;
  entityId?: string;
  ipAddress: string;
  details?: string;
  createdAt: string;
}

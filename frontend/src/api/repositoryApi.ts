import { api } from './baseApi';
import { Repository, PullRequest, PullRequestFile, ReviewFinding, AIReviewDetail, DashboardStats } from '@/types';

export const repositoryApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getRepositories: builder.query<Repository[], { search?: string }>({
      query: (params) => ({
        url: '/repositories',
        params,
      }),
      providesTags: ['Repository'],
    }),
    getRepositoryById: builder.query<Repository, string>({
      query: (id) => `/repositories/${id}`,
      providesTags: (result, error, id) => [{ type: 'Repository', id }],
    }),
    createRepository: builder.mutation<Repository, Partial<Repository>>({
      query: (body) => ({
        url: '/repositories',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Repository'],
    }),
    updateRepository: builder.mutation<Repository, { id: string; data: Partial<Repository> }>({
      query: ({ id, data }) => ({
        url: `/repositories/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: 'Repository', id }, 'Repository'],
    }),
    deleteRepository: builder.mutation<void, string>({
      query: (id) => ({
        url: `/repositories/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['Repository'],
    }),
    syncGithubRepositories: builder.mutation<Repository[], void>({
      query: () => ({
        url: '/repositories/sync',
        method: 'POST',
      }),
      invalidatesTags: ['Repository'],
    }),
    syncRepository: builder.mutation<{ status: string; count: number }, string>({
      query: (id) => ({
        url: `/repositories/${id}/sync`,
        method: 'POST',
      }),
      invalidatesTags: ['Repository', 'PullRequest', 'Review', 'Finding'],
    }),
  }),
});

export const pullRequestApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getPullRequests: builder.query<PullRequest[], { repoId?: string; state?: string; search?: string }>({
      query: (params) => ({
        url: '/pull-requests',
        params,
      }),
      providesTags: ['PullRequest'],
    }),
    getPullRequestById: builder.query<PullRequest, string>({
      query: (id) => `/pull-requests/${id}`,
      providesTags: (result, error, id) => [{ type: 'PullRequest', id }],
    }),
    getPullRequestFiles: builder.query<PullRequestFile[], string>({
      query: (prId) => `/pull-requests/${prId}/files`,
    }),
    syncPullRequest: builder.mutation<{ status: string }, string>({
      query: (id) => ({
        url: `/pull-requests/${id}/sync`,
        method: 'POST',
      }),
      invalidatesTags: ['PullRequest', 'Review', 'Finding'],
    }),
    triggerReview: builder.mutation<AIReviewDetail, { prId: string; mode?: string }>({
      query: ({ prId, mode }) => ({
        url: `/pull-requests/${prId}/review`,
        method: 'POST',
        body: { mode },
      }),
      invalidatesTags: ['PullRequest', 'Review', 'Finding'],
    }),
    chatWithPr: builder.mutation<{ response: string }, { prId: string; message: string }>({
      query: ({ prId, message }) => ({
        url: `/pull-requests/${prId}/chat`,
        method: 'POST',
        body: { message },
      }),
    }),
  }),
});

export const reviewApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getReviewById: builder.query<AIReviewDetail, string>({
      query: (id) => `/reviews/${id}`,
      providesTags: (result, error, id) => [{ type: 'Review', id }],
    }),
    getFindingsByReview: builder.query<ReviewFinding[], string>({
      query: (reviewId) => `/reviews/${reviewId}/findings`,
      providesTags: ['Finding'],
    }),
  }),
});

export const findingsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getFindings: builder.query<
      ReviewFinding[],
      { severity?: string; category?: string; status?: string; search?: string }
    >({
      query: (params) => ({
        url: '/findings',
        params,
      }),
      providesTags: ['Finding'],
    }),
    updateFindingStatus: builder.mutation<
      ReviewFinding,
      { findingId: string; status: string; comment?: string }
    >({
      query: ({ findingId, status, comment }) => ({
        url: `/findings/${findingId}/status`,
        method: 'PATCH',
        body: { status, comment },
      }),
      invalidatesTags: ['Finding'],
    }),
    assignFinding: builder.mutation<ReviewFinding, { findingId: string; userId: string }>({
      query: ({ findingId, userId }) => ({
        url: `/findings/${findingId}/assign`,
        method: 'POST',
        body: { userId },
      }),
      invalidatesTags: ['Finding'],
    }),
  }),
});

export const analyticsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getDashboardStats: builder.query<DashboardStats, void>({
      query: () => '/analytics/dashboard',
      providesTags: ['Analytics'],
    }),
  }),
});

export const {
  useGetRepositoriesQuery,
  useGetRepositoryByIdQuery,
  useCreateRepositoryMutation,
  useUpdateRepositoryMutation,
  useDeleteRepositoryMutation,
  useSyncGithubRepositoriesMutation,
  useSyncRepositoryMutation,
} = repositoryApi;

export const {
  useGetPullRequestsQuery,
  useGetPullRequestByIdQuery,
  useGetPullRequestFilesQuery,
  useSyncPullRequestMutation,
  useTriggerReviewMutation,
  useChatWithPrMutation,
} = pullRequestApi;

export const { useGetReviewByIdQuery, useGetFindingsByReviewQuery } = reviewApi;

export const {
  useGetFindingsQuery,
  useUpdateFindingStatusMutation,
  useAssignFindingMutation,
} = findingsApi;

export const { useGetDashboardStatsQuery } = analyticsApi;

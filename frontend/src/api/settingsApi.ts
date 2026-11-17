import { api } from './baseApi';
import { AIProviderConfig, Team, NotificationItem, AuditLogItem } from '@/types';

export const settingsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getAiProviders: builder.query<AIProviderConfig[], void>({
      query: () => '/settings/ai-providers',
      providesTags: ['AIProvider'],
    }),
    updateAiProvider: builder.mutation<
      AIProviderConfig,
      { provider: string; apiKey?: string; modelName: string; baseUrl?: string; isDefault: boolean; enabled: boolean }
    >({
      query: (body) => ({
        url: '/settings/ai-providers',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['AIProvider'],
    }),
    testAiProvider: builder.mutation<{ success: boolean; latencyMs: number; message: string }, { provider: string }>({
      query: (body) => ({
        url: '/settings/ai-providers/test',
        method: 'POST',
        body,
      }),
    }),
    getGithubAppConfig: builder.query<
      {
        appId?: string;
        appName?: string;
        clientId?: string;
        hasClientSecret?: boolean;
        hasToken?: boolean;
        webhookSecretConfigured?: boolean;
        installUrl?: string;
        webhookUrl?: string;
        isConfigured?: boolean;
      },
      void
    >({
      query: () => '/settings/github-app',
      providesTags: ['GithubApp'],
    }),
    convertGithubAppCode: builder.mutation<
      {
        appId?: string;
        appName?: string;
        clientId?: string;
        clientSecret?: string;
        webhookSecret?: string;
        pem?: string;
        htmlUrl?: string;
      },
      { code: string }
    >({
      query: (body) => ({
        url: '/settings/github-app/convert-code',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['GithubApp'],
    }),
    getTeams: builder.query<Team[], void>({
      query: () => '/teams',
      providesTags: ['Team'],
    }),
    createTeam: builder.mutation<Team, { name: string; description?: string }>({
      query: (body) => ({
        url: '/teams',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Team'],
    }),
    getNotifications: builder.query<NotificationItem[], void>({
      query: () => '/notifications',
      providesTags: ['Notification'],
    }),
    markNotificationAsRead: builder.mutation<void, string>({
      query: (id) => ({
        url: `/notifications/${id}/read`,
        method: 'PATCH',
      }),
      invalidatesTags: ['Notification'],
    }),
    getAuditLogs: builder.query<AuditLogItem[], { search?: string }>({
      query: (params) => ({
        url: '/admin/audit-logs',
        params,
      }),
      providesTags: ['AuditLog'],
    }),
  }),
});

export const {
  useGetAiProvidersQuery,
  useUpdateAiProviderMutation,
  useTestAiProviderMutation,
  useGetGithubAppConfigQuery,
  useConvertGithubAppCodeMutation,
  useGetTeamsQuery,
  useCreateTeamMutation,
  useGetNotificationsQuery,
  useMarkNotificationAsReadMutation,
  useGetAuditLogsQuery,
} = settingsApi;

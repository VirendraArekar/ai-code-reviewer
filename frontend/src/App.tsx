import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { MainLayout } from '@/layouts/MainLayout';
import { AuthLayout } from '@/layouts/AuthLayout';
import { ProtectedRoute } from '@/components/auth/ProtectedRoute';

// Public Pages
import { LandingPage } from '@/pages/LandingPage';
import { LoginPage } from '@/pages/auth/LoginPage';
import { RegisterPage } from '@/pages/auth/RegisterPage';
import { ForgotPasswordPage } from '@/pages/auth/ForgotPasswordPage';
import { ResetPasswordPage } from '@/pages/auth/ResetPasswordPage';

// Onboarding
import { OnboardingPage } from '@/pages/OnboardingPage';

// Protected Core Pages
import { DashboardPage } from '@/pages/DashboardPage';
import { RepositoriesPage } from '@/pages/RepositoriesPage';
import { RepositoryDetailPage } from '@/pages/RepositoryDetailPage';
import { PullRequestsPage } from '@/pages/PullRequestsPage';
import { PullRequestDetailPage } from '@/pages/PullRequestDetailPage';
import { ReviewResultsPage } from '@/pages/ReviewResultsPage';
import { FindingsPage } from '@/pages/FindingsPage';
import { AnalyticsPage } from '@/pages/AnalyticsPage';
import { TeamsPage } from '@/pages/TeamsPage';
import { TeamDetailPage } from '@/pages/TeamDetailPage';
import { NotificationsPage } from '@/pages/NotificationsPage';

// Settings Pages
import { AiProvidersPage } from '@/pages/settings/AiProvidersPage';
import { GithubIntegrationPage } from '@/pages/settings/GithubIntegrationPage';
import { ProfilePage } from '@/pages/settings/ProfilePage';
import { OrganizationPage } from '@/pages/settings/OrganizationPage';
import { BillingPage } from '@/pages/settings/BillingPage';

// Admin Pages
import { AdminDashboardPage } from '@/pages/admin/AdminDashboardPage';
import { AuditLogsPage } from '@/pages/admin/AuditLogsPage';

// Error Pages
import { ForbiddenPage } from '@/pages/error/ForbiddenPage';
import { NotFoundPage } from '@/pages/error/NotFoundPage';
import { ServerErrorPage } from '@/pages/error/ServerErrorPage';

export const App: React.FC = () => {
  return (
    <Routes>
      {/* Public Landing */}
      <Route path="/" element={<LandingPage />} />

      {/* Auth Routes */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
      </Route>

      {/* Onboarding Flow */}
      <Route
        path="/onboarding"
        element={
          <ProtectedRoute>
            <OnboardingPage />
          </ProtectedRoute>
        }
      />

      {/* Protected SaaS App Routes */}
      <Route
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/repositories" element={<RepositoriesPage />} />
        <Route path="/repositories/:id" element={<RepositoryDetailPage />} />
        <Route path="/pull-requests" element={<PullRequestsPage />} />
        <Route path="/pull-requests/:id" element={<PullRequestDetailPage />} />
        <Route path="/reviews/:id" element={<ReviewResultsPage />} />
        <Route path="/findings" element={<FindingsPage />} />
        <Route path="/analytics" element={<AnalyticsPage />} />
        <Route path="/teams" element={<TeamsPage />} />
        <Route path="/teams/:id" element={<TeamDetailPage />} />
        <Route path="/notifications" element={<NotificationsPage />} />

        {/* Settings */}
        <Route path="/settings/ai" element={<AiProvidersPage />} />
        <Route path="/settings/github" element={<GithubIntegrationPage />} />
        <Route path="/settings/profile" element={<ProfilePage />} />
        <Route path="/settings/organization" element={<OrganizationPage />} />
        <Route path="/settings/billing" element={<BillingPage />} />

        {/* Admin Only */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={['SUPER_ADMIN', 'ADMIN']}>
              <AdminDashboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/audit-logs"
          element={
            <ProtectedRoute allowedRoles={['SUPER_ADMIN', 'ADMIN']}>
              <AuditLogsPage />
            </ProtectedRoute>
          }
        />
      </Route>

      {/* Errors */}
      <Route path="/403" element={<ForbiddenPage />} />
      <Route path="/500" element={<ServerErrorPage />} />
      <Route path="/404" element={<NotFoundPage />} />
      <Route path="*" element={<Navigate to="/404" replace />} />
    </Routes>
  );
};

export default App;

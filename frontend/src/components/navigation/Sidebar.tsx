import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { cn } from '@/lib/utils';
import {
  LayoutDashboard,
  GitPullRequest,
  FolderGit2,
  AlertOctagon,
  BarChart3,
  Users2,
  Bell,
  Cpu,
  Building2,
  CreditCard,
  ShieldCheck,
  FileText,
  LogOut,
  ChevronLeft,
  ChevronRight,
  Sparkles,
} from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { useAppDispatch, useAppSelector } from '@/store';
import { logout } from '@/store/slices/authSlice';

interface SidebarProps {
  collapsed: boolean;
  onToggle: () => void;
  mobileOpen: boolean;
  onMobileClose: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  collapsed,
  onToggle,
  mobileOpen,
  onMobileClose,
}) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { user } = useAppSelector((state) => state.auth);

  const isAdminOrSuperAdmin =
    user?.role === 'SUPER_ADMIN' || user?.role === 'ADMIN';

  const navGroups = [
    {
      title: 'Overview',
      items: [
        { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { label: 'Pull Requests', path: '/pull-requests', icon: GitPullRequest },
        { label: 'Repositories', path: '/repositories', icon: FolderGit2 },
        { label: 'Findings', path: '/findings', icon: AlertOctagon },
        { label: 'Analytics', path: '/analytics', icon: BarChart3 },
      ],
    },
    {
      title: 'Collaboration',
      items: [
        { label: 'Teams', path: '/teams', icon: Users2 },
        { label: 'Notifications', path: '/notifications', icon: Bell },
      ],
    },
    {
      title: 'Settings',
      items: [
        { label: 'AI Providers', path: '/settings/ai', icon: Cpu },
        { label: 'GitHub App', path: '/settings/github', icon: GithubIcon },
        { label: 'Organization', path: '/settings/organization', icon: Building2 },
        { label: 'Usage & Plans', path: '/settings/billing', icon: CreditCard },
      ],
    },
    ...(isAdminOrSuperAdmin
      ? [
          {
            title: 'Administration',
            items: [
              { label: 'Admin Console', path: '/admin', icon: ShieldCheck },
              { label: 'Audit Logs', path: '/admin/audit-logs', icon: FileText },
            ],
          },
        ]
      : []),
  ];

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  const sidebarContent = (
    <div className="flex flex-col h-full bg-slate-900/95 border-r border-slate-800 text-slate-300">
      {/* Brand Header */}
      <div className="flex items-center justify-between h-16 px-4 border-b border-slate-800">
        <NavLink
          to="/dashboard"
          className="flex items-center gap-3 transition-opacity hover:opacity-90 overflow-hidden"
        >
          <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-indigo-600 via-indigo-500 to-purple-600 flex items-center justify-center text-white shadow-lg shadow-indigo-500/20 shrink-0">
            <Sparkles className="w-5 h-5" />
          </div>
          {!collapsed && (
            <div className="flex flex-col truncate">
              <span className="font-bold text-white text-base tracking-tight leading-tight">
                ReviewAI
              </span>
              <span className="text-[10px] text-indigo-400 font-medium uppercase tracking-wider">
                PR Intelligence
              </span>
            </div>
          )}
        </NavLink>

        <button
          onClick={onToggle}
          className="hidden lg:flex items-center justify-center w-7 h-7 rounded-lg bg-slate-800/80 hover:bg-slate-700 text-slate-400 hover:text-white transition-colors"
        >
          {collapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
        </button>
      </div>

      {/* Nav items */}
      <div className="flex-1 overflow-y-auto px-3 py-4 space-y-6">
        {navGroups.map((group, idx) => (
          <div key={idx} className="space-y-1">
            {!collapsed && (
              <h4 className="px-3 text-[11px] font-semibold text-slate-400 uppercase tracking-wider mb-2">
                {group.title}
              </h4>
            )}
            {group.items.map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  onClick={onMobileClose}
                  className={({ isActive }) =>
                    cn(
                      'flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all duration-150 group relative',
                      isActive
                        ? 'bg-gradient-to-r from-indigo-600/20 to-purple-600/10 text-indigo-400 border border-indigo-500/30 shadow-sm'
                        : 'text-slate-400 hover:text-slate-100 hover:bg-slate-800/60'
                    )
                  }
                  title={collapsed ? item.label : undefined}
                >
                  <Icon className="w-5 h-5 shrink-0 transition-transform group-hover:scale-105" />
                  {!collapsed && <span className="truncate">{item.label}</span>}
                </NavLink>
              );
            })}
          </div>
        ))}
      </div>

      {/* User profile & logout */}
      <div className="p-3 border-t border-slate-800/80">
        <div
          className={cn(
            'flex items-center gap-3 p-2 rounded-xl bg-slate-800/40 border border-slate-700/50',
            collapsed ? 'justify-center' : 'justify-between'
          )}
        >
          <div className="flex items-center gap-2.5 truncate">
            <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center text-white text-xs font-bold uppercase shrink-0">
              {user?.fullName?.charAt(0) || 'U'}
            </div>
            {!collapsed && (
              <div className="truncate">
                <p className="text-xs font-semibold text-slate-100 truncate">
                  {user?.fullName || 'User'}
                </p>
                <p className="text-[11px] text-slate-400 truncate">{user?.email}</p>
              </div>
            )}
          </div>
          {!collapsed && (
            <button
              onClick={handleLogout}
              className="p-1.5 rounded-lg text-slate-400 hover:text-rose-400 hover:bg-slate-800 transition-colors"
              title="Sign Out"
            >
              <LogOut className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>
    </div>
  );

  return (
    <>
      {/* Desktop Sidebar */}
      <aside
        className={cn(
          'hidden lg:block fixed inset-y-0 left-0 z-30 transition-all duration-200 ease-in-out',
          collapsed ? 'w-20' : 'w-64'
        )}
      >
        {sidebarContent}
      </aside>

      {/* Mobile Drawer Backdrop */}
      {mobileOpen && (
        <div
          className="fixed inset-0 z-40 bg-slate-950/80 backdrop-blur-sm lg:hidden"
          onClick={onMobileClose}
        />
      )}

      {/* Mobile Drawer */}
      <div
        className={cn(
          'fixed inset-y-0 left-0 z-50 w-72 lg:hidden transform transition-transform duration-200 ease-in-out',
          mobileOpen ? 'translate-x-0' : '-translate-x-full'
        )}
      >
        {sidebarContent}
      </div>
    </>
  );
};

import React from 'react';
import { NavLink } from 'react-router-dom';
import { cn } from '@/lib/utils';
import {
  Menu,
  Bell,
  Search,
  Sun,
  Moon,
  Plus,
  User as UserIcon,
} from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { useAppSelector } from '@/store';

interface NavbarProps {
  onMobileOpen: () => void;
  collapsed: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({ onMobileOpen, collapsed }) => {
  const { user } = useAppSelector((state) => state.auth);
  const [isDark, setIsDark] = React.useState<boolean>(() => {
    if (typeof window !== 'undefined') {
      const saved = localStorage.getItem('theme');
      if (saved === 'light') return false;
      if (saved === 'dark') return true;
      return document.documentElement.classList.contains('dark');
    }
    return true;
  });

  React.useEffect(() => {
    const saved = localStorage.getItem('theme');
    if (saved === 'light') {
      document.documentElement.classList.remove('dark');
      document.documentElement.classList.add('light');
      setIsDark(false);
    } else {
      document.documentElement.classList.remove('light');
      document.documentElement.classList.add('dark');
      setIsDark(true);
    }
  }, []);

  const toggleTheme = () => {
    const nextDark = !isDark;
    setIsDark(nextDark);
    if (nextDark) {
      document.documentElement.classList.remove('light');
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      document.documentElement.classList.add('light');
      localStorage.setItem('theme', 'light');
    }
  };

  return (
    <header
      className={cn(
        'sticky top-0 z-20 h-16 bg-slate-900/80 backdrop-blur-md border-b border-slate-800 transition-all duration-200 px-4 lg:px-8 flex items-center justify-between',
        collapsed ? 'lg:pl-24' : 'lg:pl-72'
      )}
    >
      {/* Left side: Mobile trigger & breadcrumb/search */}
      <div className="flex items-center gap-4">
        <button
          onClick={onMobileOpen}
          className="lg:hidden p-2 rounded-xl bg-slate-800 text-slate-300 hover:text-white"
        >
          <Menu className="w-5 h-5" />
        </button>

        <div className="hidden sm:flex items-center gap-2 px-3 py-1.5 rounded-xl bg-slate-800/60 border border-slate-700/60 text-slate-400 text-xs w-64 lg:w-80">
          <Search className="w-4 h-4 shrink-0" />
          <input
            type="text"
            placeholder="Search PRs, repos, findings..."
            className="bg-transparent text-slate-200 placeholder:text-slate-500 text-xs focus:outline-none w-full"
          />
          <kbd className="px-1.5 py-0.5 rounded bg-slate-700/60 text-[10px] font-mono text-slate-400">
            ⌘K
          </kbd>
        </div>
      </div>

      {/* Right side: Actions, Theme, Notifications, User */}
      <div className="flex items-center gap-3">
        {/* Connect Repo CTA */}
        <NavLink
          to="/repositories"
          className="hidden md:inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-indigo-600/20 text-indigo-400 border border-indigo-500/30 text-xs font-semibold hover:bg-indigo-600/30 transition-colors"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Connect Repo</span>
        </NavLink>

        {/* Theme Toggle */}
        <button
          onClick={toggleTheme}
          className="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
          title="Toggle Light/Dark Theme"
        >
          {isDark ? <Sun className="w-4 h-4" /> : <Moon className="w-4 h-4" />}
        </button>

        {/* Notifications */}
        <NavLink
          to="/notifications"
          className="relative p-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
          title="Notifications"
        >
          <Bell className="w-4 h-4" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-indigo-500 ring-2 ring-slate-900" />
        </NavLink>

        {/* User Avatar */}
        <NavLink
          to="/settings/profile"
          className="flex items-center gap-2 pl-2 border-l border-slate-800"
        >
          <div className="w-8 h-8 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-200 hover:border-indigo-500 transition-colors">
            <UserIcon className="w-4 h-4" />
          </div>
        </NavLink>
      </div>
    </header>
  );
};

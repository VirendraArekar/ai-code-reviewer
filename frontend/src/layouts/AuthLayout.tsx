import React from 'react';
import { Outlet, NavLink } from 'react-router-dom';
import { Sparkles, ShieldCheck, Zap, GitPullRequest } from 'lucide-react';

export const AuthLayout: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8 relative overflow-hidden">
      {/* Background Glow Orbs */}
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-indigo-600/15 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-10 left-10 w-72 h-72 bg-purple-600/10 rounded-full blur-3xl pointer-events-none" />

      <div className="sm:mx-auto sm:w-full sm:max-w-md relative z-10 text-center">
        <NavLink to="/" className="inline-flex items-center gap-3 mb-6">
          <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-600 via-indigo-500 to-purple-600 flex items-center justify-center text-white shadow-xl shadow-indigo-500/25">
            <Sparkles className="w-6 h-6" />
          </div>
          <div className="text-left">
            <h1 className="font-bold text-2xl text-white tracking-tight leading-none">
              ReviewAI
            </h1>
            <p className="text-xs text-indigo-400 font-medium tracking-wider uppercase mt-1">
              Autonomous PR Intelligence
            </p>
          </div>
        </NavLink>
      </div>

      <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md relative z-10">
        <div className="bg-slate-900/80 backdrop-blur-xl py-8 px-6 sm:px-10 shadow-2xl rounded-3xl border border-slate-800/80">
          <Outlet />
        </div>

        {/* Feature Pills Footer */}
        <div className="mt-8 flex items-center justify-center gap-4 text-xs text-slate-400">
          <span className="flex items-center gap-1.5">
            <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" /> OWASP & CVE Scanning
          </span>
          <span className="flex items-center gap-1.5">
            <Zap className="w-3.5 h-3.5 text-amber-400" /> Sub-minute Reviews
          </span>
        </div>
      </div>
    </div>
  );
};

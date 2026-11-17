import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  Sparkles,
  ShieldCheck,
  Zap,
  GitPullRequest,
  CheckCircle2,
  Bug,
  Cpu,
  Layers,
  ArrowRight,
  Terminal,
  Lock,
  ChevronRight,
  Star,
} from 'lucide-react';
import { Button } from '@/components/ui/Button';

export const LandingPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 selection:bg-indigo-500 selection:text-white">
      {/* Top Navbar */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-slate-950/70 border-b border-slate-800/80">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-indigo-600 via-indigo-500 to-purple-600 flex items-center justify-center text-white shadow-lg shadow-indigo-500/25">
              <Sparkles className="w-5 h-5" />
            </div>
            <span className="font-bold text-lg text-white tracking-tight">ReviewAI</span>
          </div>

          <nav className="hidden md:flex items-center gap-8 text-sm text-slate-300 font-medium">
            <a href="#features" className="hover:text-indigo-400 transition-colors">Features</a>
            <a href="#how-it-works" className="hover:text-indigo-400 transition-colors">How It Works</a>
            <a href="#pricing" className="hover:text-indigo-400 transition-colors">Pricing</a>
            <a href="#security" className="hover:text-indigo-400 transition-colors">Security</a>
          </nav>

          <div className="flex items-center gap-3">
            <NavLink to="/login">
              <Button variant="ghost" size="sm">Sign In</Button>
            </NavLink>
            <NavLink to="/register">
              <Button variant="primary" size="sm" rightIcon={<ArrowRight className="w-4 h-4" />}>
                Get Started
              </Button>
            </NavLink>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative pt-24 pb-20 px-4 sm:px-6 lg:px-8 overflow-hidden">
        {/* Glow Effects */}
        <div className="absolute top-1/3 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[350px] bg-indigo-600/20 rounded-full blur-[120px] pointer-events-none" />
        <div className="absolute top-1/4 right-1/4 w-[300px] h-[300px] bg-purple-600/15 rounded-full blur-[100px] pointer-events-none" />

        <div className="max-w-4xl mx-auto text-center relative z-10 space-y-6">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-indigo-500/10 border border-indigo-500/30 text-indigo-400 text-xs font-semibold tracking-wide">
            <Sparkles className="w-3.5 h-3.5 animate-pulse" />
            <span>Next-Gen Autonomous Pull Request Intelligence</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-extrabold tracking-tight text-white leading-tight">
            Catch critical bugs and vulnerabilities{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400">
              before they hit production.
            </span>
          </h1>

          <p className="text-lg sm:text-xl text-slate-400 max-w-2xl mx-auto leading-relaxed">
            AI Code Reviewer connects directly to your GitHub Pull Requests. It analyzes syntax diffs, enforces architecture rules, checks OWASP top 10 risks, and generates one-click code fixes.
          </p>

          <div className="pt-4 flex flex-col sm:flex-row items-center justify-center gap-4">
            <NavLink to="/register">
              <Button variant="primary" size="lg" className="w-full sm:w-auto text-base">
                Install GitHub App Free
              </Button>
            </NavLink>
            <NavLink to="/login">
              <Button variant="outline" size="lg" className="w-full sm:w-auto text-base">
                Live Interactive Demo
              </Button>
            </NavLink>
          </div>

          {/* Social Proof */}
          <div className="pt-8 flex items-center justify-center gap-6 text-xs text-slate-400">
            <span className="flex items-center gap-1.5">
              <CheckCircle2 className="w-4 h-4 text-emerald-400" /> Supports Java, TS, Python, Go & 7 more
            </span>
            <span className="flex items-center gap-1.5">
              <Lock className="w-4 h-4 text-indigo-400" /> Zero Code Retention Mode
            </span>
            <span className="flex items-center gap-1.5">
              <Zap className="w-4 h-4 text-amber-400" /> &lt; 30 Second Review Times
            </span>
          </div>
        </div>

        {/* Hero Code / PR Preview Mockup */}
        <div className="mt-16 max-w-5xl mx-auto rounded-3xl border border-slate-800 bg-slate-900/90 shadow-2xl overflow-hidden backdrop-blur-xl">
          <div className="h-11 bg-slate-950 border-b border-slate-800 flex items-center justify-between px-4">
            <div className="flex items-center gap-2">
              <span className="w-3 h-3 rounded-full bg-rose-500/80 inline-block" />
              <span className="w-3 h-3 rounded-full bg-amber-500/80 inline-block" />
              <span className="w-3 h-3 rounded-full bg-emerald-500/80 inline-block" />
              <span className="text-xs text-slate-400 ml-2 font-mono">pr #42 - auth-service/jwt-validator.ts</span>
            </div>
            <span className="text-xs font-semibold px-2 py-0.5 rounded bg-rose-500/20 text-rose-400 border border-rose-500/30">
              1 Critical Bug Found
            </span>
          </div>

          <div className="p-6 font-mono text-xs sm:text-sm space-y-3 bg-slate-950/80">
            <div className="text-slate-500">// Vulnerable changed line detected in diff:</div>
            <div className="p-3 rounded-xl bg-rose-950/30 border border-rose-500/30 text-rose-300 flex items-start justify-between">
              <div>
                <span className="text-rose-500 font-bold mr-2">-</span>
                <span>jwt.verify(token, process.env.SECRET, &#123; algorithms: ['none', 'HS256'] &#125;);</span>
              </div>
              <span className="text-[10px] uppercase font-bold text-rose-400 bg-rose-500/20 px-2 py-0.5 rounded">
                CRITICAL: ALGORITHM NONE
              </span>
            </div>
            <div className="p-3 rounded-xl bg-emerald-950/30 border border-emerald-500/30 text-emerald-300">
              <span className="text-emerald-500 font-bold mr-2">+</span>
              <span>jwt.verify(token, process.env.SECRET, &#123; algorithms: ['HS256'] &#125;); // Enforces strong HMAC</span>
            </div>
          </div>
        </div>
      </section>

      {/* 6 Category Highlights */}
      <section id="features" className="py-20 px-4 sm:px-6 lg:px-8 border-t border-slate-800/80 bg-slate-900/30">
        <div className="max-w-7xl mx-auto space-y-12">
          <div className="text-center space-y-3 max-w-2xl mx-auto">
            <h2 className="text-3xl font-bold tracking-tight text-white">
              Comprehensive 6-Pillar Static & AI Analysis
            </h2>
            <p className="text-slate-400 text-sm sm:text-base">
              Every pull request undergoes multi-stage AST analysis combined with customized LLM reasoning.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[
              {
                icon: Bug,
                title: 'Bug & Crash Prevention',
                desc: 'Identifies null pointer exceptions, unhandled promises, race conditions, and boundary index overflows.',
                color: 'text-rose-400 bg-rose-500/10 border-rose-500/30',
              },
              {
                icon: ShieldCheck,
                title: 'Security Vulnerability Audit',
                desc: 'Audits OWASP Top 10: SQL injection, XSS, CSRF, insecure deserialization, IDOR, and leaked tokens.',
                color: 'text-purple-400 bg-purple-500/10 border-purple-500/30',
              },
              {
                icon: Zap,
                title: 'Performance Optimization',
                desc: 'Spots N+1 database queries, inefficient algorithm complexity, React re-render cascades, and memory leaks.',
                color: 'text-amber-400 bg-amber-500/10 border-amber-500/30',
              },
              {
                icon: Sparkles,
                title: 'Code Quality & Smells',
                desc: 'Enforces SOLID architecture principles, dead code pruning, naming clarity, and cyclomatic complexity limits.',
                color: 'text-cyan-400 bg-cyan-500/10 border-cyan-500/30',
              },
              {
                icon: Layers,
                title: 'Architectural Compliance',
                desc: 'Detects circular imports, leaky boundary abstractions, missing validation layers, and tight coupling.',
                color: 'text-indigo-400 bg-indigo-500/10 border-indigo-500/30',
              },
              {
                icon: Cpu,
                title: 'Unit Test Generation',
                desc: 'Automatically recommends missing test edge cases and produces runnable JUnit, Vitest, or pytest snippets.',
                color: 'text-emerald-400 bg-emerald-500/10 border-emerald-500/30',
              },
            ].map((feature, i) => {
              const Icon = feature.icon;
              return (
                <div
                  key={i}
                  className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 space-y-4 hover:border-slate-700 transition-all hover:translate-y-[-2px]"
                >
                  <div className={`w-11 h-11 rounded-xl flex items-center justify-center border ${feature.color}`}>
                    <Icon className="w-5 h-5" />
                  </div>
                  <h3 className="text-lg font-semibold text-white">{feature.title}</h3>
                  <p className="text-sm text-slate-400 leading-relaxed">{feature.desc}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
        <div className="max-w-4xl mx-auto rounded-3xl bg-gradient-to-r from-indigo-900/60 via-purple-900/60 to-slate-900/60 border border-indigo-500/30 p-10 sm:p-14 text-center space-y-6 relative z-10 backdrop-blur-xl">
          <h2 className="text-3xl sm:text-4xl font-bold text-white tracking-tight">
            Ready to upgrade your pull request reviews?
          </h2>
          <p className="text-slate-300 max-w-xl mx-auto text-sm sm:text-base">
            Connect your repository in under 2 minutes. Empower your engineering team with instant, actionable code insights.
          </p>
          <div className="pt-2">
            <NavLink to="/register">
              <Button variant="primary" size="lg" className="text-base px-8">
                Start Free Trial
              </Button>
            </NavLink>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-slate-800/80 py-12 px-4 sm:px-6 lg:px-8 text-center text-xs text-slate-500">
        <p>© 2026 ReviewAI SaaS Inc. All rights reserved. Enterprise-grade AI Code Reviewer.</p>
      </footer>
    </div>
  );
};

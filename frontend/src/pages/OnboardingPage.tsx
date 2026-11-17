import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  CheckCircle2,
  FolderGit2,
  Cpu,
  ArrowRight,
  ShieldCheck,
  Sparkles,
} from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Button } from '@/components/ui/Button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card';

export const OnboardingPage: React.FC = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState<1 | 2 | 3>(1);
  const [selectedRepos, setSelectedRepos] = useState<string[]>(['backend-api', 'frontend-web']);
  const [selectedProvider, setSelectedProvider] = useState<'OPENAI' | 'GEMINI' | 'OLLAMA'>('GEMINI');

  const dummyRepos = [
    { id: 'backend-api', name: 'org/backend-api', lang: 'Java', stars: 42 },
    { id: 'frontend-web', name: 'org/frontend-web', lang: 'TypeScript', stars: 128 },
    { id: 'payments-microservice', name: 'org/payments-microservice', lang: 'Go', stars: 15 },
    { id: 'data-pipeline', name: 'org/data-pipeline', lang: 'Python', stars: 33 },
  ];

  const toggleRepo = (id: string) => {
    if (selectedRepos.includes(id)) {
      setSelectedRepos(selectedRepos.filter((r) => r !== id));
    } else {
      setSelectedRepos([...selectedRepos, id]);
    }
  };

  const handleFinish = () => {
    navigate('/dashboard');
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto w-full space-y-8">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-600 to-purple-600 flex items-center justify-center text-white mx-auto shadow-xl shadow-indigo-500/25">
            <Sparkles className="w-6 h-6" />
          </div>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
            Welcome to ReviewAI
          </h1>
          <p className="text-sm text-slate-400">
            Let&apos;s get your workspace configured in just 3 quick steps
          </p>

          {/* Stepper Progress */}
          <div className="flex items-center justify-center gap-4 pt-4">
            {[1, 2, 3].map((s) => (
              <div key={s} className="flex items-center gap-2">
                <div
                  className={`w-7 h-7 rounded-full text-xs font-bold flex items-center justify-center transition-all ${
                    step >= s
                      ? 'bg-indigo-600 text-white ring-2 ring-indigo-400/40'
                      : 'bg-slate-800 text-slate-500'
                  }`}
                >
                  {step > s ? <CheckCircle2 className="w-4 h-4" /> : s}
                </div>
                {s < 3 && <div className={`w-12 h-0.5 ${step > s ? 'bg-indigo-600' : 'bg-slate-800'}`} />}
              </div>
            ))}
          </div>
        </div>

        {/* Step 1: Install GitHub App */}
        {step === 1 && (
          <Card className="border-slate-800 bg-slate-900/80 p-6 sm:p-8 space-y-6">
            <div className="text-center space-y-2">
              <GithubIcon className="w-12 h-12 text-white mx-auto" />
              <h3 className="text-lg font-bold text-white">Connect GitHub Organization</h3>
              <p className="text-xs text-slate-400 max-w-md mx-auto leading-relaxed">
                Install our official GitHub App to securely grant read access to PR diffs and write access for inline review comments.
              </p>
            </div>

            <div className="p-4 rounded-xl bg-slate-950/60 border border-slate-800 space-y-2 text-xs text-slate-300">
              <div className="flex items-center gap-2 text-emerald-400 font-semibold">
                <ShieldCheck className="w-4 h-4" /> Least-Privilege Permissions:
              </div>
              <ul className="list-disc list-inside space-y-1 pl-2 text-slate-400">
                <li>Read access to code diffs and commit metadata</li>
                <li>Write access to Pull Request Checks & inline review comments</li>
                <li>Zero arbitrary code execution or repository modification</li>
              </ul>
            </div>

            <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-800">
              <Button
                variant="primary"
                onClick={() => setStep(2)}
                rightIcon={<ArrowRight className="w-4 h-4" />}
              >
                Connect GitHub & Continue
              </Button>
            </div>
          </Card>
        )}

        {/* Step 2: Select Repositories */}
        {step === 2 && (
          <Card className="border-slate-800 bg-slate-900/80 p-6 sm:p-8 space-y-6">
            <div className="space-y-1">
              <h3 className="text-lg font-bold text-white flex items-center gap-2">
                <FolderGit2 className="w-5 h-5 text-indigo-400" /> Choose Repositories for AI Review
              </h3>
              <p className="text-xs text-slate-400">
                Select repositories where ReviewAI should automatically analyze incoming pull requests
              </p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {dummyRepos.map((repo) => {
                const isSelected = selectedRepos.includes(repo.id);
                return (
                  <div
                    key={repo.id}
                    onClick={() => toggleRepo(repo.id)}
                    className={`p-4 rounded-xl border cursor-pointer transition-all flex items-center justify-between ${
                      isSelected
                        ? 'bg-indigo-600/15 border-indigo-500/50 shadow-sm'
                        : 'bg-slate-950/50 border-slate-800 hover:border-slate-700'
                    }`}
                  >
                    <div>
                      <h4 className="text-sm font-semibold text-white">{repo.name}</h4>
                      <span className="text-xs text-slate-400">{repo.lang}</span>
                    </div>
                    <div
                      className={`w-5 h-5 rounded-md flex items-center justify-center border ${
                        isSelected
                          ? 'bg-indigo-600 border-indigo-500 text-white'
                          : 'border-slate-700 bg-slate-900'
                      }`}
                    >
                      {isSelected && <CheckCircle2 className="w-3.5 h-3.5" />}
                    </div>
                  </div>
                );
              })}
            </div>

            <div className="flex items-center justify-between pt-4 border-t border-slate-800">
              <Button variant="ghost" onClick={() => setStep(1)}>
                Back
              </Button>
              <Button
                variant="primary"
                onClick={() => setStep(3)}
                rightIcon={<ArrowRight className="w-4 h-4" />}
              >
                Next: AI Settings
              </Button>
            </div>
          </Card>
        )}

        {/* Step 3: Choose AI Provider */}
        {step === 3 && (
          <Card className="border-slate-800 bg-slate-900/80 p-6 sm:p-8 space-y-6">
            <div className="space-y-1">
              <h3 className="text-lg font-bold text-white flex items-center gap-2">
                <Cpu className="w-5 h-5 text-purple-400" /> Select Default AI Engine
              </h3>
              <p className="text-xs text-slate-400">
                Pick the primary model used for AST diff analysis and code generation
              </p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              {[
                {
                  id: 'GEMINI' as const,
                  title: 'Google Gemini',
                  model: 'gemini-2.0-flash / pro',
                  badge: 'Recommended',
                  desc: 'Ultra-fast token throughput and massive 1M context window for large pull requests.',
                },
                {
                  id: 'OPENAI' as const,
                  title: 'OpenAI',
                  model: 'gpt-4o / gpt-4o-mini',
                  badge: 'High Accuracy',
                  desc: 'Exceptional reasoning on intricate algorithms and race conditions.',
                },
                {
                  id: 'OLLAMA' as const,
                  title: 'Ollama (Local/On-Prem)',
                  model: 'deepseek-coder / llama3',
                  badge: 'Air-Gapped',
                  desc: 'Keep 100% of your source code on your private infrastructure.',
                },
              ].map((prov) => {
                const isSelected = selectedProvider === prov.id;
                return (
                  <div
                    key={prov.id}
                    onClick={() => setSelectedProvider(prov.id)}
                    className={`p-4 rounded-2xl border cursor-pointer transition-all flex flex-col justify-between ${
                      isSelected
                        ? 'bg-indigo-600/15 border-indigo-500 shadow-md ring-1 ring-indigo-500/40'
                        : 'bg-slate-950/50 border-slate-800 hover:border-slate-700'
                    }`}
                  >
                    <div>
                      <div className="flex items-center justify-between mb-2">
                        <span className="text-xs font-bold text-indigo-400 bg-indigo-500/10 px-2 py-0.5 rounded">
                          {prov.badge}
                        </span>
                        {isSelected && <CheckCircle2 className="w-4 h-4 text-indigo-400" />}
                      </div>
                      <h4 className="font-semibold text-white text-sm">{prov.title}</h4>
                      <p className="text-[11px] font-mono text-slate-400 mt-0.5">{prov.model}</p>
                      <p className="text-xs text-slate-400 mt-2 leading-relaxed">{prov.desc}</p>
                    </div>
                  </div>
                );
              })}
            </div>

            <div className="flex items-center justify-between pt-4 border-t border-slate-800">
              <Button variant="ghost" onClick={() => setStep(2)}>
                Back
              </Button>
              <Button variant="primary" onClick={handleFinish} rightIcon={<CheckCircle2 className="w-4 h-4" />}>
                Complete Setup & Go to Dashboard
              </Button>
            </div>
          </Card>
        )}
      </div>
    </div>
  );
};

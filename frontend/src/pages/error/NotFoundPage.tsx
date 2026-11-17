import React from 'react';
import { NavLink } from 'react-router-dom';
import { HelpCircle, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/Button';

export const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center p-4 text-center">
      <div className="w-16 h-16 rounded-2xl bg-indigo-500/10 border border-indigo-500/30 text-indigo-400 flex items-center justify-center mb-6">
        <HelpCircle className="w-8 h-8" />
      </div>
      <h1 className="text-4xl font-extrabold text-white tracking-tight mb-2">404 - Page Not Found</h1>
      <p className="text-sm text-slate-400 max-w-md mb-8">
        The repository, pull request, or page you are attempting to reach does not exist or has been moved.
      </p>
      <NavLink to="/dashboard">
        <Button variant="primary" leftIcon={<ArrowLeft className="w-4 h-4" />}>
          Return to Dashboard
        </Button>
      </NavLink>
    </div>
  );
};

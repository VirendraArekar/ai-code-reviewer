import React from 'react';
import { NavLink } from 'react-router-dom';
import { AlertTriangle, RefreshCw, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/Button';

export const ServerErrorPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center p-4 text-center">
      <div className="w-16 h-16 rounded-2xl bg-amber-500/10 border border-amber-500/30 text-amber-400 flex items-center justify-center mb-6">
        <AlertTriangle className="w-8 h-8" />
      </div>
      <h1 className="text-4xl font-extrabold text-white tracking-tight mb-2">500 - Server Error</h1>
      <p className="text-sm text-slate-400 max-w-md mb-8">
        An unexpected error occurred while processing your request. Our automated observability pipeline has been notified.
      </p>
      <div className="flex items-center gap-3">
        <Button variant="outline" onClick={() => window.location.reload()} leftIcon={<RefreshCw className="w-4 h-4" />}>
          Try Again
        </Button>
        <NavLink to="/dashboard">
          <Button variant="primary" leftIcon={<ArrowLeft className="w-4 h-4" />}>
            Back to Dashboard
          </Button>
        </NavLink>
      </div>
    </div>
  );
};

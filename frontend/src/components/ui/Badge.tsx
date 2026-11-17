import React from 'react';
import { cn } from '@/lib/utils';
import { FindingSeverity, FindingCategory, FindingStatus } from '@/types';
import {
  AlertTriangle,
  Bug,
  ShieldAlert,
  Zap,
  Sparkles,
  Layers,
  FlaskConical,
  CheckCircle2,
  XCircle,
  HelpCircle,
  Clock,
} from 'lucide-react';

interface BadgeProps extends React.HTMLAttributes<HTMLSpanElement> {
  variant?: 'default' | 'outline' | 'severity' | 'category' | 'status';
  severity?: FindingSeverity;
  category?: FindingCategory;
  status?: FindingStatus;
  size?: 'sm' | 'md';
}

export const Badge: React.FC<BadgeProps> = ({
  className,
  variant = 'default',
  severity,
  category,
  status,
  size = 'md',
  children,
  ...props
}) => {
  const sizeClasses = size === 'sm' ? 'px-2 py-0.5 text-xs' : 'px-2.5 py-1 text-xs font-semibold';

  if (severity) {
    const severityStyles = {
      CRITICAL: 'bg-rose-500/15 text-rose-400 border border-rose-500/30',
      HIGH: 'bg-orange-500/15 text-orange-400 border border-orange-500/30',
      MEDIUM: 'bg-amber-500/15 text-amber-400 border border-amber-500/30',
      LOW: 'bg-blue-500/15 text-blue-400 border border-blue-500/30',
      INFO: 'bg-slate-500/15 text-slate-300 border border-slate-500/30',
    };

    return (
      <span
        className={cn(
          'inline-flex items-center gap-1 rounded-full uppercase tracking-wider',
          sizeClasses,
          severityStyles[severity],
          className
        )}
        {...props}
      >
        <span className="w-1.5 h-1.5 rounded-full bg-current animate-pulse" />
        {children || severity}
      </span>
    );
  }

  if (category) {
    const categoryStyles = {
      BUG: 'bg-red-500/15 text-red-400 border border-red-500/30',
      SECURITY: 'bg-purple-500/15 text-purple-400 border border-purple-500/30',
      PERFORMANCE: 'bg-amber-500/15 text-amber-400 border border-amber-500/30',
      CODE_QUALITY: 'bg-cyan-500/15 text-cyan-400 border border-cyan-500/30',
      ARCHITECTURE: 'bg-indigo-500/15 text-indigo-400 border border-indigo-500/30',
      TESTING: 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30',
    };

    const icons = {
      BUG: <Bug className="w-3 h-3" />,
      SECURITY: <ShieldAlert className="w-3 h-3" />,
      PERFORMANCE: <Zap className="w-3 h-3" />,
      CODE_QUALITY: <Sparkles className="w-3 h-3" />,
      ARCHITECTURE: <Layers className="w-3 h-3" />,
      TESTING: <FlaskConical className="w-3 h-3" />,
    };

    return (
      <span
        className={cn(
          'inline-flex items-center gap-1.5 rounded-md font-medium tracking-wide',
          sizeClasses,
          categoryStyles[category],
          className
        )}
        {...props}
      >
        {icons[category]}
        {children || category.replace('_', ' ')}
      </span>
    );
  }

  if (status) {
    const statusStyles = {
      OPEN: 'bg-amber-500/15 text-amber-400 border border-amber-500/30',
      RESOLVED: 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30',
      IGNORED: 'bg-slate-500/15 text-slate-400 border border-slate-500/30',
      FALSE_POSITIVE: 'bg-indigo-500/15 text-indigo-400 border border-indigo-500/30',
    };

    const statusIcons = {
      OPEN: <Clock className="w-3 h-3" />,
      RESOLVED: <CheckCircle2 className="w-3 h-3" />,
      IGNORED: <XCircle className="w-3 h-3" />,
      FALSE_POSITIVE: <HelpCircle className="w-3 h-3" />,
    };

    return (
      <span
        className={cn(
          'inline-flex items-center gap-1 rounded-md font-medium',
          sizeClasses,
          statusStyles[status],
          className
        )}
        {...props}
      >
        {statusIcons[status]}
        {children || status.replace('_', ' ')}
      </span>
    );
  }

  return (
    <span
      className={cn(
        'inline-flex items-center rounded-md bg-slate-800 text-slate-200 border border-slate-700 font-medium',
        sizeClasses,
        className
      )}
      {...props}
    >
      {children}
    </span>
  );
};

import React from 'react';
import { cn } from '@/lib/utils';

interface ScoreGaugeProps {
  score: number; // 0 to 100
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  showGrade?: boolean;
}

export const ScoreGauge: React.FC<ScoreGaugeProps> = ({
  score,
  size = 'md',
  label,
  showGrade = true,
}) => {
  const normalizedScore = Math.max(0, Math.min(100, Math.round(score)));

  const getScoreColor = (val: number) => {
    if (val >= 90) return { stroke: '#10b981', text: 'text-emerald-400', grade: 'A+' };
    if (val >= 80) return { stroke: '#3b82f6', text: 'text-blue-400', grade: 'A' };
    if (val >= 70) return { stroke: '#f59e0b', text: 'text-amber-400', grade: 'B' };
    if (val >= 60) return { stroke: '#f97316', text: 'text-orange-400', grade: 'C' };
    return { stroke: '#ef4444', text: 'text-rose-400', grade: 'F' };
  };

  const { stroke, text, grade } = getScoreColor(normalizedScore);

  const dimensions = {
    sm: { width: 64, height: 64, strokeWidth: 5, radius: 24, fontSize: 'text-sm' },
    md: { width: 96, height: 96, strokeWidth: 8, radius: 36, fontSize: 'text-xl' },
    lg: { width: 128, height: 128, strokeWidth: 10, radius: 48, fontSize: 'text-3xl' },
  };

  const dim = dimensions[size];
  const circumference = 2 * Math.PI * dim.radius;
  const strokeDashoffset = circumference - (normalizedScore / 100) * circumference;

  return (
    <div className="flex flex-col items-center justify-center text-center">
      <div className="relative inline-flex items-center justify-center">
        <svg
          width={dim.width}
          height={dim.height}
          className="transform -rotate-90 drop-shadow-md"
        >
          {/* Background Track */}
          <circle
            cx={dim.width / 2}
            cy={dim.height / 2}
            r={dim.radius}
            stroke="currentColor"
            strokeWidth={dim.strokeWidth}
            fill="transparent"
            className="text-slate-800"
          />
          {/* Score Indicator */}
          <circle
            cx={dim.width / 2}
            cy={dim.height / 2}
            r={dim.radius}
            stroke={stroke}
            strokeWidth={dim.strokeWidth}
            strokeDasharray={circumference}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            fill="transparent"
            className="transition-all duration-1000 ease-out"
          />
        </svg>

        <div className="absolute flex flex-col items-center justify-center">
          <span className={cn('font-bold font-mono tracking-tight', dim.fontSize, text)}>
            {normalizedScore}
          </span>
          {showGrade && size !== 'sm' && (
            <span className="text-[10px] uppercase font-bold text-slate-400 tracking-wider">
              {grade}
            </span>
          )}
        </div>
      </div>
      {label && <span className="text-xs font-medium text-slate-400 mt-2">{label}</span>}
    </div>
  );
};

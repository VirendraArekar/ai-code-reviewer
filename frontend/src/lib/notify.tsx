import React from 'react';
import { toast } from 'sonner';
import { CheckCircle2, AlertCircle, AlertTriangle, Info, Loader2 } from 'lucide-react';

export interface ToastOptions {
  description?: string;
  duration?: number;
  id?: string | number;
  action?: {
    label: string;
    onClick: () => void;
  };
}

export const notify = {
  success: (title: string, descriptionOrOptions?: string | ToastOptions) => {
    const opts = typeof descriptionOrOptions === 'string'
      ? { description: descriptionOrOptions }
      : descriptionOrOptions || {};

    return toast.success(title, {
      ...opts,
      icon: <CheckCircle2 className="w-5 h-5 text-emerald-500 shrink-0" />,
      duration: opts.duration ?? 4000,
    });
  },

  error: (title: string, descriptionOrOptions?: string | ToastOptions) => {
    const opts = typeof descriptionOrOptions === 'string'
      ? { description: descriptionOrOptions }
      : descriptionOrOptions || {};

    return toast.error(title, {
      ...opts,
      icon: <AlertCircle className="w-5 h-5 text-rose-500 shrink-0" />,
      duration: opts.duration ?? 5000,
    });
  },

  warning: (title: string, descriptionOrOptions?: string | ToastOptions) => {
    const opts = typeof descriptionOrOptions === 'string'
      ? { description: descriptionOrOptions }
      : descriptionOrOptions || {};

    return toast.warning(title, {
      ...opts,
      icon: <AlertTriangle className="w-5 h-5 text-amber-500 shrink-0" />,
      duration: opts.duration ?? 4500,
    });
  },

  info: (title: string, descriptionOrOptions?: string | ToastOptions) => {
    const opts = typeof descriptionOrOptions === 'string'
      ? { description: descriptionOrOptions }
      : descriptionOrOptions || {};

    return toast.info(title, {
      ...opts,
      icon: <Info className="w-5 h-5 text-sky-500 shrink-0" />,
      duration: opts.duration ?? 4000,
    });
  },

  loading: (title: string, descriptionOrOptions?: string | ToastOptions) => {
    const opts = typeof descriptionOrOptions === 'string'
      ? { description: descriptionOrOptions }
      : descriptionOrOptions || {};

    return toast.loading(title, {
      ...opts,
      icon: <Loader2 className="w-5 h-5 text-indigo-500 animate-spin shrink-0" />,
    });
  },

  dismiss: (id?: string | number) => {
    toast.dismiss(id);
  },

  promise: <T,>(
    promise: Promise<T>,
    msgs: {
      loading: string;
      success: string | ((data: T) => string);
      error: string | ((err: any) => string);
    }
  ) => {
    return toast.promise(promise, {
      loading: msgs.loading,
      success: msgs.success,
      error: msgs.error,
    });
  },
};

export default notify;

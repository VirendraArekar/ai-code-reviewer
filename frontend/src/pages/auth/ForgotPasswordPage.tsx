import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Mail, ArrowLeft, CheckCircle2 } from 'lucide-react';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { useForgotPasswordMutation } from '@/api/authApi';

const forgotPasswordSchema = z.object({
  email: z.string().email('Please enter a valid email address'),
});

type ForgotPasswordFormData = z.infer<typeof forgotPasswordSchema>;

export const ForgotPasswordPage: React.FC = () => {
  const [forgotPassword, { isLoading }] = useForgotPasswordMutation();
  const [isSuccess, setIsSuccess] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotPasswordFormData>({
    resolver: zodResolver(forgotPasswordSchema),
  });

  const onSubmit = async (data: ForgotPasswordFormData) => {
    setErrorMsg(null);
    try {
      await forgotPassword(data).unwrap();
      setIsSuccess(true);
    } catch (err: any) {
      setErrorMsg(err?.data?.message || 'Failed to send reset link. Try again.');
    }
  };

  if (isSuccess) {
    return (
      <div className="text-center space-y-4">
        <div className="w-12 h-12 rounded-full bg-emerald-500/20 text-emerald-400 flex items-center justify-center mx-auto border border-emerald-500/30">
          <CheckCircle2 className="w-6 h-6" />
        </div>
        <h3 className="text-lg font-bold text-white">Check your email</h3>
        <p className="text-xs text-slate-400 leading-relaxed">
          We have sent a secure password reset link to your email address if an account exists.
        </p>
        <NavLink to="/login" className="inline-block mt-4">
          <Button variant="outline" size="sm" leftIcon={<ArrowLeft className="w-4 h-4" />}>
            Back to Sign In
          </Button>
        </NavLink>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="text-center space-y-1">
        <h2 className="text-xl font-bold text-white tracking-tight">
          Reset password
        </h2>
        <p className="text-xs text-slate-400">
          Enter your email and we will send you a reset link
        </p>
      </div>

      {errorMsg && (
        <div className="p-3 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs text-center">
          {errorMsg}
        </div>
      )}

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">
            Work Email
          </label>
          <Input
            type="email"
            placeholder="you@company.com"
            leftIcon={<Mail className="w-4 h-4" />}
            error={errors.email?.message}
            {...register('email')}
          />
        </div>

        <Button
          type="submit"
          variant="primary"
          isLoading={isLoading}
          className="w-full mt-2"
        >
          Send Reset Link
        </Button>
      </form>

      <div className="text-center">
        <NavLink
          to="/login"
          className="inline-flex items-center gap-1.5 text-xs text-slate-400 hover:text-white"
        >
          <ArrowLeft className="w-3.5 h-3.5" /> Back to Sign In
        </NavLink>
      </div>
    </div>
  );
};

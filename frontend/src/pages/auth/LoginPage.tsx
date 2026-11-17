import React, { useState } from 'react';
import { NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Mail, Lock, ArrowRight } from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { useLoginMutation } from '@/api/authApi';
import { useAppDispatch } from '@/store';
import { setCredentials } from '@/store/slices/authSlice';

const loginSchema = z.object({
  email: z.string().email('Please enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

type LoginFormData = z.infer<typeof loginSchema>;

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const [login, { isLoading }] = useLoginMutation();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: 'admin@aicode.com',
      password: 'AdminPassword123!',
    },
  });

  const onSubmit = async (data: LoginFormData) => {
    setErrorMessage(null);
    try {
      const response = await login(data).unwrap();
      dispatch(setCredentials(response));
      const from = (location.state as any)?.from?.pathname || '/dashboard';
      navigate(from, { replace: true });
    } catch (err: any) {
      setErrorMessage(
        err?.data?.message || 'Invalid email or password. Please try again.'
      );
    }
  };

  const handleGithubOAuth = () => {
    window.location.href = '/api/v1/auth/github';
  };

  return (
    <div className="space-y-6">
      <div className="text-center space-y-1">
        <h2 className="text-xl font-bold text-white tracking-tight">
          Welcome back
        </h2>
        <p className="text-xs text-slate-400">
          Sign in to access your repositories and pull request reviews
        </p>
      </div>

      {errorMessage && (
        <div className="p-3 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs text-center font-medium">
          {errorMessage}
        </div>
      )}

      {/* GitHub OAuth Button */}
      <Button
        type="button"
        variant="secondary"
        onClick={handleGithubOAuth}
        className="w-full gap-2 text-sm bg-slate-800 hover:bg-slate-700 border-slate-700"
        leftIcon={<GithubIcon className="w-4 h-4" />}
      >
        Continue with GitHub
      </Button>

      <div className="relative flex items-center justify-center">
        <div className="border-t border-slate-800 w-full" />
        <span className="bg-slate-900 px-3 text-[11px] uppercase tracking-wider text-slate-500 absolute">
          or sign in with email
        </span>
      </div>

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

        <div>
          <div className="flex items-center justify-between mb-1">
            <label className="block text-xs font-medium text-slate-300">
              Password
            </label>
            <NavLink
              to="/forgot-password"
              className="text-xs text-indigo-400 hover:text-indigo-300"
            >
              Forgot password?
            </NavLink>
          </div>
          <Input
            type="password"
            placeholder="••••••••••••"
            leftIcon={<Lock className="w-4 h-4" />}
            error={errors.password?.message}
            {...register('password')}
          />
        </div>

        <Button
          type="submit"
          variant="primary"
          isLoading={isLoading}
          className="w-full mt-2"
          rightIcon={<ArrowRight className="w-4 h-4" />}
        >
          Sign In
        </Button>
      </form>

      <div className="text-center text-xs text-slate-400">
        Don&apos;t have an account yet?{' '}
        <NavLink
          to="/register"
          className="text-indigo-400 hover:text-indigo-300 font-medium"
        >
          Create an account
        </NavLink>
      </div>
    </div>
  );
};

import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Mail, Lock, User, Building, ArrowRight } from 'lucide-react';
import { GithubIcon } from '@/components/icons/GithubIcon';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { useRegisterMutation } from '@/api/authApi';
import { useAppDispatch } from '@/store';
import { setCredentials } from '@/store/slices/authSlice';

const registerSchema = z.object({
  fullName: z.string().min(2, 'Full name must be at least 2 characters'),
  email: z.string().email('Please enter a valid email address'),
  organizationName: z.string().min(2, 'Organization name must be at least 2 characters'),
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters')
    .regex(/[A-Z]/, 'Password must contain at least one uppercase letter')
    .regex(/[0-9]/, 'Password must contain at least one number'),
});

type RegisterFormData = z.infer<typeof registerSchema>;

export const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [registerUser, { isLoading }] = useRegisterMutation();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = async (data: RegisterFormData) => {
    setErrorMessage(null);
    try {
      const response = await registerUser(data).unwrap();
      dispatch(setCredentials(response));
      navigate('/onboarding');
    } catch (err: any) {
      setErrorMessage(
        err?.data?.message || 'Failed to create account. Please try again.'
      );
    }
  };

  return (
    <div className="space-y-6">
      <div className="text-center space-y-1">
        <h2 className="text-xl font-bold text-white tracking-tight">
          Create your account
        </h2>
        <p className="text-xs text-slate-400">
          Start reviewing pull requests automatically with AI
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
        onClick={() => (window.location.href = '/api/v1/auth/github')}
        className="w-full gap-2 text-sm bg-slate-800 hover:bg-slate-700 border-slate-700"
        leftIcon={<GithubIcon className="w-4 h-4" />}
      >
        Sign up with GitHub
      </Button>

      <div className="relative flex items-center justify-center">
        <div className="border-t border-slate-800 w-full" />
        <span className="bg-slate-900 px-3 text-[11px] uppercase tracking-wider text-slate-500 absolute">
          or register with email
        </span>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-3.5">
        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">
            Full Name
          </label>
          <Input
            type="text"
            placeholder="Ada Lovelace"
            leftIcon={<User className="w-4 h-4" />}
            error={errors.fullName?.message}
            {...register('fullName')}
          />
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">
            Organization / Team Name
          </label>
          <Input
            type="text"
            placeholder="Acme Engineering"
            leftIcon={<Building className="w-4 h-4" />}
            error={errors.organizationName?.message}
            {...register('organizationName')}
          />
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">
            Work Email
          </label>
          <Input
            type="email"
            placeholder="ada@acme.com"
            leftIcon={<Mail className="w-4 h-4" />}
            error={errors.email?.message}
            {...register('email')}
          />
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-300 mb-1">
            Password (min 8 chars, 1 uppercase, 1 number)
          </label>
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
          Create Account
        </Button>
      </form>

      <div className="text-center text-xs text-slate-400">
        Already have an account?{' '}
        <NavLink
          to="/login"
          className="text-indigo-400 hover:text-indigo-300 font-medium"
        >
          Sign in
        </NavLink>
      </div>
    </div>
  );
};

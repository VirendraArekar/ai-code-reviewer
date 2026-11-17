import React, { useState } from 'react';
import { User, Lock, Shield, CheckCircle2, Save } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { useAppSelector } from '@/store';
import { notify } from '@/lib/notify';
import { useChangePasswordMutation, useUpdateProfileMutation } from '@/api/authApi';

export const ProfilePage: React.FC = () => {
  const { user } = useAppSelector((state) => state.auth);
  const [updateProfile, { isLoading: isUpdatingProfile }] = useUpdateProfileMutation();
  const [changePassword, { isLoading: isChangingPw }] = useChangePasswordMutation();

  const [fullName, setFullName] = useState(user?.fullName || 'Virendra Arekar');
  const [email] = useState(user?.email || 'admin@aicode.com');
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [feedback, setFeedback] = useState<string | null>(null);

  const handleProfileSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFeedback(null);
    try {
      await updateProfile({ fullName }).unwrap();
      setFeedback('Profile details saved successfully.');
      notify.success('Profile Updated', 'Profile details have been saved.');
    } catch (err: any) {
      setFeedback('Failed to update profile.');
      notify.error('Update Failed', err?.data?.message || 'Failed to update profile.');
    }
  };

  const handlePasswordSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFeedback(null);
    if (newPassword !== confirmPassword) {
      setFeedback('New passwords do not match.');
      notify.warning('Password Mismatch', 'The new passwords entered do not match.');
      return;
    }
    try {
      await changePassword({ currentPassword, newPassword }).unwrap();
      setFeedback('Password changed successfully.');
      notify.success('Password Changed', 'Your account password has been updated.');
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err: any) {
      setFeedback('Failed to change password.');
      notify.error('Password Update Failed', err?.data?.message || 'Failed to change password.');
    }
  };

  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
          User Profile & Security <User className="w-5 h-5 text-indigo-400" />
        </h1>
        <p className="text-xs sm:text-sm text-slate-400 mt-1">
          Manage your account credentials, security settings, and personal details
        </p>
      </div>

      {feedback && (
        <div className="p-3 rounded-xl bg-indigo-500/10 border border-indigo-500/30 text-indigo-400 text-xs font-semibold flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4" /> {feedback}
        </div>
      )}

      {/* Profile Info */}
      <Card className="p-6 space-y-4">
        <CardTitle>Personal Information</CardTitle>
        <form onSubmit={handleProfileSubmit} className="space-y-4">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Full Name
              </label>
              <Input
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                required
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Email Address
              </label>
              <Input value={email} disabled className="opacity-60" />
            </div>
          </div>

          <div className="flex justify-end">
            <Button
              type="submit"
              variant="primary"
              size="sm"
              isLoading={isUpdatingProfile}
              leftIcon={<Save className="w-3.5 h-3.5" />}
            >
              Update Profile
            </Button>
          </div>
        </form>
      </Card>

      {/* Password Change */}
      <Card className="p-6 space-y-4">
        <CardTitle>Change Password</CardTitle>
        <form onSubmit={handlePasswordSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Current Password
            </label>
            <Input
              type="password"
              placeholder="••••••••••••"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              required
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                New Password
              </label>
              <Input
                type="password"
                placeholder="••••••••••••"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Confirm New Password
              </label>
              <Input
                type="password"
                placeholder="••••••••••••"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="flex justify-end">
            <Button
              type="submit"
              variant="outline"
              size="sm"
              isLoading={isChangingPw}
              leftIcon={<Lock className="w-3.5 h-3.5" />}
            >
              Save New Password
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
};

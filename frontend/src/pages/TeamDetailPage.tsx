import React, { useState } from 'react';
import { useParams, NavLink } from 'react-router-dom';
import { Users2, ArrowLeft, Plus, Mail, Shield, UserX, FolderGit2 } from 'lucide-react';
import { Card, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Modal } from '@/components/ui/Modal';
import { Input, Select } from '@/components/ui/Input';

export const TeamDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviteRole, setInviteRole] = useState('DEVELOPER');

  const members = [
    { id: 'm-1', name: 'Alex Rivera', email: 'alex@aicode.com', role: 'TEAM_LEAD' },
    { id: 'm-2', name: 'Sarah Chen', email: 'sarah@aicode.com', role: 'DEVELOPER' },
    { id: 'm-3', name: 'David Kim', email: 'david@aicode.com', role: 'DEVELOPER' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <NavLink to="/teams">
          <Button variant="ghost" size="icon">
            <ArrowLeft className="w-4 h-4" />
          </Button>
        </NavLink>
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            Backend Platform Squad <Users2 className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs text-slate-400 mt-0.5">
            Squad members, repository assignments, and team review settings
          </p>
        </div>
      </div>

      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold text-white">Squad Members ({members.length})</h3>
        <Button
          variant="primary"
          size="sm"
          onClick={() => setIsInviteModalOpen(true)}
          leftIcon={<Plus className="w-4 h-4" />}
        >
          Invite Member
        </Button>
      </div>

      <Card className="overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-950/80 text-slate-400 border-b border-slate-800 uppercase font-semibold">
              <tr>
                <th className="py-3 px-4">Member</th>
                <th className="py-3 px-4">Email</th>
                <th className="py-3 px-4">Role</th>
                <th className="py-3 px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800 text-slate-200">
              {members.map((m) => (
                <tr key={m.id} className="hover:bg-slate-800/40">
                  <td className="py-3 px-4 font-semibold text-white flex items-center gap-2">
                    <div className="w-7 h-7 rounded-full bg-indigo-600 flex items-center justify-center text-white font-bold text-[10px]">
                      {m.name.charAt(0)}
                    </div>
                    <span>{m.name}</span>
                  </td>
                  <td className="py-3 px-4 text-slate-400 font-mono">{m.email}</td>
                  <td className="py-3 px-4">
                    <span className="px-2 py-0.5 rounded bg-slate-800 text-indigo-300 font-semibold text-[10px]">
                      {m.role}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-right">
                    <button className="text-slate-400 hover:text-rose-400 transition-colors">
                      <UserX className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>

      <Modal
        isOpen={isInviteModalOpen}
        onClose={() => setIsInviteModalOpen(false)}
        title="Invite New Member"
        description="Send an invitation link to join this team"
      >
        <form
          onSubmit={(e) => {
            e.preventDefault();
            setIsInviteModalOpen(false);
          }}
          className="space-y-4"
        >
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Email Address
            </label>
            <Input
              type="email"
              placeholder="colleague@company.com"
              value={inviteEmail}
              onChange={(e) => setInviteEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Team Role
            </label>
            <Select value={inviteRole} onChange={(e) => setInviteRole(e.target.value)}>
              <option value="DEVELOPER">Developer</option>
              <option value="TEAM_LEAD">Team Lead</option>
              <option value="VIEWER">Viewer</option>
            </Select>
          </div>

          <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-800">
            <Button variant="ghost" type="button" onClick={() => setIsInviteModalOpen(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Send Invitation
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

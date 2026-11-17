import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { Users2, Plus, Mail, Shield, UserX, ChevronRight } from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Modal } from '@/components/ui/Modal';
import { Input } from '@/components/ui/Input';
import { useGetTeamsQuery, useCreateTeamMutation } from '@/api/settingsApi';
import { Team } from '@/types';

export const TeamsPage: React.FC = () => {
  const { data: teamsList, refetch } = useGetTeamsQuery();
  const [createTeam, { isLoading: isCreating }] = useCreateTeamMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [teamName, setTeamName] = useState('');
  const [teamDesc, setTeamDesc] = useState('');

  const teams = teamsList || [];

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!teamName) return;
    try {
      await createTeam({ name: teamName, description: teamDesc }).unwrap();
      setIsModalOpen(false);
      setTeamName('');
      setTeamDesc('');
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            Teams & Collaboration <Users2 className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Organize engineering squads, manage repository ownership, and set team review policies
          </p>
        </div>

        <Button
          variant="primary"
          size="sm"
          onClick={() => setIsModalOpen(true)}
          leftIcon={<Plus className="w-4 h-4" />}
        >
          Create Team
        </Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {teams.map((team) => (
          <Card key={team.id} glow className="p-6 space-y-4">
            <div className="flex items-start justify-between">
              <div>
                <NavLink
                  to={`/teams/${team.id}`}
                  className="text-lg font-semibold text-white hover:text-indigo-400 flex items-center gap-2"
                >
                  {team.name} <ChevronRight className="w-4 h-4 text-slate-500" />
                </NavLink>
                <p className="text-xs text-slate-400 mt-1">{team.description}</p>
              </div>
            </div>

            <div className="pt-3 border-t border-slate-800 flex items-center justify-between text-xs text-slate-400">
              <div className="flex items-center gap-4">
                <span>
                  <strong className="text-white font-mono">{team.memberCount}</strong> members
                </span>
                <span>
                  <strong className="text-white font-mono">{team.repositoryCount}</strong> repos
                </span>
              </div>
              <NavLink to={`/teams/${team.id}`}>
                <Button variant="ghost" size="sm">
                  Manage Squad
                </Button>
              </NavLink>
            </div>
          </Card>
        ))}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Create New Team"
        description="Group developers and assign specific repositories for automatic code reviews"
      >
        <form onSubmit={handleCreate} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Team Name
            </label>
            <Input
              placeholder="e.g. Infrastructure Squad"
              value={teamName}
              onChange={(e) => setTeamName(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">
              Description
            </label>
            <Input
              placeholder="e.g. Kubernetes, CI/CD pipelines, and cloud security"
              value={teamDesc}
              onChange={(e) => setTeamDesc(e.target.value)}
            />
          </div>

          <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-800">
            <Button variant="ghost" type="button" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" isLoading={isCreating}>
              Create Team
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

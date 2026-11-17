import React from 'react';
import { NavLink } from 'react-router-dom';
import { Bell, CheckCircle2, ShieldAlert, AlertTriangle, ArrowRight } from 'lucide-react';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { useGetNotificationsQuery, useMarkNotificationAsReadMutation } from '@/api/settingsApi';
import { NotificationItem } from '@/types';

export const NotificationsPage: React.FC = () => {
  const { data: notificationsData, refetch } = useGetNotificationsQuery();
  const [markAsRead] = useMarkNotificationAsReadMutation();

  const fallbackNotifications: NotificationItem[] = [
    {
      id: 'notif-1',
      userId: 'u-1',
      title: 'Review Completed for PR #42',
      message: 'Autonomous review generated 3 findings (1 High, 2 Medium) for api-gateway.',
      type: 'REVIEW_COMPLETED',
      read: false,
      actionUrl: '/pull-requests/pr-1',
      createdAt: '2026-11-17T08:45:00Z',
    },
    {
      id: 'notif-2',
      userId: 'u-1',
      title: 'Critical Timing Attack Vulnerability Detected',
      message: 'PR #15 in payment-service has a critical non-constant time signature validation.',
      type: 'CRITICAL_VULNERABILITY',
      read: false,
      actionUrl: '/pull-requests/pr-3',
      createdAt: '2026-11-15T12:00:00Z',
    },
    {
      id: 'notif-3',
      userId: 'u-1',
      title: 'Team Invitation Accepted',
      message: 'Sarah Chen has joined the Backend Platform Squad.',
      type: 'TEAM_INVITATION',
      read: true,
      actionUrl: '/teams/team-1',
      createdAt: '2026-11-14T10:00:00Z',
    },
  ];

  const list = notificationsData && notificationsData.length > 0 ? notificationsData : fallbackNotifications;

  const handleRead = async (id: string) => {
    try {
      await markAsRead(id).unwrap();
      refetch();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            Notification Center <Bell className="w-5 h-5 text-indigo-400" />
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Real-time alerts for completed code reviews, critical security warnings, and team events
          </p>
        </div>
      </div>

      <div className="space-y-3">
        {list.map((n) => (
          <Card
            key={n.id}
            className={`p-4 flex items-start justify-between gap-4 transition-all ${
              !n.read ? 'bg-slate-900/90 border-indigo-500/40' : 'bg-slate-950/60 border-slate-800'
            }`}
          >
            <div className="flex items-start gap-3">
              <div
                className={`p-2 rounded-xl mt-0.5 ${
                  n.type === 'CRITICAL_VULNERABILITY'
                    ? 'bg-rose-500/10 text-rose-400'
                    : 'bg-indigo-500/10 text-indigo-400'
                }`}
              >
                {n.type === 'CRITICAL_VULNERABILITY' ? (
                  <ShieldAlert className="w-4 h-4" />
                ) : (
                  <Bell className="w-4 h-4" />
                )}
              </div>

              <div>
                <h4 className="text-sm font-semibold text-white">{n.title}</h4>
                <p className="text-xs text-slate-400 mt-0.5">{n.message}</p>
              </div>
            </div>

            <div className="flex items-center gap-3 shrink-0">
              {n.actionUrl && (
                <NavLink to={n.actionUrl}>
                  <Button variant="outline" size="sm" rightIcon={<ArrowRight className="w-3.5 h-3.5" />}>
                    View
                  </Button>
                </NavLink>
              )}
              {!n.read && (
                <Button variant="ghost" size="sm" onClick={() => handleRead(n.id)}>
                  Mark Read
                </Button>
              )}
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

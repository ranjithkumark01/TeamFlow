import { CheckCircle2 } from 'lucide-react';
import { updateResource } from '../api/resources';
import useApiPage from '../api/useApiPage';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import Pagination from '../components/Pagination';

export default function Notifications() {
  const notifications = useApiPage('/notifications');

  const markRead = async (notification) => {
    await updateResource('/notifications', notification.id, { ...notification, recipientId: notification.recipientId, read: true, readAt: new Date().toISOString() });
    notifications.reload();
  };

  return (
    <>
      <Header title="Notifications" description="Assignments, updates, comments, invitations, and review requests." />
      <section className="space-y-4 p-4 sm:p-6">
        <ErrorBanner message={notifications.error} />
        {notifications.loading ? <LoadingSpinner label="Loading notifications" /> : notifications.content.length === 0 ? <EmptyState title="No notifications" /> : (
          <div className="space-y-3">
            {notifications.content.map((notification) => (
              <article key={notification.id} className="flex items-start justify-between gap-4 border border-slate-200 bg-white p-4 dark:border-slate-800 dark:bg-slate-900">
                <div>
                  <div className="flex flex-wrap items-center gap-2">
                    <h2 className="font-semibold text-slate-950 dark:text-white">{notification.title}</h2>
                    {!notification.read && <span className="bg-cyan-100 px-2 py-1 text-xs font-bold text-cyan-700 dark:bg-cyan-950 dark:text-cyan-200">Unread</span>}
                  </div>
                  <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">{notification.message}</p>
                  <p className="mt-2 text-xs text-slate-400">{notification.type}</p>
                </div>
                {!notification.read && (
                  <button onClick={() => markRead(notification)} className="inline-flex h-9 w-9 shrink-0 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200" title="Mark as read">
                    <CheckCircle2 className="h-4 w-4" />
                  </button>
                )}
              </article>
            ))}
          </div>
        )}
        <Pagination page={notifications.page} totalPages={notifications.totalPages} onPageChange={notifications.setPage} />
      </section>
    </>
  );
}


import useApiPage from '../api/useApiPage';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import { useTheme } from '../contexts/ThemeContext';

export default function Settings() {
  const { isDark, toggleTheme } = useTheme();
  const notifications = useApiPage('/notifications', { size: 5 });

  return (
    <>
      <Header title="Settings" description="Workspace preferences and notification visibility." />
      <section className="space-y-6 p-4 sm:p-6">
        <ErrorBanner message={notifications.error} />
        <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
          <div className="flex items-center justify-between gap-4">
            <div>
              <h2 className="font-semibold text-slate-950 dark:text-white">Theme</h2>
              <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Current mode: {isDark ? 'Dark' : 'Light'}</p>
            </div>
            <button onClick={toggleTheme} className="h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Toggle</button>
          </div>
        </section>
        <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
          <h2 className="font-semibold text-slate-950 dark:text-white">Recent notification types</h2>
          {notifications.loading ? <LoadingSpinner label="Loading preferences" /> : (
            <div className="mt-3 flex flex-wrap gap-2">
              {notifications.content.length === 0 ? <span className="text-sm text-slate-500">No notifications available.</span> : notifications.content.map((item) => (
                <span key={item.id} className="bg-slate-100 px-2 py-1 text-xs font-semibold text-slate-700 dark:bg-slate-800 dark:text-slate-200">{item.type}</span>
              ))}
            </div>
          )}
        </section>
      </section>
    </>
  );
}


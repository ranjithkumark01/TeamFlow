import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import api, { getErrorMessage, unwrap } from '../api/client';
import useApiPage from '../api/useApiPage';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import ProjectCard from '../components/ProjectCard';
import TaskCard from '../components/TaskCard';

export default function Dashboard() {
  const projects = useApiPage('/projects', { size: 6 });
  const tasks = useApiPage('/tasks', { size: 6 });
  const notifications = useApiPage('/notifications', { size: 5 });
  const [analytics, setAnalytics] = useState(null);
  const [analyticsError, setAnalyticsError] = useState('');

  useEffect(() => {
    api.get('/analytics/dashboard')
      .then(unwrap)
      .then(setAnalytics)
      .catch((err) => setAnalyticsError(getErrorMessage(err)));
  }, []);

  const loading = projects.loading || tasks.loading || notifications.loading;
  const openTasks = tasks.content.filter((task) => task.status !== 'DONE').length;

  return (
    <>
      <Header title="Dashboard" description="Workspace activity, open work, and delivery signals." />
      <section className="space-y-6 p-4 sm:p-6">
        {(analyticsError || projects.error || tasks.error || notifications.error) && <ErrorBanner message={analyticsError || projects.error || tasks.error || notifications.error} />}
        {loading ? <LoadingSpinner label="Loading dashboard" /> : (
          <>
            <div className="grid gap-4 md:grid-cols-3">
              <Metric label="Projects" value={analytics?.totalProjects ?? projects.totalElements} />
              <Metric label="Open tasks" value={analytics?.openTasks ?? openTasks} />
              <Metric label="Unread alerts" value={notifications.content.filter((item) => !item.read).length} />
            </div>
            <div className="grid gap-6 xl:grid-cols-[1.2fr_0.8fr]">
              <section>
                <SectionTitle title="Recent projects" to="/projects" />
                {projects.content.length === 0 ? <EmptyState title="No projects" message="Projects created through the API will appear here." /> : (
                  <div className="grid gap-4 md:grid-cols-2">
                    {projects.content.slice(0, 4).map((project) => (
                      <ProjectCard key={project.id} project={project} taskCount={tasks.content.filter((task) => task.projectId === project.id).length} />
                    ))}
                  </div>
                )}
              </section>
              <section>
                <SectionTitle title="Current tasks" to="/kanban" />
                {tasks.content.length === 0 ? <EmptyState title="No tasks" /> : (
                  <div className="space-y-3">
                    {tasks.content.slice(0, 4).map((task) => <TaskCard key={task.id} task={task} />)}
                  </div>
                )}
              </section>
            </div>
          </>
        )}
      </section>
    </>
  );
}

function Metric({ label, value }) {
  return (
    <div className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
      <p className="text-sm text-slate-500 dark:text-slate-400">{label}</p>
      <p className="mt-2 text-3xl font-bold text-slate-950 dark:text-white">{value}</p>
    </div>
  );
}

function SectionTitle({ title, to }) {
  return (
    <div className="mb-3 flex items-center justify-between">
      <h2 className="text-lg font-semibold text-slate-950 dark:text-white">{title}</h2>
      <Link className="text-sm font-semibold text-cyan-600 dark:text-cyan-300" to={to}>View all</Link>
    </div>
  );
}

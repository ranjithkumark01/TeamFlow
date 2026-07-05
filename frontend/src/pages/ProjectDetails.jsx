import { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getErrorMessage } from '../api/client';
import { getResource } from '../api/resources';
import useApiPage from '../api/useApiPage';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import TaskCard from '../components/TaskCard';

export default function ProjectDetails() {
  const { id } = useParams();
  const [project, setProject] = useState(null);
  const [loadingProject, setLoadingProject] = useState(true);
  const [error, setError] = useState('');
  const tasks = useApiPage('/tasks', { size: 200 });
  const members = useApiPage('/project-members', { size: 200 });

  useEffect(() => {
    setLoadingProject(true);
    getResource('/projects', id)
      .then(setProject)
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoadingProject(false));
  }, [id]);

  const projectTasks = useMemo(() => tasks.content.filter((task) => task.projectId === Number(id)), [tasks.content, id]);
  const projectMembers = useMemo(() => members.content.filter((member) => member.projectId === Number(id)), [members.content, id]);

  return (
    <>
      <Header title={project?.name || 'Project details'} description={project?.description || 'Project workspace overview.'} />
      <section className="space-y-6 p-4 sm:p-6">
        <ErrorBanner message={error || tasks.error || members.error} />
        {loadingProject || tasks.loading || members.loading ? <LoadingSpinner label="Loading project" /> : (
          <div className="grid gap-6 xl:grid-cols-[1fr_320px]">
            <section>
              <h2 className="mb-3 text-lg font-semibold text-slate-950 dark:text-white">Tasks</h2>
              {projectTasks.length === 0 ? <EmptyState title="No project tasks" /> : <div className="grid gap-3 md:grid-cols-2">{projectTasks.map((task) => <TaskCard key={task.id} task={task} />)}</div>}
            </section>
            <aside className="border border-slate-200 bg-white p-4 dark:border-slate-800 dark:bg-slate-900">
              <h2 className="text-lg font-semibold text-slate-950 dark:text-white">Members</h2>
              <div className="mt-3 space-y-2">
                {projectMembers.length === 0 ? <p className="text-sm text-slate-500 dark:text-slate-400">No members assigned.</p> : projectMembers.map((member) => (
                  <div key={member.id} className="flex items-center justify-between border-b border-slate-100 py-2 text-sm dark:border-slate-800">
                    <span>User #{member.userId}</span>
                    <span className="font-semibold">{member.memberRole}</span>
                  </div>
                ))}
              </div>
            </aside>
          </div>
        )}
      </section>
    </>
  );
}


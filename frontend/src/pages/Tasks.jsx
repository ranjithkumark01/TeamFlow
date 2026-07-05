import { useEffect, useState } from 'react';
import { listResource } from '../api/resources';
import { getErrorMessage } from '../api/client';
import useApiPage from '../api/useApiPage';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import Pagination from '../components/Pagination';
import SearchBar from '../components/SearchBar';
import TaskCard from '../components/TaskCard';

export default function Tasks() {
  const [query, setQuery] = useState('');
  const [status, setStatus] = useState('');
  const [priority, setPriority] = useState('');
  const [page, setPage] = useState(0);
  const [tasks, setTasks] = useState({ content: [], totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    listResource('/tasks', { page, size: 20, query: query || undefined, status: status || undefined, priority: priority || undefined })
      .then(setTasks)
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, [query, status, priority, page]);

  return (
    <>
      <Header title="Tasks" description="Search and inspect work items across projects." />
      <section className="space-y-4 p-4 sm:p-6">
        <div className="flex flex-col gap-3 lg:flex-row">
          <SearchBar value={query} onChange={(value) => { setPage(0); setQuery(value); }} placeholder="Search tasks" />
          <select value={status} onChange={(event) => { setPage(0); setStatus(event.target.value); }} className="h-10 border border-slate-300 bg-white px-3 text-sm dark:border-slate-700 dark:bg-slate-900 dark:text-white">
            <option value="">All statuses</option>
            {['TODO', 'IN_PROGRESS', 'BLOCKED', 'IN_REVIEW', 'DONE', 'CANCELLED'].map((item) => <option key={item}>{item}</option>)}
          </select>
          <select value={priority} onChange={(event) => { setPage(0); setPriority(event.target.value); }} className="h-10 border border-slate-300 bg-white px-3 text-sm dark:border-slate-700 dark:bg-slate-900 dark:text-white">
            <option value="">All priorities</option>
            {['LOW', 'MEDIUM', 'HIGH', 'URGENT'].map((item) => <option key={item}>{item}</option>)}
          </select>
        </div>
        <ErrorBanner message={error} />
        {loading ? <LoadingSpinner label="Loading tasks" /> : tasks.content.length === 0 ? <EmptyState title="No tasks found" /> : (
          <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
            {tasks.content.map((task) => <TaskCard key={task.id} task={task} />)}
          </div>
        )}
        <Pagination page={page} totalPages={tasks.totalPages} onPageChange={setPage} />
      </section>
    </>
  );
}

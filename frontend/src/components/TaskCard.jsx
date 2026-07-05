import { Link } from 'react-router-dom';
import Badge from './Badge';

export default function TaskCard({ task }) {
  return (
    <article className="border border-slate-200 bg-white p-4 dark:border-slate-800 dark:bg-slate-900">
      <div className="flex items-start justify-between gap-3">
        <Link to={`/tasks/${task.id}`} className="min-w-0 text-sm font-semibold text-slate-950 hover:text-cyan-600 dark:text-white dark:hover:text-cyan-300">
          {task.title}
        </Link>
        <Badge tone={task.priority}>{task.priority}</Badge>
      </div>
      <p className="mt-2 line-clamp-2 text-sm text-slate-500 dark:text-slate-400">{task.description || 'No description provided.'}</p>
      <div className="mt-4 flex flex-wrap items-center gap-2">
        <Badge tone={task.status}>{task.status}</Badge>
        {task.dueDate && <span className="text-xs text-slate-500 dark:text-slate-400">Due {task.dueDate}</span>}
      </div>
    </article>
  );
}


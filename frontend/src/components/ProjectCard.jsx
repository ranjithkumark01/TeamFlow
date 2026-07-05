import { ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function ProjectCard({ project, taskCount = 0 }) {
  return (
    <article className="border border-slate-200 bg-white p-4 dark:border-slate-800 dark:bg-slate-900">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h3 className="text-base font-semibold text-slate-950 dark:text-white">{project.name}</h3>
          <p className="mt-2 line-clamp-2 text-sm text-slate-500 dark:text-slate-400">{project.description || 'No description yet.'}</p>
        </div>
        <Link to={`/projects/${project.id}`} className="inline-flex h-9 w-9 shrink-0 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200" title="Open project">
          <ArrowRight className="h-4 w-4" />
        </Link>
      </div>
      <div className="mt-5 flex items-center justify-between text-sm text-slate-500 dark:text-slate-400">
        <span>{taskCount} tasks</span>
        <span>Owner #{project.createdById}</span>
      </div>
    </article>
  );
}


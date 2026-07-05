import { Inbox } from 'lucide-react';

export default function EmptyState({ title = 'Nothing here yet', message = 'New items will appear here when they are available.' }) {
  return (
    <div className="flex min-h-44 flex-col items-center justify-center border border-dashed border-slate-300 bg-white p-6 text-center dark:border-slate-700 dark:bg-slate-900">
      <Inbox className="h-8 w-8 text-slate-400" />
      <h3 className="mt-3 text-base font-semibold text-slate-900 dark:text-white">{title}</h3>
      <p className="mt-1 max-w-md text-sm text-slate-500 dark:text-slate-400">{message}</p>
    </div>
  );
}


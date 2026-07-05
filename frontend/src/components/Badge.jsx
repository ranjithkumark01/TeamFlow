const styles = {
  TODO: 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200',
  IN_PROGRESS: 'bg-blue-100 text-blue-700 dark:bg-blue-950 dark:text-blue-200',
  BLOCKED: 'bg-rose-100 text-rose-700 dark:bg-rose-950 dark:text-rose-200',
  IN_REVIEW: 'bg-amber-100 text-amber-700 dark:bg-amber-950 dark:text-amber-200',
  DONE: 'bg-emerald-100 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-200',
  CANCELLED: 'bg-slate-200 text-slate-700 dark:bg-slate-700 dark:text-slate-200',
  LOW: 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200',
  MEDIUM: 'bg-cyan-100 text-cyan-700 dark:bg-cyan-950 dark:text-cyan-200',
  HIGH: 'bg-orange-100 text-orange-700 dark:bg-orange-950 dark:text-orange-200',
  URGENT: 'bg-rose-100 text-rose-700 dark:bg-rose-950 dark:text-rose-200'
};

export default function Badge({ children, tone }) {
  return (
    <span className={`inline-flex items-center px-2 py-1 text-xs font-semibold ${styles[tone] || styles.TODO}`}>
      {String(children).replaceAll('_', ' ')}
    </span>
  );
}


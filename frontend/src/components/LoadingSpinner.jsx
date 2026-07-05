export default function LoadingSpinner({ label = 'Loading' }) {
  return (
    <div className="flex min-h-40 items-center justify-center gap-3 text-slate-600 dark:text-slate-300">
      <div className="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-cyan-500" />
      <span className="text-sm font-medium">{label}</span>
    </div>
  );
}


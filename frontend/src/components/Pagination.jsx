import { ChevronLeft, ChevronRight } from 'lucide-react';

export default function Pagination({ page, totalPages, onPageChange }) {
  if (!totalPages || totalPages <= 1) return null;
  return (
    <div className="flex items-center justify-end gap-2">
      <button
        type="button"
        onClick={() => onPageChange(Math.max(0, page - 1))}
        disabled={page === 0}
        className="inline-flex h-9 w-9 items-center justify-center border border-slate-300 text-slate-700 disabled:opacity-40 dark:border-slate-700 dark:text-slate-200"
        title="Previous page"
      >
        <ChevronLeft className="h-4 w-4" />
      </button>
      <span className="text-sm text-slate-600 dark:text-slate-300">Page {page + 1} of {totalPages}</span>
      <button
        type="button"
        onClick={() => onPageChange(Math.min(totalPages - 1, page + 1))}
        disabled={page >= totalPages - 1}
        className="inline-flex h-9 w-9 items-center justify-center border border-slate-300 text-slate-700 disabled:opacity-40 dark:border-slate-700 dark:text-slate-200"
        title="Next page"
      >
        <ChevronRight className="h-4 w-4" />
      </button>
    </div>
  );
}


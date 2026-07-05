import { Search } from 'lucide-react';

export default function SearchBar({ value, onChange, placeholder = 'Search' }) {
  return (
    <label className="relative block w-full max-w-sm">
      <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
      <input
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
        className="h-10 w-full border border-slate-300 bg-white pl-9 pr-3 text-sm outline-none transition focus:border-cyan-500 dark:border-slate-700 dark:bg-slate-900 dark:text-white"
      />
    </label>
  );
}


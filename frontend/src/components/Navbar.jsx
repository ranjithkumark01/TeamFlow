import { LogOut, Menu, Moon, Sun } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useTheme } from '../contexts/ThemeContext';
import NotificationBell from './NotificationBell';

export default function Navbar({ onMenuClick }) {
  const { user, logout } = useAuth();
  const { isDark, toggleTheme } = useTheme();

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-slate-200 bg-white px-4 dark:border-slate-800 dark:bg-slate-950">
      <div className="flex items-center gap-3">
        <button type="button" onClick={onMenuClick} className="inline-flex h-10 w-10 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200 lg:hidden" title="Open navigation">
          <Menu className="h-5 w-5" />
        </button>
        <div>
          <p className="text-sm font-bold text-slate-950 dark:text-white">TeamFlow</p>
          <p className="text-xs text-slate-500 dark:text-slate-400">{user?.role || 'Workspace'}</p>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <NotificationBell />
        <button type="button" onClick={toggleTheme} className="inline-flex h-10 w-10 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200" title="Toggle theme">
          {isDark ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />}
        </button>
        <button type="button" onClick={() => logout()} className="inline-flex h-10 w-10 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200" title="Logout">
          <LogOut className="h-5 w-5" />
        </button>
      </div>
    </header>
  );
}


import { BarChart3, CalendarDays, ClipboardList, Gauge, Kanban, ListTodo, Settings, User, X, Bell, SearchCheck } from 'lucide-react';
import { NavLink } from 'react-router-dom';

const navItems = [
  { to: '/', label: 'Dashboard', icon: Gauge },
  { to: '/projects', label: 'Projects', icon: ClipboardList },
  { to: '/kanban', label: 'Kanban', icon: Kanban },
  { to: '/calendar', label: 'Calendar', icon: CalendarDays },
  { to: '/tasks', label: 'Tasks', icon: ListTodo },
  { to: '/notifications', label: 'Notifications', icon: Bell },
  { to: '/rca', label: 'RCA', icon: SearchCheck },
  { to: '/reports', label: 'Reports', icon: BarChart3 },
  { to: '/settings', label: 'Settings', icon: Settings },
  { to: '/profile', label: 'Profile', icon: User }
];

export default function Sidebar({ open, onClose }) {
  return (
    <>
      {open && <button type="button" aria-label="Close navigation overlay" onClick={onClose} className="fixed inset-0 z-40 bg-slate-950/50 lg:hidden" />}
      <aside className={`fixed inset-y-0 left-0 z-50 w-72 border-r border-slate-200 bg-white transition-transform dark:border-slate-800 dark:bg-slate-950 lg:static lg:z-auto lg:translate-x-0 ${open ? 'translate-x-0' : '-translate-x-full'}`}>
        <div className="flex h-16 items-center justify-between border-b border-slate-200 px-5 dark:border-slate-800">
          <span className="text-lg font-bold text-slate-950 dark:text-white">TeamFlow</span>
          <button type="button" onClick={onClose} className="inline-flex h-9 w-9 items-center justify-center text-slate-500 lg:hidden" title="Close navigation">
            <X className="h-5 w-5" />
          </button>
        </div>
        <nav className="space-y-1 p-3">
          {navItems.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end={to === '/'}
              onClick={onClose}
              className={({ isActive }) => `flex h-11 items-center gap-3 px-3 text-sm font-medium transition ${isActive ? 'bg-slate-950 text-white dark:bg-white dark:text-slate-950' : 'text-slate-600 hover:bg-slate-100 dark:text-slate-300 dark:hover:bg-slate-900'}`}
            >
              <Icon className="h-4 w-4" />
              {label}
            </NavLink>
          ))}
        </nav>
      </aside>
    </>
  );
}


import { Bell } from 'lucide-react';
import { useEffect, useState } from 'react';
import { listResource } from '../api/resources';

export default function NotificationBell() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    let active = true;
    listResource('/notifications', { page: 0, size: 20 })
      .then((data) => {
        if (active) setCount((data.content || []).filter((item) => !item.read).length);
      })
      .catch(() => {
        if (active) setCount(0);
      });
    return () => {
      active = false;
    };
  }, []);

  return (
    <button type="button" className="relative inline-flex h-10 w-10 items-center justify-center border border-slate-300 text-slate-700 dark:border-slate-700 dark:text-slate-200" title="Notifications">
      <Bell className="h-5 w-5" />
      {count > 0 && (
        <span className="absolute -right-1 -top-1 flex h-5 min-w-5 items-center justify-center bg-rose-600 px-1 text-xs font-bold text-white">
          {count}
        </span>
      )}
    </button>
  );
}


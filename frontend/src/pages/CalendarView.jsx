import useApiPage from '../api/useApiPage';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import TaskCard from '../components/TaskCard';

export default function CalendarView() {
  const tasks = useApiPage('/tasks', { size: 200 });
  const datedTasks = tasks.content.filter((task) => task.dueDate).sort((a, b) => a.dueDate.localeCompare(b.dueDate));
  const grouped = datedTasks.reduce((acc, task) => {
    acc[task.dueDate] = [...(acc[task.dueDate] || []), task];
    return acc;
  }, {});

  return (
    <>
      <Header title="Calendar View" description="Task due dates grouped by day." />
      <section className="space-y-4 p-4 sm:p-6">
        <ErrorBanner message={tasks.error} />
        {tasks.loading ? <LoadingSpinner label="Loading calendar" /> : datedTasks.length === 0 ? <EmptyState title="No scheduled tasks" message="Tasks with due dates will appear here." /> : (
          <div className="space-y-4">
            {Object.entries(grouped).map(([date, items]) => (
              <section key={date} className="border border-slate-200 bg-white p-4 dark:border-slate-800 dark:bg-slate-900">
                <h2 className="mb-3 text-sm font-bold text-slate-700 dark:text-slate-200">{date}</h2>
                <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
                  {items.map((task) => <TaskCard key={task.id} task={task} />)}
                </div>
              </section>
            ))}
          </div>
        )}
      </section>
    </>
  );
}


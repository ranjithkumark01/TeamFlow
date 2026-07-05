import useApiPage from '../api/useApiPage';
import { downloadCsv } from '../api/resources';
import ErrorBanner from '../components/ErrorBanner';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';

export default function Reports() {
  const projects = useApiPage('/projects', { size: 200 });
  const tasks = useApiPage('/tasks', { size: 200 });
  const users = useApiPage('/users', { size: 200 });
  const rcas = useApiPage('/root-cause-analyses', { size: 200 });
  const loading = projects.loading || tasks.loading || users.loading || rcas.loading;
  const done = tasks.content.filter((task) => task.status === 'DONE').length;
  const blocked = tasks.content.filter((task) => task.status === 'BLOCKED').length;

  const exportCsv = async (path, fileName) => {
    const blob = await downloadCsv(path);
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    link.click();
    URL.revokeObjectURL(url);
  };

  return (
    <>
      <Header
        title="Reports"
        description="Operational summaries from projects, tasks, users, and RCA records."
        action={<div className="flex flex-wrap gap-2">
          <button onClick={() => exportCsv('/exports/tasks.csv', 'teamflow-tasks.csv')} className="h-10 border border-slate-300 px-3 text-sm font-semibold dark:border-slate-700">Tasks CSV</button>
          <button onClick={() => exportCsv('/exports/projects.csv', 'teamflow-projects.csv')} className="h-10 border border-slate-300 px-3 text-sm font-semibold dark:border-slate-700">Projects CSV</button>
          <button onClick={() => exportCsv('/exports/root-cause-analyses.csv', 'teamflow-rca.csv')} className="h-10 border border-slate-300 px-3 text-sm font-semibold dark:border-slate-700">RCA CSV</button>
        </div>}
      />
      <section className="space-y-6 p-4 sm:p-6">
        <ErrorBanner message={projects.error || tasks.error || users.error || rcas.error} />
        {loading ? <LoadingSpinner label="Loading reports" /> : (
          <>
            <div className="grid gap-4 md:grid-cols-4">
              <ReportTile label="Projects" value={projects.totalElements} />
              <ReportTile label="Users" value={users.totalElements} />
              <ReportTile label="Tasks done" value={done} />
              <ReportTile label="Blocked tasks" value={blocked} />
            </div>
            <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
              <h2 className="text-lg font-semibold text-slate-950 dark:text-white">Task status mix</h2>
              <div className="mt-4 space-y-3">
                {['TODO', 'IN_PROGRESS', 'BLOCKED', 'IN_REVIEW', 'DONE', 'CANCELLED'].map((status) => {
                  const count = tasks.content.filter((task) => task.status === status).length;
                  const width = tasks.content.length ? `${Math.round((count / tasks.content.length) * 100)}%` : '0%';
                  return (
                    <div key={status}>
                      <div className="mb-1 flex justify-between text-sm text-slate-600 dark:text-slate-300"><span>{status.replaceAll('_', ' ')}</span><span>{count}</span></div>
                      <div className="h-2 bg-slate-100 dark:bg-slate-800"><div className="h-2 bg-cyan-500" style={{ width }} /></div>
                    </div>
                  );
                })}
              </div>
            </section>
          </>
        )}
      </section>
    </>
  );
}

function ReportTile({ label, value }) {
  return (
    <div className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
      <p className="text-sm text-slate-500 dark:text-slate-400">{label}</p>
      <p className="mt-2 text-3xl font-bold text-slate-950 dark:text-white">{value}</p>
    </div>
  );
}

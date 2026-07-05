import { Plus } from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { createResource, updateResource } from '../api/resources';
import useApiPage from '../api/useApiPage';
import { getErrorMessage } from '../api/client';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import FormError from '../components/FormError';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import TaskCard from '../components/TaskCard';

const columns = ['TODO', 'IN_PROGRESS', 'BLOCKED', 'IN_REVIEW', 'DONE'];

export default function KanbanBoard() {
  const [modalOpen, setModalOpen] = useState(false);
  const [draggedTask, setDraggedTask] = useState(null);
  const [formError, setFormError] = useState('');
  const [boardError, setBoardError] = useState('');
  const tasks = useApiPage('/tasks', { size: 200 });
  const projects = useApiPage('/projects', { size: 200 });
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm({ defaultValues: { status: 'TODO', priority: 'MEDIUM' } });

  const createTask = async (values) => {
    setFormError('');
    try {
      await createResource('/tasks', { ...values, projectId: Number(values.projectId), assigneeId: values.assigneeId ? Number(values.assigneeId) : null });
      reset({ status: 'TODO', priority: 'MEDIUM' });
      setModalOpen(false);
      tasks.reload();
    } catch (err) {
      setFormError(getErrorMessage(err));
    }
  };

  const moveTask = async (status) => {
    if (!draggedTask || draggedTask.status === status) {
      setDraggedTask(null);
      return;
    }
    setBoardError('');
    try {
      await updateResource('/tasks', draggedTask.id, { ...draggedTask, status });
      await tasks.reload();
    } catch (err) {
      setBoardError(getErrorMessage(err));
    } finally {
      setDraggedTask(null);
    }
  };

  return (
    <>
      <Header title="Kanban Board" description="Track task status from planning through completion." action={<button onClick={() => setModalOpen(true)} className="inline-flex h-10 items-center gap-2 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950"><Plus className="h-4 w-4" />Task</button>} />
      <section className="space-y-4 p-4 sm:p-6">
        <ErrorBanner message={boardError || tasks.error || projects.error} />
        {tasks.loading || projects.loading ? <LoadingSpinner label="Loading board" /> : (
          <div className="grid gap-4 xl:grid-cols-5">
            {columns.map((status) => {
              const columnTasks = tasks.content.filter((task) => task.status === status);
              return (
                <section
                  key={status}
                  onDragOver={(event) => event.preventDefault()}
                  onDrop={() => moveTask(status)}
                  className="min-h-96 border border-slate-200 bg-slate-100 p-3 dark:border-slate-800 dark:bg-slate-900/60"
                >
                  <div className="mb-3 flex items-center justify-between">
                    <h2 className="text-sm font-bold text-slate-700 dark:text-slate-200">{status.replaceAll('_', ' ')}</h2>
                    <span className="text-xs text-slate-500">{columnTasks.length}</span>
                  </div>
                  <div className="space-y-3">
                    {columnTasks.length === 0 ? <EmptyState title="No tasks" message="This lane is clear." /> : columnTasks.map((task) => (
                      <div key={task.id} draggable onDragStart={() => setDraggedTask(task)} className="cursor-grab active:cursor-grabbing">
                        <TaskCard task={task} />
                      </div>
                    ))}
                  </div>
                </section>
              );
            })}
          </div>
        )}
      </section>
      <Modal open={modalOpen} title="Create task" onClose={() => setModalOpen(false)}>
        <TaskForm onSubmit={handleSubmit(createTask)} register={register} errors={errors} projects={projects.content} submitting={isSubmitting} error={formError} />
      </Modal>
    </>
  );
}

function TaskForm({ onSubmit, register, errors, projects, submitting, error }) {
  return (
    <form onSubmit={onSubmit} className="space-y-4">
      <ErrorBanner message={error} />
      <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">
        Title
        <input {...register('title', { required: 'Task title is required' })} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
        <FormError message={errors.title?.message} />
      </label>
      <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">
        Project
        <select {...register('projectId', { required: 'Project is required' })} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">
          <option value="">Select project</option>
          {projects.map((project) => <option key={project.id} value={project.id}>{project.name}</option>)}
        </select>
        <FormError message={errors.projectId?.message} />
      </label>
      <div className="grid gap-3 sm:grid-cols-2">
        <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">Status<select {...register('status')} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">{columns.map((status) => <option key={status}>{status}</option>)}</select></label>
        <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">Priority<select {...register('priority')} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">{['LOW', 'MEDIUM', 'HIGH', 'URGENT'].map((priority) => <option key={priority}>{priority}</option>)}</select></label>
      </div>
      <textarea {...register('description')} rows="3" placeholder="Description" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
      <input {...register('dueDate')} type="date" className="h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
      <button disabled={submitting} className="h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Save task</button>
    </form>
  );
}

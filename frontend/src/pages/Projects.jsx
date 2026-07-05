import { Plus, Trash2 } from 'lucide-react';
import { useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { createResource, deleteResource } from '../api/resources';
import useApiPage from '../api/useApiPage';
import { getErrorMessage } from '../api/client';
import ConfirmDialog from '../components/ConfirmDialog';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import FormError from '../components/FormError';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import Pagination from '../components/Pagination';
import ProjectCard from '../components/ProjectCard';
import SearchBar from '../components/SearchBar';
import { useAuth } from '../contexts/AuthContext';

export default function Projects() {
  const { user } = useAuth();
  const [query, setQuery] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  const [formError, setFormError] = useState('');
  const projects = useApiPage('/projects');
  const tasks = useApiPage('/tasks', { size: 200 });
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm();

  const filteredProjects = useMemo(() => projects.content.filter((project) => (
    project.name.toLowerCase().includes(query.toLowerCase()) ||
    (project.description || '').toLowerCase().includes(query.toLowerCase())
  )), [projects.content, query]);

  const createProject = async (values) => {
    setFormError('');
    try {
      await createResource('/projects', { ...values, createdById: user.id });
      reset();
      setModalOpen(false);
      projects.reload();
    } catch (err) {
      setFormError(getErrorMessage(err));
    }
  };

  const confirmDelete = async () => {
    await deleteResource('/projects', deleteId);
    setDeleteId(null);
    projects.reload();
  };

  return (
    <>
      <Header
        title="Projects"
        description="Plan, inspect, and maintain TeamFlow projects."
        action={<button onClick={() => setModalOpen(true)} className="inline-flex h-10 items-center gap-2 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950"><Plus className="h-4 w-4" />Project</button>}
      />
      <section className="space-y-4 p-4 sm:p-6">
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <SearchBar value={query} onChange={setQuery} placeholder="Search projects" />
        </div>
        <ErrorBanner message={projects.error || tasks.error} />
        {projects.loading ? <LoadingSpinner label="Loading projects" /> : filteredProjects.length === 0 ? <EmptyState title="No projects found" /> : (
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
            {filteredProjects.map((project) => (
              <div key={project.id} className="relative">
                <ProjectCard project={project} taskCount={tasks.content.filter((task) => task.projectId === project.id).length} />
                <button onClick={() => setDeleteId(project.id)} className="absolute bottom-3 right-3 inline-flex h-8 w-8 items-center justify-center text-slate-400 hover:text-rose-600" title="Delete project">
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            ))}
          </div>
        )}
        <Pagination page={projects.page} totalPages={projects.totalPages} onPageChange={projects.setPage} />
      </section>
      <Modal open={modalOpen} title="Create project" onClose={() => setModalOpen(false)}>
        <form onSubmit={handleSubmit(createProject)} className="space-y-4">
          <ErrorBanner message={formError} />
          <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">
            Name
            <input {...register('name', { required: 'Project name is required' })} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
            <FormError message={errors.name?.message} />
          </label>
          <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">
            Description
            <textarea {...register('description')} rows="4" className="mt-2 w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          </label>
          <button disabled={isSubmitting} className="h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Save project</button>
        </form>
      </Modal>
      <ConfirmDialog open={Boolean(deleteId)} message="This project and its related records will be removed." onCancel={() => setDeleteId(null)} onConfirm={confirmDelete} />
    </>
  );
}


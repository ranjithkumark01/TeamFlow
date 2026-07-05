import { Plus } from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { createResource } from '../api/resources';
import { getErrorMessage } from '../api/client';
import useApiPage from '../api/useApiPage';
import Badge from '../components/Badge';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import FormError from '../components/FormError';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import Pagination from '../components/Pagination';
import { useAuth } from '../contexts/AuthContext';

const statuses = ['DRAFT', 'OPEN', 'IN_REVIEW', 'APPROVED', 'REJECTED', 'CLOSED'];

export default function RootCauseAnalysis() {
  const { user } = useAuth();
  const [modalOpen, setModalOpen] = useState(false);
  const [formError, setFormError] = useState('');
  const rcas = useApiPage('/root-cause-analyses');
  const tasks = useApiPage('/tasks', { size: 200 });
  const reviews = useApiPage('/rca-reviews', { size: 200 });
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm({ defaultValues: { status: 'DRAFT' } });
  const reviewForm = useForm({ defaultValues: { decision: 'APPROVED' } });
  const [reviewingRca, setReviewingRca] = useState(null);

  const createRca = async (values) => {
    setFormError('');
    try {
      await createResource('/root-cause-analyses', { ...values, taskId: Number(values.taskId), createdById: user.id });
      reset({ status: 'DRAFT' });
      setModalOpen(false);
      rcas.reload();
    } catch (err) {
      setFormError(getErrorMessage(err));
    }
  };

  const createReview = async (values) => {
    setFormError('');
    try {
      await createResource('/rca-reviews', { ...values, rcaId: reviewingRca.id, reviewerId: user.id });
      setReviewingRca(null);
      reviewForm.reset({ decision: 'APPROVED' });
      await Promise.all([rcas.reload(), reviews.reload()]);
    } catch (err) {
      setFormError(getErrorMessage(err));
    }
  };

  return (
    <>
      <Header title="Root Cause Analysis" description="Investigate task issues, corrective actions, and review decisions." action={<button onClick={() => setModalOpen(true)} className="inline-flex h-10 items-center gap-2 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950"><Plus className="h-4 w-4" />RCA</button>} />
      <section className="space-y-4 p-4 sm:p-6">
        <ErrorBanner message={rcas.error || tasks.error || reviews.error} />
        {rcas.loading || tasks.loading || reviews.loading ? <LoadingSpinner label="Loading RCA records" /> : rcas.content.length === 0 ? <EmptyState title="No RCA records" /> : (
          <div className="grid gap-4 lg:grid-cols-2">
            {rcas.content.map((rca) => (
              <article key={rca.id} className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
                <div className="flex items-start justify-between gap-3">
                  <h2 className="font-semibold text-slate-950 dark:text-white">Task #{rca.taskId}</h2>
                  <Badge tone={rca.status}>{rca.status}</Badge>
                </div>
                <p className="mt-3 text-sm text-slate-600 dark:text-slate-300">{rca.problemSummary}</p>
                <dl className="mt-4 grid gap-3 text-sm sm:grid-cols-2">
                  <RcaField label="Root cause" value={rca.rootCause} />
                  <RcaField label="Corrective action" value={rca.correctiveAction} />
                  <RcaField label="Preventive action" value={rca.preventiveAction} />
                  <RcaField label="Reviews" value={reviews.content.filter((review) => review.rcaId === rca.id).length} />
                </dl>
                <button onClick={() => setReviewingRca(rca)} className="mt-4 h-9 border border-slate-300 px-3 text-sm font-semibold dark:border-slate-700">Review</button>
              </article>
            ))}
          </div>
        )}
        <Pagination page={rcas.page} totalPages={rcas.totalPages} onPageChange={rcas.setPage} />
      </section>
      <Modal open={modalOpen} title="Create RCA" onClose={() => setModalOpen(false)}>
        <form onSubmit={handleSubmit(createRca)} className="space-y-4">
          <ErrorBanner message={formError} />
          <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">
            Task
            <select {...register('taskId', { required: 'Task is required' })} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">
              <option value="">Select task</option>
              {tasks.content.map((task) => <option key={task.id} value={task.id}>{task.title}</option>)}
            </select>
            <FormError message={errors.taskId?.message} />
          </label>
          <textarea {...register('problemSummary', { required: 'Problem summary is required' })} rows="3" placeholder="Problem summary" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          <FormError message={errors.problemSummary?.message} />
          <textarea {...register('rootCause')} rows="3" placeholder="Root cause" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          <textarea {...register('correctiveAction')} rows="3" placeholder="Corrective action" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          <textarea {...register('preventiveAction')} rows="3" placeholder="Preventive action" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          <select {...register('status')} className="h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">{statuses.map((status) => <option key={status}>{status}</option>)}</select>
          <button disabled={isSubmitting} className="h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Save RCA</button>
        </form>
      </Modal>
      <Modal open={Boolean(reviewingRca)} title="Review RCA" onClose={() => setReviewingRca(null)}>
        <form onSubmit={reviewForm.handleSubmit(createReview)} className="space-y-4">
          <ErrorBanner message={formError} />
          <select {...reviewForm.register('decision')} className="h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white">
            {['APPROVED', 'REJECTED', 'CHANGES_REQUESTED'].map((decision) => <option key={decision}>{decision}</option>)}
          </select>
          <textarea {...reviewForm.register('comments')} rows="4" placeholder="Review comments" className="w-full border border-slate-300 px-3 py-2 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
          <button className="h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Submit review</button>
        </form>
      </Modal>
    </>
  );
}

function RcaField({ label, value }) {
  return (
    <div>
      <dt className="text-slate-500 dark:text-slate-400">{label}</dt>
      <dd className="mt-1 text-slate-800 dark:text-slate-100">{value || 'Not set'}</dd>
    </div>
  );
}

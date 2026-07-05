import { Send, Upload } from 'lucide-react';
import { useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import { getErrorMessage } from '../api/client';
import { createResource, getResource, uploadFile } from '../api/resources';
import useApiPage from '../api/useApiPage';
import Badge from '../components/Badge';
import EmptyState from '../components/EmptyState';
import ErrorBanner from '../components/ErrorBanner';
import FormError from '../components/FormError';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../contexts/AuthContext';

export default function TaskDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const [task, setTask] = useState(null);
  const [loadingTask, setLoadingTask] = useState(true);
  const [error, setError] = useState('');
  const comments = useApiPage('/comments', { size: 200 });
  const attachments = useApiPage('/attachments', { size: 200 });
  const relations = useApiPage('/task-relations', { size: 200 });
  const commentForm = useForm();
  const attachmentForm = useForm();

  useEffect(() => {
    setLoadingTask(true);
    getResource('/tasks', id)
      .then(setTask)
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoadingTask(false));
  }, [id]);

  const taskComments = useMemo(() => comments.content.filter((comment) => comment.taskId === Number(id)), [comments.content, id]);
  const taskAttachments = useMemo(() => attachments.content.filter((item) => item.taskId === Number(id)), [attachments.content, id]);
  const taskRelations = useMemo(() => relations.content.filter((item) => item.predecessorTaskId === Number(id) || item.successorTaskId === Number(id)), [relations.content, id]);

  const addComment = async (values) => {
    await createResource('/comments', { ...values, taskId: Number(id), authorId: user.id });
    commentForm.reset();
    comments.reload();
  };

  const addAttachment = async (values) => {
    const formData = new FormData();
    formData.append('taskId', id);
    formData.append('uploadedById', user.id);
    formData.append('file', values.file[0]);
    await uploadFile(formData);
    attachmentForm.reset();
    attachments.reload();
  };

  return (
    <>
      <Header title={task?.title || 'Task details'} description="Task metadata, comments, attachments, and dependencies." />
      <section className="space-y-6 p-4 sm:p-6">
        <ErrorBanner message={error || comments.error || attachments.error || relations.error} />
        {loadingTask || comments.loading || attachments.loading || relations.loading ? <LoadingSpinner label="Loading task" /> : task && (
          <div className="grid gap-6 xl:grid-cols-[1fr_360px]">
            <section className="space-y-6">
              <div className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
                <div className="flex flex-wrap gap-2">
                  <Badge tone={task.status}>{task.status}</Badge>
                  <Badge tone={task.priority}>{task.priority}</Badge>
                </div>
                <p className="mt-4 text-sm text-slate-600 dark:text-slate-300">{task.description || 'No description provided.'}</p>
                <dl className="mt-5 grid gap-3 text-sm sm:grid-cols-3">
                  <Info label="Project" value={`#${task.projectId}`} />
                  <Info label="Assignee" value={task.assigneeId ? `#${task.assigneeId}` : 'Unassigned'} />
                  <Info label="Due" value={task.dueDate || 'No date'} />
                </dl>
              </div>
              <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
                <h2 className="text-lg font-semibold">Comments</h2>
                <form onSubmit={commentForm.handleSubmit(addComment)} className="mt-4 flex gap-2">
                  <input {...commentForm.register('content', { required: 'Comment cannot be empty' })} className="h-10 min-w-0 flex-1 border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" placeholder="Add comment" />
                  <button className="inline-flex h-10 w-10 items-center justify-center bg-slate-950 text-white dark:bg-cyan-500 dark:text-slate-950" title="Send comment"><Send className="h-4 w-4" /></button>
                </form>
                <FormError message={commentForm.formState.errors.content?.message} />
                <div className="mt-4 space-y-3">
                  {taskComments.length === 0 ? <EmptyState title="No comments" /> : taskComments.map((comment) => (
                    <div key={comment.id} className="border border-slate-100 p-3 text-sm dark:border-slate-800">
                      <p className="text-slate-700 dark:text-slate-200">{comment.content}</p>
                      <p className="mt-2 text-xs text-slate-500">Author #{comment.authorId}</p>
                    </div>
                  ))}
                </div>
              </section>
            </section>
            <aside className="space-y-6">
              <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
                <h2 className="text-lg font-semibold">Attachments</h2>
                <form onSubmit={attachmentForm.handleSubmit(addAttachment)} className="mt-4 space-y-3">
                  <input {...attachmentForm.register('file', { required: 'Choose a file' })} type="file" className="block w-full border border-slate-300 p-2 text-sm dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
                  <FormError message={attachmentForm.formState.errors.file?.message} />
                  <button className="inline-flex h-10 items-center gap-2 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950"><Upload className="h-4 w-4" />Attach</button>
                </form>
                <div className="mt-4 space-y-2">
                  {taskAttachments.length === 0 ? <p className="text-sm text-slate-500">No attachments.</p> : taskAttachments.map((file) => <a key={file.id} className="block text-sm font-medium text-cyan-600 dark:text-cyan-300" href={file.fileUrl}>{file.fileName}</a>)}
                </div>
              </section>
              <section className="border border-slate-200 bg-white p-5 dark:border-slate-800 dark:bg-slate-900">
                <h2 className="text-lg font-semibold">Relations</h2>
                <div className="mt-3 space-y-2">
                  {taskRelations.length === 0 ? <p className="text-sm text-slate-500">No task relations.</p> : taskRelations.map((relation) => (
                    <p key={relation.id} className="text-sm text-slate-600 dark:text-slate-300">#{relation.predecessorTaskId} {relation.relationType} #{relation.successorTaskId}</p>
                  ))}
                </div>
              </section>
            </aside>
          </div>
        )}
      </section>
    </>
  );
}

function Info({ label, value }) {
  return (
    <div>
      <dt className="text-slate-500 dark:text-slate-400">{label}</dt>
      <dd className="mt-1 font-semibold text-slate-950 dark:text-white">{value}</dd>
    </div>
  );
}

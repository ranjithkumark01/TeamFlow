import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { getErrorMessage } from '../api/client';
import api, { unwrap } from '../api/client';
import ErrorBanner from '../components/ErrorBanner';
import FormError from '../components/FormError';
import Header from '../components/Header';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../contexts/AuthContext';

export default function Profile() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm();

  useEffect(() => {
    if (!user?.id) return;
    api.get('/profile')
      .then(unwrap)
      .then((data) => {
        setProfile(data);
        reset({ name: data.name, email: data.email, password: '' });
      })
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, [user?.id, reset]);

  const updateProfile = async (values) => {
    setError('');
    setSuccess('');
    try {
      const payload = { ...values, password: values.password || null };
      const updated = unwrap(await api.put('/profile', payload));
      setProfile(updated);
      reset({ name: updated.name, email: updated.email, password: '' });
      setSuccess('Profile updated.');
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  return (
    <>
      <Header title="Profile" description="Signed-in user account details." />
      <section className="p-4 sm:p-6">
        <ErrorBanner message={error} />
        {success && <div className="mb-4 border border-emerald-200 bg-emerald-50 p-3 text-sm text-emerald-700 dark:border-emerald-900 dark:bg-emerald-950 dark:text-emerald-200">{success}</div>}
        {loading ? <LoadingSpinner label="Loading profile" /> : profile && (
          <form onSubmit={handleSubmit(updateProfile)} className="max-w-2xl border border-slate-200 bg-white p-6 dark:border-slate-800 dark:bg-slate-900">
            <div className="flex h-16 w-16 items-center justify-center bg-slate-950 text-xl font-bold text-white dark:bg-cyan-500 dark:text-slate-950">
              {profile.name?.slice(0, 1)}
            </div>
            <div className="mt-6 grid gap-4 sm:grid-cols-2">
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">Name<input {...register('name', { required: 'Name is required' })} className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" /><FormError message={errors.name?.message} /></label>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">Email<input {...register('email', { required: 'Email is required' })} type="email" className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" /><FormError message={errors.email?.message} /></label>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-200">New password<input {...register('password', { minLength: { value: 8, message: 'Use at least 8 characters' } })} type="password" className="mt-2 h-10 w-full border border-slate-300 px-3 dark:border-slate-700 dark:bg-slate-950 dark:text-white" /><FormError message={errors.password?.message} /></label>
              <ProfileField label="Role" value={profile.role} />
              <ProfileField label="User ID" value={`#${profile.id}`} />
            </div>
            <button disabled={isSubmitting} className="mt-6 h-10 bg-slate-950 px-4 text-sm font-semibold text-white dark:bg-cyan-500 dark:text-slate-950">Save profile</button>
          </form>
        )}
      </section>
    </>
  );
}

function ProfileField({ label, value }) {
  return (
    <div>
      <dt className="text-sm text-slate-500 dark:text-slate-400">{label}</dt>
      <dd className="mt-1 font-semibold text-slate-950 dark:text-white">{value}</dd>
    </div>
  );
}

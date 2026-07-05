import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { getErrorMessage } from '../api/client';
import FormError from '../components/FormError';

const roles = ['ADMIN', 'MANAGER', 'DEVELOPER', 'REVIEWER'];

export default function Register() {
  const { register: registerUser } = useAuth();
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues: { role: 'DEVELOPER' } });

  const onSubmit = async (values) => {
    setSubmitting(true);
    setError('');
    try {
      await registerUser(values);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <main className="grid min-h-screen bg-slate-50 dark:bg-slate-950 lg:grid-cols-[0.95fr_1.05fr]">
      <section className="hidden bg-slate-950 p-10 text-white lg:flex lg:flex-col lg:justify-between">
        <div className="text-xl font-bold">TeamFlow</div>
        <div>
          <h1 className="max-w-lg text-4xl font-bold">Set up your account and start managing delivery flow.</h1>
          <p className="mt-4 max-w-md text-slate-300">Roles are enforced through JWT-backed Spring Security.</p>
        </div>
      </section>
      <section className="flex items-center justify-center px-5 py-10">
        <form onSubmit={handleSubmit(onSubmit)} className="w-full max-w-md border border-slate-200 bg-white p-6 dark:border-slate-800 dark:bg-slate-900">
          <h2 className="text-2xl font-bold text-slate-950 dark:text-white">Register</h2>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Create a TeamFlow user account.</p>
          {error && <div className="mt-4 bg-rose-50 p-3 text-sm text-rose-700 dark:bg-rose-950 dark:text-rose-200">{error}</div>}
          <label className="mt-5 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Name
            <input {...register('name', { required: 'Name is required' })} className="mt-2 h-11 w-full border border-slate-300 px-3 outline-none focus:border-cyan-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
            <FormError message={errors.name?.message} />
          </label>
          <label className="mt-4 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Email
            <input {...register('email', { required: 'Email is required' })} type="email" className="mt-2 h-11 w-full border border-slate-300 px-3 outline-none focus:border-cyan-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
            <FormError message={errors.email?.message} />
          </label>
          <label className="mt-4 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Password
            <input {...register('password', { required: 'Password is required', minLength: { value: 8, message: 'Use at least 8 characters' } })} type="password" className="mt-2 h-11 w-full border border-slate-300 px-3 outline-none focus:border-cyan-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white" />
            <FormError message={errors.password?.message} />
          </label>
          <label className="mt-4 block text-sm font-medium text-slate-700 dark:text-slate-200">
            Role
            <select {...register('role', { required: 'Role is required' })} className="mt-2 h-11 w-full border border-slate-300 px-3 outline-none focus:border-cyan-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white">
              {roles.map((role) => <option key={role} value={role}>{role}</option>)}
            </select>
          </label>
          <button disabled={submitting} className="mt-6 h-11 w-full bg-slate-950 text-sm font-semibold text-white disabled:opacity-60 dark:bg-cyan-500 dark:text-slate-950">
            {submitting ? 'Creating account...' : 'Register'}
          </button>
          <p className="mt-4 text-center text-sm text-slate-500 dark:text-slate-400">
            Already registered? <Link className="font-semibold text-cyan-600 dark:text-cyan-300" to="/login">Log in</Link>
          </p>
        </form>
      </section>
    </main>
  );
}


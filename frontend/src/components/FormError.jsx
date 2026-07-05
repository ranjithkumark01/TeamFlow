export default function FormError({ message }) {
  if (!message) return null;
  return <p className="mt-1 text-sm text-rose-600 dark:text-rose-300">{message}</p>;
}


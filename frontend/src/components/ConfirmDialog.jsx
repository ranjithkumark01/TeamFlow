import Modal from './Modal';

export default function ConfirmDialog({ open, title = 'Confirm action', message, onConfirm, onCancel }) {
  return (
    <Modal open={open} title={title} onClose={onCancel}>
      <p className="text-sm text-slate-600 dark:text-slate-300">{message}</p>
      <div className="mt-5 flex justify-end gap-2">
        <button type="button" onClick={onCancel} className="border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 dark:border-slate-700 dark:text-slate-200">Cancel</button>
        <button type="button" onClick={onConfirm} className="bg-rose-600 px-4 py-2 text-sm font-semibold text-white">Delete</button>
      </div>
    </Modal>
  );
}


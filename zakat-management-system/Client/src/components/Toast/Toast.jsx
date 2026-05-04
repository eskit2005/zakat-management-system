import { useEffect } from 'react';
import { Info, X } from 'lucide-react';
import styles from './Toast.module.css';

const Toast = ({ message, onClose, duration = 3000 }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [onClose, duration]);

  return (
    <div className={styles.toastContainer}>
      <div className={styles.toast}>
        <div className={styles.icon}>
          <Info size={20} />
        </div>
        <div className={styles.message}>{message}</div>
        <button className={styles.closeBtn} onClick={onClose}>
          <X size={16} />
        </button>
      </div>
    </div>
  );
};

export default Toast;

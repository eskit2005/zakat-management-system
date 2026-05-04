import { useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { addInventoryItem } from '../../infrastructure/zakatRepository';
import Toast from '../../components/Toast/Toast';
import styles from '../Beneficiary/Form.module.css';

const ItemDonation = () => {
  const [formData, setFormData] = useState({
    name: '',
    approxValue: '',
  });
  const [submitted, setSubmitted] = useState(false);
  const [toast, setToast] = useState(null);
  const { user, accessToken } = useContext(AuthContext);
  const authFetch = useAuthFetch();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        name: formData.name,
        approxValue: parseFloat(formData.approxValue),
        donorId: user.id
      };
      await addInventoryItem(authFetch, payload);
      setSubmitted(true);
    } catch (err) {
      setToast({ message: "Item donation failed. " + err.message, type: 'error' });
    }
  };

  if (!accessToken || user?.role !== 'DONOR') {
    return (
      <div className={styles.container}>
        <div className={styles.simplePrompt}>
          <h2>No Access</h2>
          <p>You need to be a donor to access this webpage.</p>
          {!accessToken ? (
            <Link to="/login" className={styles.simpleLink}>Login here</Link>
          ) : (
            <Link to="/" className={styles.simpleLink}>Return to Homepage</Link>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className={`${styles.container} container`}>
      {toast && (
        <Toast 
          message={toast.message} 
          onClose={() => setToast(null)} 
        />
      )}
      <div className={styles.header}>
        <h2>Item Donation</h2>
        <p>Your gently used items can make a world of difference.</p>
      </div>

      <div className={styles.formNarrow}>
        {submitted ? (
          <div className={styles.card} style={{ textAlign: 'center' }}>
            <h3 className={styles.successTitle}>Contribution Logged</h3>
            <p className={styles.successMsg}>Thank you for your donation. Our logistics team will contact you soon.</p>
            <button onClick={() => setSubmitted(false)} className={styles.updateBtn}>
              Donate Another Item
            </button>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className={styles.card}>
            <div className={styles.formGroup}>
              <label>Item Name</label>
              <input 
                className={styles.input}
                name="name" 
                value={formData.name} 
                onChange={handleChange} 
                required 
                placeholder="e.g. Winter Jacket, Laptop" 
              />
            </div>
            <div className={styles.formGroup}>
              <label>Approximate Value (USD)</label>
              <input 
                className={styles.input}
                type="number"
                name="approxValue" 
                value={formData.approxValue} 
                onChange={handleChange} 
                required 
                placeholder="0.00" 
              />
            </div>
            
            <button type="submit" className={styles.submitBtn}>
               Register Item Donation
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ItemDonation;

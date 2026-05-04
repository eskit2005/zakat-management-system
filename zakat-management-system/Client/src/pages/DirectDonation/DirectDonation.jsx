import { useEffect, useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { fetchBeneficiaryQueue, donateDirectly } from '../../infrastructure/zakatRepository';
import Toast from '../../components/Toast/Toast';
import styles from './DirectDonation.module.css';

const DirectDonation = () => {
  const [beneficiaries, setBeneficiaries] = useState([]);
  const [selectedBeneficiary, setSelectedBeneficiary] = useState(null);
  const [amount, setAmount] = useState('');
  const [toast, setToast] = useState(null);
  const { user, accessToken } = useContext(AuthContext);
  const authFetch = useAuthFetch();

  async function loadQueue() {
    try {
      const data = await fetchBeneficiaryQueue(authFetch);
      setBeneficiaries(data);
    } catch (err) {
      console.error("Queue fetch failed", err);
    }
  }

  useEffect(() => {
    if (accessToken && user?.role === 'DONOR') {
      loadQueue();
    }
  }, [accessToken, user]);

  const handleDonateClick = (id) => {
    setSelectedBeneficiary(beneficiaries.find(b => b.id === id));
  };

  const handleDirectDonate = async (e) => {
    e.preventDefault();
    try {
      await donateDirectly(authFetch, user.id, {
        beneficiaryId: selectedBeneficiary.id,
        amount: parseFloat(amount)
      });
      setToast({ message: `Successfully donated $${amount} to ${selectedBeneficiary.fullName}`, type: 'success' });
      setSelectedBeneficiary(null);
      setAmount('');
      loadQueue();
    } catch (err) {
      setToast({ message: "Direct donation failed. Please try again.", type: 'error' });
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
        <h2>Direct Aid</h2>
        <p>Support our verified beneficiaries directly based on their specific needs.</p>
      </div>

      {selectedBeneficiary ? (
        <div className={styles.donationForm}>
          <div className={styles.backBtn} onClick={() => setSelectedBeneficiary(null)}>
             &larr; Back to Queue
          </div>
          <div className={styles.formHeader}>
            <h3 className={styles.formTitle}>Aid for {selectedBeneficiary.fullName}</h3>
            <p className={styles.formSub}>{selectedBeneficiary.reason}</p>
          </div>
          <form onSubmit={handleDirectDonate}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Amount to Donate (USD)</label>
              <input 
                className={styles.input}
                type="number" 
                value={amount} 
                onChange={(e) => setAmount(e.target.value)} 
                required 
                placeholder="0.00"
              />
            </div>
            <button type="submit" className={styles.submitBtn}>
              Confirm Direct Donation
            </button>
          </form>
        </div>
      ) : (
        <div className={styles.grid}>
          {beneficiaries.map(b => (
            <div key={b.id} className={styles.card}>
              <div className={styles.cardHeader}>
                <div className={styles.avatar}>
                  U
                </div>
                <div className={styles.beneficiaryInfo}>
                  <h3>{b.fullName}</h3>
                  <div className={styles.priorityBadge}>Priority: {b.priorityScore}</div>
                </div>
              </div>
              
              <p className={styles.story}>
                {b.reason || "No personal story provided yet."}
              </p>

              <div className={styles.cardFooter}>
                <span className={styles.ageInfo}>Age: {b.age || 'N/A'}</span>
                <button onClick={() => handleDonateClick(b.id)} className={styles.donateBtn}>
                  Support &rarr;
                </button>
              </div>
            </div>
          ))}
          {beneficiaries.length === 0 && (
            <div className={styles.emptyQueue}>
              <p className={styles.emptyText}>The distribution queue is currently empty.</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default DirectDonation;

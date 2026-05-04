import { useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { createDonation } from '../../infrastructure/zakatRepository';
import { jsPDF } from 'jspdf';
import Toast from '../../components/Toast/Toast';
import styles from './Donation.module.css';

const DonationPage = () => {
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');
  const [receipt, setReceipt] = useState(null);
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState(null);
  const { user, accessToken } = useContext(AuthContext);
  const authFetch = useAuthFetch();

  const handleDonate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const data = await createDonation(authFetch, {
        donorId: user.id,
        amount: parseFloat(amount),
        description: description
      });
      setReceipt(data);
    } catch (err) {
      setToast({ message: 'Donation failed. Please try again.', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const downloadPDF = () => {
    if (!receipt) return;
    const doc = new jsPDF();
    doc.setFontSize(22);
    doc.text('Zakat Management System', 105, 20, { align: 'center' });
    doc.setFontSize(16);
    doc.text('Official Donation Receipt', 105, 30, { align: 'center' });
    doc.line(20, 40, 190, 40);
    doc.setFontSize(12);
    doc.text(`Receipt ID: ${receipt.recepNum || receipt.receiptId}`, 20, 60);
    doc.text(`Date: ${new Date(receipt.issuedAt).toLocaleDateString()}`, 20, 70);
    doc.text(`Donor Name: ${receipt.donorName}`, 20, 80);
    doc.text(`Amount: $${receipt.amount.toLocaleString()}`, 20, 90);
    doc.text(`Description: ${receipt.description || 'N/A'}`, 20, 100);
    doc.save(`Receipt_${receipt.recepNum || receipt.receiptId}.pdf`);
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
    <div className={styles.container}>
      {toast && (
        <Toast 
          message={toast.message} 
          onClose={() => setToast(null)} 
        />
      )}
      <div className={styles.header}>
        <h2>Fulfill your Zakat</h2>
        <p>A simple, transparent way to give back to the community.</p>
      </div>

      <div className={styles.grid}>
        <div className={styles.card}>
          <h3>Make a Contribution</h3>
          <form onSubmit={handleDonate}>
            <div className={styles.formGroup}>
              <label>Donation Amount</label>
              <div className={styles.inputWrapper}>
                <span className={styles.currency}>$</span>
                <input 
                  type="number" 
                  value={amount} 
                  onChange={(e) => setAmount(e.target.value)} 
                  placeholder="0.00" 
                  required 
                />
              </div>
            </div>
            <div className={styles.formGroup}>
              <label>Notes / Description</label>
              <textarea 
                className={styles.textarea}
                value={description} 
                onChange={(e) => setDescription(e.target.value)} 
                rows="4"
                placeholder="Optional notes about your donation..."
              ></textarea>
            </div>
            <button type="submit" className={styles.submitBtn} disabled={loading}>
              {loading ? 'Processing...' : 'Complete Payment'}
            </button>
          </form>
        </div>

        {receipt ? (
          <div className={styles.receiptCard}>
            <h3 className={styles.receiptTitle}>Success!</h3>
            <p className={styles.receiptSub}>Thank you for your generous support.</p>
            
            <div className={styles.receiptDetails}>
              <div className={styles.detailItem}>
                <span className={styles.detailLabel}>Receipt Number</span>
                <span className={styles.detailValId}>{receipt.recepNum || `#${receipt.receiptId}`}</span>
              </div>
              <div className={styles.detailItem}>
                <span className={styles.detailLabel}>Amount</span>
                <span className={styles.detailValAmount}>${receipt.amount.toLocaleString()}</span>
              </div>
            </div>

            <button onClick={downloadPDF} className={styles.downloadBtn}>
              Download Receipt (PDF)
            </button>
          </div>
        ) : (
          <div className={styles.emptyReceipt}>
            <p>Your receipt will be generated automatically.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default DonationPage;

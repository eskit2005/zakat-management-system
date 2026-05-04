import { useEffect, useState, useContext } from 'react';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { 
  fetchDashboard, 
  fetchBeneficiaryQueue, 
  fetchAvailableInventory, 
  assignZakat 
} from '../../infrastructure/zakatRepository';
import { 
  Users, 
  DollarSign, 
  Activity, 
  ShieldAlert, 
  CheckCircle, 
  ArrowRight,
  TrendingUp,
  HeartHandshake
} from 'lucide-react';
import styles from './AdminDashboard.module.css';

const AdminDashboard = () => {
  const [data, setData] = useState(null);
  const [queue, setQueue] = useState([]);
  const [inventory, setInventory] = useState([]);
  const [selectedBeneficiary, setSelectedBeneficiary] = useState(null);
  const [assignmentType, setAssignmentType] = useState('MONEY'); // 'MONEY' or 'ITEM'
  const [amount, setAmount] = useState('');
  const [itemId, setItemId] = useState('');
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState({ type: '', text: '' });
  
  const { user } = useContext(AuthContext);
  const authFetch = useAuthFetch();

  async function loadAllData() {
    try {
      const d = await fetchDashboard(authFetch);
      setData(d);
      
      const q = await fetchBeneficiaryQueue(authFetch);
      setQueue(q);
      
      const i = await fetchAvailableInventory(authFetch);
      setInventory(i);
    } catch (err) {
      console.error("Dashboard data load failed", err);
    }
  }

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      loadAllData();
    }
  }, [user]);

  const handleAssign = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMsg({ type: '', text: '' });
    try {
      const payload = {
        beneficiaryId: selectedBeneficiary.id,
        adminId: user.id,
      };

      if (assignmentType === 'MONEY') {
        payload.amountAssigned = parseFloat(amount);
        payload.inventoryId = null;
      } else {
        payload.amountAssigned = null;
        payload.inventoryId = parseInt(itemId);
      }
      
      await assignZakat(authFetch, payload);
      setMsg({ type: 'success', text: `Successfully assigned to ${selectedBeneficiary.fullName}` });
      setSelectedBeneficiary(null);
      setAmount('');
      setItemId('');
      loadAllData(); // Refresh stats
    } catch (err) {
      setMsg({ type: 'error', text: err.message });
    } finally {
      setLoading(false);
    }
  };

  if (user?.role !== 'ADMIN') {
    return (
      <div className={styles.unauthContainer}>
        <ShieldAlert size={80} />
        <h2>Access Restricted</h2>
        <p>This dashboard is reserved for authorized administrators.</p>
      </div>
    );
  }

  if (!data) return <div className={styles.loading}>Initializing Secure Dashboard...</div>;

  return (
    <div className={`${styles.container} container`}>
      <div className={styles.header}>
        <h2>System Monitoring</h2>
        <p>Real-time financial tracking and aid allocation.</p>
      </div>

      <div className={styles.statsGrid}>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.statIconUsers}`}>
            <Users size={24} />
          </div>
          <div className={styles.statInfo}>
            <h4>Total Donors</h4>
            <h2>{data.totalDonors || 0}</h2>
          </div>
        </div>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.statIconBeneficiaries}`}>
            <Activity size={24} />
          </div>
          <div className={styles.statInfo}>
            <h4>Beneficiaries</h4>
            <h2>{data.totalBeneficiaries || 0}</h2>
          </div>
        </div>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.statIconFunds}`}>
            <DollarSign size={24} />
          </div>
          <div className={styles.statInfo}>
            <h4>Total Donated</h4>
            <h2>${data.totalDonated?.toLocaleString()}</h2>
          </div>
        </div>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.statIconDistributed}`}>
            <HeartHandshake size={24} />
          </div>
          <div className={styles.statInfo}>
            <h4>Total Distributed</h4>
            <h2>${data.totalDistributed?.toLocaleString()}</h2>
          </div>
        </div>
        <div className={styles.statCard}>
          <div className={`${styles.statIcon} ${styles.statIconRemaining}`}>
            <TrendingUp size={24} />
          </div>
          <div className={styles.statInfo}>
            <h4>Funds Remaining</h4>
            <h2 style={{ color: '#10b981' }}>${data.remaining?.toLocaleString()}</h2>
          </div>
        </div>
      </div>

      <div className={styles.mainGrid}>
        <div className={styles.queueCard}>
          <div className={styles.cardHeader}>
            <h3>Beneficiary Queue</h3>
            <p>Priority-sorted candidates awaiting assistance.</p>
          </div>
          <div className={styles.queueList}>
            {queue.map(b => (
              <div key={b.id} className={styles.queueItem}>
                <div className={styles.queueInfo}>
                  <strong>{b.fullName}</strong>
                  <div className={styles.priorityBadge}>Score: {b.priorityScore}</div>
                  <p className={styles.queueReason}>{b.reason}</p>
                </div>
                <button 
                  className={styles.assignBtn}
                  onClick={() => setSelectedBeneficiary(b)}
                >
                  Allocate Aid <ArrowRight size={16} />
                </button>
              </div>
            ))}
            {queue.length === 0 && <p className={styles.emptyMsg}>No beneficiaries currently in queue.</p>}
          </div>
        </div>

        <div className={styles.actionCard}>
          <h3>Aid Allocation</h3>
          {selectedBeneficiary ? (
            <div className={styles.formWrapper}>
              <div className={styles.selectedTarget}>
                <span>Target:</span>
                <strong>{selectedBeneficiary.fullName}</strong>
              </div>
              
              {msg.text && (
                <div className={msg.type === 'success' ? styles.successMsg : styles.errorMsg}>
                  {msg.text}
                </div>
              )}

              <form onSubmit={handleAssign}>
                <div className={styles.typeSelector}>
                  <button 
                    type="button"
                    className={assignmentType === 'MONEY' ? styles.activeType : ''}
                    onClick={() => setAssignmentType('MONEY')}
                  >
                    Financial Aid
                  </button>
                  <button 
                    type="button"
                    className={assignmentType === 'ITEM' ? styles.activeType : ''}
                    onClick={() => setAssignmentType('ITEM')}
                  >
                    Inventory Item
                  </button>
                </div>

                {assignmentType === 'MONEY' ? (
                  <div className={styles.formGroup}>
                    <label>Amount (USD)</label>
                    <input 
                      type="number" 
                      value={amount} 
                      onChange={(e) => setAmount(e.target.value)} 
                      placeholder="Enter amount..."
                      required 
                    />
                  </div>
                ) : (
                  <div className={styles.formGroup}>
                    <label>Select Item from Inventory</label>
                    <select value={itemId} onChange={(e) => setItemId(e.target.value)} required>
                      <option value="">-- Choose Item --</option>
                      {inventory.map(i => (
                        <option key={i.id} value={i.id}>{i.name} (Value: ${i.appoxValue})</option>
                      ))}
                    </select>
                  </div>
                )}

                <div className={styles.formActions}>
                  <button 
                    type="button" 
                    className={styles.cancelBtn}
                    onClick={() => setSelectedBeneficiary(null)}
                  >
                    Cancel
                  </button>
                  <button 
                    type="submit" 
                    className={styles.confirmBtn}
                    disabled={loading}
                  >
                    {loading ? 'Processing...' : 'Confirm Assignment'}
                  </button>
                </div>
              </form>
            </div>
          ) : (
            <div className={styles.emptyAction}>
              <CheckCircle size={48} opacity={0.2} />
              <p>Select a beneficiary from the queue to allocate funds or items.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

import { Users, User } from 'lucide-react';
import styles from './Profile.module.css';

const DonorProfile = ({ data, totalDonated, beneficiaries }) => {
  return (
    <div className={styles.profileContent}>
      <div className={styles.statsGrid}>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Total Contributions</span>
          <div className={styles.statValue}>
            ${(totalDonated || 0).toLocaleString()}
          </div>
        </div>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Beneficiaries Supported</span>
          <div className={styles.statValue}>{beneficiaries?.length || 0}</div>
        </div>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Donor ID</span>
          <div className={styles.statValue}>#{data.id}</div>
        </div>
      </div>

      <div className={styles.sectionTitle}>
        <Users size={24} />
        <span>Impact Network</span>
      </div>

      {beneficiaries?.length > 0 ? (
        <div className={styles.listGrid}>
          {beneficiaries.map((b, index) => (
            <div key={`${b.id}-${index}`} className={styles.beneficiaryCard}>
              <div className={styles.miniAvatar}>
                <User size={20} />
              </div>
              <div className={styles.beneficiaryInfo}>
                <div className={styles.beneficiaryName}>{b.fullName}</div>
                <div className={styles.beneficiaryReason}>{b.reason}</div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className={styles.emptyState}>
          <p>You haven't supported any individual beneficiaries directly yet.</p>
        </div>
      )}
    </div>
  );
};

export default DonorProfile;

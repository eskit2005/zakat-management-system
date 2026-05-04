import { HeartHandshake } from 'lucide-react';
import styles from './Profile.module.css';

const BeneficiaryProfile = ({ data }) => {
  return (
    <div className={styles.profileContent}>
      <div className={styles.statsGrid}>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Total Aid Received</span>
          <div className={styles.statValue}>
            ${(data.totalReceivedValue || 0).toLocaleString()}
          </div>
        </div>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Priority Score</span>
          <div className={styles.statValue}>{data.priorityScore || 0}</div>
        </div>
        <div className={styles.statItem}>
          <span className={styles.statLabel}>Status</span>
          <div className={styles.statValue}>{data.eligible ? 'Eligible' : 'Reviewing'}</div>
        </div>
      </div>

      <div className={styles.sectionTitle}>
        <HeartHandshake size={24} />
        <span>Your Impact Story</span>
      </div>
      
      <div className={styles.profileStory}>
        <p className={styles.storyText}>
          {data.reason || "You haven't shared your story yet. Completing your eligibility form helps us better understand your needs."}
        </p>
      </div>
    </div>
  );
};

export default BeneficiaryProfile;

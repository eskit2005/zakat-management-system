import { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { 
  fetchBeneficiaryById, 
  fetchDonorById, 
  fetchDonatedBeneficiaries, 
  fetchTotalDonations 
} from '../../infrastructure/zakatRepository';
import BeneficiaryProfile from './BeneficiaryProfile';
import DonorProfile from './DonorProfile';
import styles from './Profile.module.css';

const Profile = () => {
  const { user, accessToken } = useContext(AuthContext);
  const authFetch = useAuthFetch();
  const [profileData, setProfileData] = useState(null);
  const [donorStats, setDonorStats] = useState({ total: 0, beneficiaries: [] });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadProfile() {
      if (!accessToken || !user) return;
      
      try {
        setLoading(true);
        if (user.role === 'BENEFICIARY') {
          const data = await fetchBeneficiaryById(authFetch, user.id);
          setProfileData(data);
        } else if (user.role === 'DONOR') {
          const [data, total, beneficiaries] = await Promise.all([
            fetchDonorById(authFetch, user.id),
            fetchTotalDonations(authFetch, user.id),
            fetchDonatedBeneficiaries(authFetch, user.id)
          ]);
          setProfileData(data);
          setDonorStats({ total, beneficiaries });
        }
      } catch (err) {
        console.error("Profile load failed", err);
      } finally {
        setLoading(false);
      }
    }
    loadProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [accessToken, user]);

  if (loading) return <div className={styles.profileContainer}><p>Loading profile...</p></div>;
  if (!profileData) return <div className={styles.profileContainer}><p>No profile data found.</p></div>;

  return (
    <div className={styles.profileContainer}>
      <div className={styles.profileCard}>
        <div className={styles.profileHeader}>
          <div className={styles.avatar}>
            {(user.name || 'U').charAt(0)}
          </div>
          <div className={styles.headerInfo}>
            <h2>{user.name}</h2>
            <span className={styles.roleBadge}>{user.role}</span>
          </div>
        </div>

        {user.role === 'BENEFICIARY' ? (
          <BeneficiaryProfile data={profileData} />
        ) : (
          <DonorProfile 
            data={profileData} 
            totalDonated={donorStats.total} 
            beneficiaries={donorStats.beneficiaries} 
          />
        )}
      </div>
    </div>
  );
};

export default Profile;

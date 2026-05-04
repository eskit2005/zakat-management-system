import { useEffect, useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { fetchReport } from '../../infrastructure/zakatRepository';
import { ShieldCheck, Users, TrendingUp, HeartHandshake, HandHeart, UserPlus, ClipboardList } from 'lucide-react';
import Toast from '../../components/Toast/Toast';
import styles from './Homepage.module.css';

const Homepage = () => {
  const [report, setReport] = useState(null);
  const [showToast, setShowToast] = useState(false);
  const { accessToken } = useContext(AuthContext);

  const handleJoinClick = (e) => {
    if (accessToken) {
      e.preventDefault();
      setShowToast(true);
    }
  };

  useEffect(() => {
    async function loadReport() {
      try {
        const data = await fetchReport();
        setReport(data);
      } catch (err) {
        console.error("Failed to fetch transparency report", err);
      }
    }
    loadReport();
  }, []); // Empty array means run only once on mount

  return (
    <div className={styles.page}>
      {showToast && (
        <Toast 
          message="You are already signed in to our community." 
          onClose={() => setShowToast(false)} 
        />
      )}
      <section className={styles.hero}>
        <div className={styles.heroGlow}></div>
        <div className={`container ${styles.heroContent}`}>
          <div className={styles.heroBadge}>Transparency & Impact</div>
          <h1 className={styles.heroTitle}>
            Purity Through <span className={styles.heroGreen}>Charity</span>
          </h1>
          <p className={styles.heroSub}>
            Empowering communities through transparent and efficient Zakat management. 
            Your contributions reach those in need, directly and securely.
          </p>
          <div className={styles.heroActions}>
            <Link to="/donate" className={styles.primaryBtn}>Donate Now</Link>
            <Link to="/register" className={styles.outlineBtn} onClick={handleJoinClick}>Join our Community</Link>
          </div>
        </div>
      </section>

      <section className={`${styles.section} container`}>
        <div className={styles.sectionHeader}>
          <h2>Excellence in Distribution</h2>
          <p className={styles.sectionHeaderSub}>We combine traditional values with modern technology for maximum impact.</p>
        </div>
        <div className={styles.grid}>
          <div className={styles.card}>
            <div className={styles.iconWrapper}>
              <ShieldCheck size={32} />
            </div>
            <h3>Full Transparency</h3>
            <p>Real-time tracking of every donation ensures absolute accountability in our distribution process.</p>
          </div>
          <div className={styles.card}>
            <div className={styles.iconWrapper}>
              <Users size={32} />
            </div>
            <h3>Direct Impact</h3>
            <p>Connect directly with verified beneficiaries and hear their stories before offering your support.</p>
          </div>
          <div className={styles.card}>
            <div className={styles.iconWrapper}>
              <TrendingUp size={32} />
            </div>
            <h3>Priority System</h3>
            <p>Our intelligent assessment identifies emergency cases, ensuring aid reaches the most vulnerable first.</p>
          </div>
        </div>
      </section>

      {report && (
        <section className={`${styles.section} container`}>
          <div className={styles.reportSection}>
            <div className={styles.reportHeader}>
              <h2>Global Transparency Report</h2>
              <p className={styles.reportHeaderSub}>Real-time statistics from our community-driven ecosystem.</p>
            </div>
            
            {/* Financial Stats */}
            <div className={styles.reportGridFin}>
              <div className={styles.reportItem}>
                <div className={styles.reportVal}>${(report.totalDonated || 0).toLocaleString()}</div>
                <div className={styles.reportLabel}>Total Collected</div>
              </div>
              <div className={styles.reportItem}>
                <div className={styles.reportVal}>${(report.totalDistributed || 0).toLocaleString()}</div>
                <div className={styles.reportLabel}>Total Distributed</div>
              </div>
              <div className={styles.reportItem}>
                <div className={styles.reportVal}>${(report.remaining || 0).toLocaleString()}</div>
                <div className={styles.reportLabel}>Remaining Funds</div>
              </div>
            </div>

            {/* Impact/Participant Stats */}
            <div className={styles.reportGridImpact}>
              <div className={styles.reportItem}>
                <div className={`${styles.iconWrapper} ${styles.iconImpactDonors}`}>
                  <HandHeart size={28} />
                </div>
                <div className={styles.reportValImpact}>{report.totalDonors || 0}</div>
                <div className={styles.reportLabel}>Generous Donors</div>
              </div>
              <div className={styles.reportItem}>
                <div className={`${styles.iconWrapper} ${styles.iconImpactBeneficiaries}`}>
                  <UserPlus size={28} />
                </div>
                <div className={styles.reportValImpact}>{report.totalBeneficiaries || 0}</div>
                <div className={styles.reportLabel}>Verified Beneficiaries</div>
              </div>
              <div className={styles.reportItem}>
                <div className={`${styles.iconWrapper} ${styles.iconImpactAssignments}`}>
                  <ClipboardList size={28} />
                </div>
                <div className={styles.reportValImpact}>{report.totalAssignments || 0}</div>
                <div className={styles.reportLabel}>Successful Aid Deliveries</div>
              </div>
            </div>
          </div>
        </section>
      )}

      <section className={`${styles.section} container`}>
        <div className={styles.ctaCard}>
          <div className={styles.ctaContent}>
            <h2>Need Assistance?</h2>
            <p>Our verification process is dignified, fast, and strictly follows Shariah principles to support those in genuine need.</p>
            <Link to="/register" className={styles.primaryBtn} onClick={handleJoinClick}>Apply for Aid</Link>
          </div>
          <div className={styles.ctaIcon}>
            <HeartHandshake size={240} />
          </div>
        </div>
      </section>
    </div>
  );
};

export default Homepage;

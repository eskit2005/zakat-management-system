import { useState, useContext, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { submitBeneficiaryForm, fetchBeneficiaryById } from '../../infrastructure/zakatRepository';
import styles from './Form.module.css';

const BeneficiaryForm = () => {
  const { user, accessToken } = useContext(AuthContext);
  const authFetch = useAuthFetch();
  
  const [formData, setFormData] = useState({
    beneficiaryId: user?.id || '',
    reason: '',
    dependents: 0,
    income: 0,
    age: 0,
    emergency: false,
    disability: false,
    isOrphan: false,
    hasDebt: false,
    unemployed: false,
    illness: false
  });
  
  const [submitted, setSubmitted] = useState(false);
  const [submissionState, setSubmissionState] = useState({
    hasPreviouslySubmitted: false,
    showUpdatePrompt: false,
    eligibilityMessage: ''
  });

  useEffect(() => {
    async function checkExistingData() {
      if (accessToken && user?.id && user?.role === 'BENEFICIARY') {
        try {
          const data = await fetchBeneficiaryById(authFetch, user.id);
          // Check if age is present as the indicator of a filled form
          if (data.age !== null && data.age !== undefined) {
            setSubmissionState({
              hasPreviouslySubmitted: true,
              showUpdatePrompt: true,
              eligibilityMessage: ''
            });
          }
        } catch (err) {
          console.error("Failed to fetch beneficiary data", err);
        }
      }
    }
    checkExistingData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [accessToken, user]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        beneficiaryId: user.id,
        dependents: parseInt(formData.dependents),
        income: parseFloat(formData.income),
        age: parseInt(formData.age)
      };
      
      const data = await submitBeneficiaryForm(authFetch, payload);
      setSubmitted(true);
      setSubmissionState({
        hasPreviouslySubmitted: true,
        showUpdatePrompt: false,
        eligibilityMessage: data.priorityScore > 50 
          ? "Your high-priority information has been updated. Our team will review your situation urgently."
          : "Your information has been updated successfully and your status in the verification queue has been refreshed."
      });
    } catch (err) {
      alert("Submission failed: " + err.message);
    }
  };

  if (!accessToken || user?.role !== 'BENEFICIARY') {
    return (
      <div className={styles.container}>
        <div className={styles.simplePrompt}>
          <h2>Access Restricted</h2>
          <p>You need to be a registered beneficiary to access this eligibility form.</p>
          {!accessToken ? (
            <Link to="/login" className={styles.simpleLink}>Login here</Link>
          ) : (
            <Link to="/" className={styles.simpleLink}>Return Home</Link>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className={`${styles.container} container`}>
      <div className={styles.header}>
        <h2>Eligibility Assessment</h2>
        <p>Please provide full details to help us assess your Zakat eligibility accurately.</p>
      </div>

      <div>
        {submitted ? (
          <div className={`${styles.card} ${styles.successCard}`}>
            <h3 className={styles.successTitle}>Information Updated</h3>
            <p className={styles.successMsg}>{submissionState.eligibilityMessage}</p>
            <button onClick={() => setSubmitted(false)} className={styles.updateBtn}>
              Edit Again
            </button>
          </div>
        ) : (
          <>
            {submissionState.showUpdatePrompt && (
              <div className={styles.updateNotice}>
                <p><strong>Note:</strong> You have already submitted an eligibility form. Would you like to review or update your information below?</p>
                <button className={styles.dismissBtn} onClick={() => setSubmissionState(s => ({...s, showUpdatePrompt: false}))}>Dismiss</button>
              </div>
            )}
            <form onSubmit={handleSubmit} className={styles.card}>
              <div className={styles.formGrid}>
                <div className={styles.formGroup}>
                  <label>Age</label>
                  <input 
                    className={styles.input}
                    type="number" 
                    name="age" 
                    value={formData.age} 
                    onChange={handleChange} 
                    min="0" 
                    required 
                  />
                </div>
                <div className={styles.formGroup}>
                  <label>Monthly Income (USD)</label>
                  <input 
                    className={styles.input}
                    type="number" 
                    name="income" 
                    value={formData.income} 
                    onChange={handleChange} 
                    min="0" 
                    required 
                  />
                </div>
                <div className={styles.formGroup}>
                  <label>Number of Dependents</label>
                  <input 
                    className={styles.input}
                    type="number" 
                    name="dependents" 
                    value={formData.dependents} 
                    onChange={handleChange} 
                    min="0" 
                    required 
                  />
                </div>
              </div>

              <div className={styles.checkboxGrid}>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="emergency" checked={formData.emergency} onChange={handleChange} id="emergency" />
                  <label htmlFor="emergency">Emergency Situation</label>
                </div>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="disability" checked={formData.disability} onChange={handleChange} id="disability" />
                  <label htmlFor="disability">Physical Disability</label>
                </div>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="isOrphan" checked={formData.isOrphan} onChange={handleChange} id="isOrphan" />
                  <label htmlFor="isOrphan">Orphan Status</label>
                </div>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="hasDebt" checked={formData.hasDebt} onChange={handleChange} id="hasDebt" />
                  <label htmlFor="hasDebt">Significant Debt</label>
                </div>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="unemployed" checked={formData.unemployed} onChange={handleChange} id="unemployed" />
                  <label htmlFor="unemployed">Currently Unemployed</label>
                </div>
                <div className={styles.checkboxGroup}>
                  <input type="checkbox" name="illness" checked={formData.illness} onChange={handleChange} id="illness" />
                  <label htmlFor="illness">Chronic Illness</label>
                </div>
              </div>

              <div className={styles.formGroup}>
                <label>Reason for Request / Your Story</label>
                <textarea 
                  className={styles.textarea}
                  name="reason" 
                  value={formData.reason} 
                  onChange={handleChange} 
                  rows="6" 
                  placeholder="Please describe your current situation in detail..."
                  required
                ></textarea>
              </div>

              <button type="submit" className={styles.submitBtn}>
                {submissionState.hasPreviouslySubmitted ? 'Update Application' : 'Submit Application'}
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default BeneficiaryForm;

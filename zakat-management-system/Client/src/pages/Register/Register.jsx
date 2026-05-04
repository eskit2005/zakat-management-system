import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser } from '../../domain/authService';
import styles from '../Login/Auth.module.css';

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'DONOR'
  });
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState([]);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setValidationErrors([]);
    try {
      await registerUser(formData);
      navigate('/login');
    } catch (err) {
      if (err.status === 409) {
        setError('This email is already registered. Please use another one.');
      } else if (err.status === 400 && err.errors) {
        setError('Please correct the following errors:');
        setValidationErrors(err.errors);
      } else {
        setError(err.message || 'Registration failed. Please try again.');
      }
    }
  };

  return (
    <div className={styles.authContainer}>
      <div className={styles.authCardWide}>
        <div className={styles.header}>
          <h2>Create Account</h2>
          <p>Join our secure Zakat management platform</p>
        </div>
        
        {error && (
          <div className={styles.error}>
            <p>{error}</p>
            {validationErrors.length > 0 && (
              <ul style={{ textAlign: 'left', marginTop: '10px', fontSize: '0.8rem' }}>
                {validationErrors.map((v, i) => <li key={i}>{v}</li>)}
              </ul>
            )}
          </div>
        )}
        
        <form onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label>Full Name</label>
            <input 
              name="name" 
              onChange={handleChange} 
              required 
              placeholder="John Doe" 
              value={formData.name}
            />
          </div>
          
          <div className={styles.formGrid}>
            <div className={styles.formGroup}>
              <label>Email Address</label>
              <input 
                name="email" 
                type="email" 
                onChange={handleChange} 
                required 
                placeholder="john@example.com" 
                value={formData.email}
              />
            </div>
            <div className={styles.formGroup}>
              <label>Password</label>
              <input 
                name="password" 
                type="password" 
                onChange={handleChange} 
                required 
                placeholder="••••••••" 
                value={formData.password}
              />
            </div>
          </div>

          <div className={styles.formGroup}>
            <label>I am registering as a...</label>
            <select name="role" onChange={handleChange} value={formData.role}>
              <option value="DONOR">Donor (I want to contribute)</option>
              <option value="BENEFICIARY">Beneficiary (I am seeking aid)</option>
            </select>
          </div>

          <button type="submit" className={styles.submitBtn}>
            Register Now
          </button>
        </form>
        
        <div className={styles.footer}>
          <p>Already have an account? <Link to="/login">Sign in here</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Register;

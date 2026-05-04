import { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { login, fetchMe } from '../../domain/authService';
import { Mail, Lock, LogIn } from 'lucide-react';
import styles from './Auth.module.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setAccessToken, setUser } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const { accessToken } = await login(email, password);
      setAccessToken(accessToken);
      
      const userData = await fetchMe(accessToken);
      setUser(userData);
      navigate('/');
    } catch (err) {
      setError('Invalid email or password');
    }
  };

  return (
    <div className={styles.authContainer}>
      <div className={styles.authCard}>
        <div className={styles.header}>
          <div className={styles.icon}>
            <LogIn size={32} />
          </div>
          <h2>Welcome Back</h2>
          <p>Sign in to manage your Zakat</p>
        </div>
        
        {error && <div className={styles.error}>{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label><Mail size={16} /> Email Address</label>
            <input 
              type="email" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              required 
              placeholder="name@example.com"
            />
          </div>
          <div className={styles.formGroup}>
            <label><Lock size={16} /> Password</label>
            <input 
              type="password" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required 
              placeholder="••••••••"
            />
          </div>
          <button type="submit" className={styles.submitBtn}>
            Sign In
          </button>
        </form>
        
        <div className={styles.footer}>
          <p>Don't have an account? <Link to="/register">Create one now</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Login;

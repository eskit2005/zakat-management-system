import { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../auth/authContext';
import { useAuthFetch } from '../../infrastructure/useAuthFetch';
import { logout as logoutApi } from '../../domain/authService';
import { User as UserIcon, LogOut, ChevronDown, LayoutDashboard, Heart, UserPlus, UserCircle } from 'lucide-react';
import styles from './Navbar.module.css';

const Navbar = () => {
  const { user, setAccessToken, setUser } = useContext(AuthContext);
  const [isOpen, setIsOpen] = useState(false);
  const authFetch = useAuthFetch();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logoutApi(authFetch);
    } catch (err) {
      console.error('Logout failed', err);
    } finally {
      setAccessToken(null);
      setUser(null);
      setIsOpen(false);
      navigate('/login');
    }
  };

  const closeMenu = () => setIsOpen(false);

  return (
    <nav className={styles.navbar}>
      <div className={`container ${styles.content}`}>
        <Link to="/" className={styles.logo} onClick={closeMenu}>
          <Heart fill="currentColor" size={28} />
          <span>ZakatMS</span>
        </Link>
        
        <div className={styles.links}>
          <Link to="/" className={styles.navItem} onClick={closeMenu}>Home</Link>
          <Link to="/donate" className={styles.navItem} onClick={closeMenu}>Donate</Link>
          <Link to="/direct-donate" className={styles.navItem} onClick={closeMenu}>Direct Aid</Link>
          <Link to="/donate-item" className={styles.navItem} onClick={closeMenu}>Donate Item</Link>
          
          {user ? (
            <div 
              className={styles.userMenu}
              onMouseEnter={() => setIsOpen(true)}
              onMouseLeave={() => setIsOpen(false)}
            >
              <div className={styles.userBadge} onClick={() => setIsOpen(!isOpen)}>
                <UserIcon size={18} />
                <span>{user.name || 'User'}</span>
                <ChevronDown size={14} className={isOpen ? styles.rotate : ''} />
              </div>
              
              {isOpen && (
                <div className={styles.dropdown}>
                  <div className={styles.userInfo}>
                    <p className={styles.userEmail}>{user.email}</p>
                    <p className={styles.userRole}>{user.role}</p>
                  </div>
                  {user.role !== 'ADMIN' && (
                    <Link to="/profile" className={styles.dropdownItem} onClick={closeMenu}>
                      <UserCircle size={18} /> My Profile
                    </Link>
                  )}
                  {user.role === 'ADMIN' && (
                    <Link to="/admin" className={styles.dropdownItem} onClick={closeMenu}>
                      <LayoutDashboard size={18} /> Admin Dashboard
                    </Link>
                  )}
                  {user.role === 'BENEFICIARY' && (
                    <Link to="/beneficiary-registration" className={styles.dropdownItem} onClick={closeMenu}>
                      <UserPlus size={18} /> Eligibility Form
                    </Link>
                  )}
                  <button 
                    onClick={handleLogout} 
                    className={`${styles.dropdownItem} ${styles.logoutBtn}`}
                  >
                    <LogOut size={18} /> Logout
                  </button>
                </div>
              )}
            </div>
          ) : (
            <Link to="/login" className={styles.loginBtn}>Login</Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

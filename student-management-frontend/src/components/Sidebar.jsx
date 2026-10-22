import { NavLink, useNavigate } from 'react-router-dom';
import {
  Award,
  BookOpen,
  CalendarCheck,
  ClipboardList,
  GraduationCap,
  LayoutDashboard,
  LogOut,
  School,
  Users,
  X,
} from 'lucide-react';

import { useAuth } from '../context/AuthContext';


const navigation = [
  { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard, roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
  { label: 'Students', path: '/students', icon: GraduationCap, roles: ['ADMIN'] },
  { label: 'Teachers', path: '/teachers', icon: Users, roles: ['ADMIN'] },
  { label: 'Courses', path: '/courses', icon: BookOpen, roles: ['ADMIN', 'TEACHER', 'STUDENT'] },
  { label: 'Enrollments', path: '/enrollments', icon: ClipboardList, roles: ['ADMIN'] },
  { label: 'Attendance', path: '/attendance', icon: CalendarCheck, roles: ['ADMIN', 'TEACHER'] },
  { label: 'My Attendance', path: '/my-attendance', icon: CalendarCheck, roles: ['STUDENT'] },
  { label: 'Marks', path: '/marks', icon: Award, roles: ['ADMIN', 'TEACHER'] },
  { label: 'My Marks', path: '/my-marks', icon: Award, roles: ['STUDENT'] },
];


export default function Sidebar({ open = false, onClose = () => {} }) {
  const { logout ,user} = useAuth();

  const navigate = useNavigate();
  const role = String(user?.role || '').toUpperCase();

  function handleLogout() {
    logout();
    onClose();
    navigate('/login', { replace: true });
  }

  return (
    <>
      {open && (
        <button
          className="sidebar-overlay"
          type="button"
          aria-label="Close navigation"
          onClick={onClose}
        />
      )}

      <aside className={`sidebar ${open ? 'sidebar-open' : ''}`}>
        <div className="sidebar-brand">
          <div className="brand-icon"><School size={25} /></div>
          <div className="brand-text">
            <h2>EduManage</h2>
            <span>Academic workspace</span>
          </div>
          <button
            className="icon-button sidebar-close-button"
            type="button"
            aria-label="Close sidebar"
            onClick={onClose}
          >
            <X size={19} />
          </button>
        </div>

        <div className="sidebar-section-label">WORKSPACE</div>

        <nav className="sidebar-nav" aria-label="Main navigation">
          {navigation
            .filter((item) => item.roles.includes(role))
            .map(({ label, path, icon: Icon }) => (
              <NavLink
                key={path}
                to={path}
                end
                onClick={onClose}
                className={({ isActive }) =>
                  `sidebar-link ${isActive ? 'active' : ''}`
                }
              >
                <Icon size={19} />
                <span>{label}</span>
              </NavLink>
            ))}
        </nav>

        <div className="sidebar-bottom">
          <div className="sidebar-user">
            <div className="user-avatar">
              {(user?.email || 'U').charAt(0).toUpperCase()}
            </div>
            <div className="user-details">
              <strong>{user?.email || 'User'}</strong>
              <span>{role}</span>
            </div>
          </div>

          <button
            className="sidebar-logout"
            type="button"
            onClick={handleLogout}
          >
            <LogOut size={18} />
            <span>Sign out</span>
          </button>
        </div>
      </aside>
    </>
  );
}
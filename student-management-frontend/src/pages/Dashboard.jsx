import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Activity,
  ArrowRight,
  BookOpen,
  CalendarCheck,
  GraduationCap,
  RefreshCw,
  Users,
} from 'lucide-react';

import api, { getErrorMessage } from '../api/axios';
import { useAuth } from '../context/AuthContext';
import StatCard from '../components/StatCard';

const endpoints = {
  ADMIN: '/dashboard/admin',
  TEACHER: '/dashboard/teacher',
  STUDENT: '/dashboard/student',
};

const statDefinitions = [
  { label: 'Total Students', keys: ['totalStudents', 'studentCount'], icon: GraduationCap },
  { label: 'Total Teachers', keys: ['totalTeachers', 'teacherCount'], icon: Users },
  { label: 'Total Courses', keys: ['totalCourses', 'courseCount'], icon: BookOpen },
  { label: 'Enrollments', keys: ['totalEnrollments', 'enrollmentCount'], icon: CalendarCheck },
];


const quickLinks = [
  {
    label: 'Students',
    path: '/students',
    icon: GraduationCap,
    roles: ['ADMIN', 'TEACHER'],
  },
  {
    label: 'Teachers',
    path: '/teachers',
    icon: Users,
    roles: ['ADMIN'],
  },
  {
    label: 'Courses',
    path: '/courses',
    icon: BookOpen,
    roles: ['ADMIN', 'TEACHER', 'STUDENT'],
  },
  {
    label: 'Attendance',
    path: '/attendance',
    icon: CalendarCheck,
    roles: ['ADMIN', 'TEACHER'],
  },
  {
    label: 'Marks',
    path: '/marks',
    icon: Activity,
    roles: ['ADMIN', 'TEACHER'],
  },
];

export default function Dashboard() {
  const { user, logout } = useAuth();
  const role = String(user?.role || '').toUpperCase();

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadDashboard = useCallback(async () => {
    const endpoint = endpoints[role];

    if (!endpoint) {
      setError('Your account does not have a supported role.');
      setLoading(false);
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await api.get(endpoint);
      setData(response.data);
    } catch (err) {
      if (err.response?.status === 401) {
        setError('Your session was rejected. Please sign in again.');
      } else if (err.response?.status === 403) {
        setError('Your account does not have permission to view this dashboard.');
      } else {
        setError(getErrorMessage(err));
      }
    } finally {
      setLoading(false);
    }
  }, [role]);

  useEffect(() => {
    loadDashboard();
  }, [loadDashboard]);

  return (
    <div className="dashboard-page">
      <section className="welcome-card">
        <div>
          <span className="eyebrow">ACADEMIC WORKSPACE</span>
          <h2>Welcome back, {user?.email || 'User'}!</h2>
          <p>Your academic activity and management overview.</p>
        </div>
        <div className="welcome-illustration">
          <GraduationCap size={66} />
        </div>
      </section>

      <div className="section-heading">
        <div>
          <h2>Dashboard overview</h2>
          <p>Latest information from your backend</p>
        </div>
        <button
          className="button button-secondary"
          type="button"
          onClick={loadDashboard}
          disabled={loading}
        >
          <RefreshCw size={16} /> Refresh
        </button>
      </div>

      {error && (
        <div className="alert alert-error" role="alert">
          <span>{error}</span>
          {error.includes('sign in again') && (
            <button
              className="button button-secondary"
              type="button"
              onClick={() => {
                logout();
                window.location.assign('/login');
              }}
            >
              Sign in again
            </button>
          )}
        </div>
      )}

      {loading ? (
        <div className="loading-state">
          <span className="spinner" />
          <p>Loading dashboard...</p>
        </div>
      ) : !error ? (
        <>
          <section className="stats-grid">
            {statDefinitions.map((stat) => {
              const key = stat.keys.find(
                (candidate) => data?.[candidate] !== undefined
              );

              return (
                <StatCard
                  key={stat.label}
                  label={stat.label}
                  value={key ? data[key] : '—'}
                  icon={stat.icon}
                />
              );
            })}
          </section>

          <section className="dashboard-lower-grid">
            <article className="content-card">
              <h3>Dashboard information</h3>
              {data && typeof data === 'object' ? (
                Object.entries(data)
                  .filter(([, value]) => value === null || typeof value !== 'object')
                  .map(([key, value]) => (
                    <div className="data-row" key={key}>
                      <span>{key.replace(/([A-Z])/g, ' $1')}</span>
                      <strong>{value == null ? '—' : String(value)}</strong>
                    </div>
                  ))
              ) : (
                <p>No summary data returned.</p>
              )}
            </article>

            <article className="content-card">
              <h3>Quick access</h3>
              {quickLinks
                .filter((item) => item.roles.includes(role))
                .map(({ label, path, icon: Icon }) => (
                  <Link className="quick-link" key={path} to={path}>
                    <Icon size={18} />
                    <span>{label}</span>
                    <ArrowRight size={16} />
                  </Link>
                ))}
            </article>
          </section>
        </>
      ) : null}
    </div>
  );
}
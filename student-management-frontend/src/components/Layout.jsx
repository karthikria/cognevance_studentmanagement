import { useState } from 'react';
import { Bell, Menu } from 'lucide-react';
import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';

const pageTitles = {
  '/dashboard': 'Dashboard',
  '/students': 'Student Management',
  '/teachers': 'Teacher Management',
  '/courses': 'Course Management',
  '/enrollments': 'Enrollments',
  '/attendance': 'Attendance',
  '/marks': 'Marks & Assessments',
};

export default function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();
  const title = pageTitles[location.pathname] || 'Student Portal';

  return (
    <div className="app-shell">
      <Sidebar
        open={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
      />

      <main className="main-panel">
        <header className="topbar">
          <div className="topbar-left">
            <button
              className="icon-button mobile-menu-button"
              type="button"
              aria-label="Open navigation"
              onClick={() => setSidebarOpen(true)}
            >
              <Menu size={22} />
            </button>

            <div>
              <div className="breadcrumb">Workspace / {title}</div>
              <h1>{title}</h1>
            </div>
          </div>

          <div className="topbar-actions">
            <div className="system-status">
              <span className="status-dot" />
              Student Portal
            </div>
            <button
              className="icon-button"
              type="button"
              aria-label="Notifications"
              title="Notifications are not configured yet"
            >
              <Bell size={19} />
            </button>
          </div>
        </header>

        <section className="page-content">
          <Outlet />
        </section>

        <footer className="app-footer">
          <span>EduManage</span>
          <span>Student Management System</span>
        </footer>
      </main>
    </div>
  );
}
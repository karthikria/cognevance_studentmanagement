import { Link } from 'react-router-dom';
import { ShieldAlert } from 'lucide-react';

export default function Forbidden() {
  return (
    <main className="login-page">
      <section className="login-card forbidden-card">
        <div className="login-logo">
          <ShieldAlert size={30} />
        </div>
        <h1>Access denied</h1>
        <p className="muted">
          Your account does not have permission to open this page.
        </p>
        <Link className="button button-primary" to="/dashboard">
          Return to dashboard
        </Link>
      </section>
    </main>
  );
}
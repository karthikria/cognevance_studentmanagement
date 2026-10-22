export default function StatCard({ label, value, icon: Icon }) {
  return (
    <article className="stat-card">
      <div className="stat-icon">
        {Icon && <Icon size={22} />}
      </div>

      <div className="stat-content">
        <p>{label}</p>
        <strong>{value ?? '—'}</strong>
      </div>
    </article>
  );
}
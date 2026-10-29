import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import ProgressBar from '../components/ProgressBar.jsx';

export default function Dashboard() {
  const { user, call } = useAuth();
  const [items, setItems] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => { call('/enrollments/me').then(setItems).catch((e) => setError(e.message)); }, [call]);

  if (error) return <p className="error" role="alert">{error}</p>;
  if (!items) return <p>Loading…</p>;

  const avg = items.length ? Math.round(items.reduce((s, e) => s + e.progress, 0) / items.length) : 0;
  const done = items.filter((e) => e.progress === 100).length;

  return (
    <>
      <h1>Welcome back, {user.name.split(' ')[0]}</h1>
      <p className="lead">{items.length} enrolled · {done} finished · {avg}% average progress</p>
      {items.length === 0 && (
        <p>You haven't enrolled in anything yet. <Link to="/">Browse courses</Link> and pick your first.</p>
      )}
      <div className="stack">
        {items.map((e) => (
          <article key={e.courseId} className="row-card">
            <div>
              <p className="meta">{e.category} · {e.level}</p>
              <h3><Link to={`/courses/${e.courseId}`}>{e.title}</Link></h3>
              <p className="foot">{e.lastScore == null ? 'No quiz taken yet' : `Last quiz score: ${e.lastScore}%`}</p>
            </div>
            <div className="side">
              <ProgressBar value={e.progress} />
              <Link className="btn small" to={`/courses/${e.courseId}/quiz`}>{e.lastScore == null ? 'Start quiz' : 'Retake quiz'}</Link>
            </div>
          </article>
        ))}
      </div>
    </>
  );
}

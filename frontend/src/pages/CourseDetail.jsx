import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import ProgressBar from '../components/ProgressBar.jsx';

export default function CourseDetail() {
  const { id } = useParams();
  const { user, call } = useAuth();
  const navigate = useNavigate();
  const [course, setCourse] = useState(null);
  const [enrollment, setEnrollment] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    api(`/courses/${id}`).then(setCourse).catch((e) => setError(e.message));
    if (user) {
      call('/enrollments/me')
        .then((list) => setEnrollment(list.find((e) => String(e.courseId) === id) ?? null))
        .catch(() => {});
    }
  }, [id, user, call]);

  const enroll = async () => {
    if (!user) return navigate('/login', { state: { from: `/courses/${id}` } });
    try { setEnrollment(await call(`/courses/${id}/enroll`, { method: 'POST' })); }
    catch (e) { setError(e.message); }
  };

  if (error && !course) return <p className="error" role="alert">{error}</p>;
  if (!course) return <p>Loading…</p>;

  return (
    <article className="detail">
      <Link to="/" className="back">All courses</Link>
      <p className="meta">{course.category} · {course.level}</p>
      <h1>{course.title}</h1>
      <p className="lead">{course.description}</p>
      <p className="foot">Taught by {course.instructor} · {course.durationHours} hours</p>
      {error && <p className="error" role="alert">{error}</p>}
      {enrollment ? (
        <div className="enrolled">
          <ProgressBar value={enrollment.progress} />
          <Link className="btn" to={`/courses/${id}/quiz`}>Take the quiz</Link>
        </div>
      ) : (
        <button className="btn" onClick={enroll}>Enroll in this course</button>
      )}
    </article>
  );
}

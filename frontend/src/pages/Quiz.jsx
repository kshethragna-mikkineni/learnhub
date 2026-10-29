import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import QuizRunner from '../components/QuizRunner.jsx';

export default function Quiz() {
  const { id } = useParams();
  const { call } = useAuth();
  const [questions, setQuestions] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    call(`/courses/${id}/quiz`).then(setQuestions).catch((e) => setError(e.message));
  }, [id, call]);

  const submit = (answers) => call(`/courses/${id}/quiz/submit`, { method: 'POST', body: { answers } });

  return (
    <div className="detail">
      <Link to={`/courses/${id}`} className="back">Back to course</Link>
      {error && <p className="error" role="alert">{error}</p>}
      {!questions && !error && <p>Loading quiz…</p>}
      {questions && <QuizRunner questions={questions} onSubmit={submit} />}
    </div>
  );
}

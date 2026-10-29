import { useState } from 'react';

// Shows one question at a time, then submits all answers and shows a review.
export default function QuizRunner({ questions, onSubmit }) {
  const [index, setIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [result, setResult] = useState(null);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');

  if (questions.length === 0) return <p>This course has no quiz yet.</p>;

  if (result) {
    return (
      <section>
        <h2>You scored {result.score} of {result.total}</h2>
        <ol className="review">
          {questions.map((q) => {
            const right = result.correct[q.id];
            const mine = answers[q.id];
            return (
              <li key={q.id} className={mine === right ? 'right' : 'wrong'}>
                <strong>{q.text}</strong>
                <span>Your answer: {q.options[mine] ?? 'No answer'}</span>
                {mine !== right && <span>Correct answer: {q.options[right]}</span>}
              </li>
            );
          })}
        </ol>
        <button className="btn" onClick={() => { setResult(null); setAnswers({}); setIndex(0); }}>Retake quiz</button>
      </section>
    );
  }

  const q = questions[index];
  const last = index === questions.length - 1;

  const submit = async () => {
    setBusy(true); setError('');
    try { setResult(await onSubmit(answers)); }
    catch (e) { setError(e.message); }
    finally { setBusy(false); }
  };

  return (
    <section>
      <p className="meta">Question {index + 1} of {questions.length}</p>
      <h2>{q.text}</h2>
      <div className="options" role="radiogroup" aria-label={q.text}>
        {q.options.map((opt, i) => (
          <label key={i} className={answers[q.id] === i ? 'picked' : ''}>
            <input type="radio" name={`q${q.id}`} checked={answers[q.id] === i}
                   onChange={() => setAnswers({ ...answers, [q.id]: i })} />
            {opt}
          </label>
        ))}
      </div>
      {error && <p className="error" role="alert">{error}</p>}
      <div className="row">
        <button className="btn ghost" disabled={index === 0} onClick={() => setIndex(index - 1)}>Back</button>
        {last
          ? <button className="btn" disabled={busy || answers[q.id] === undefined} onClick={submit}>Submit answers</button>
          : <button className="btn" disabled={answers[q.id] === undefined} onClick={() => setIndex(index + 1)}>Next question</button>}
      </div>
    </section>
  );
}

import { useEffect, useState } from 'react';
import { api } from '../api.js';
import CourseCard from '../components/CourseCard.jsx';

const CATEGORIES = ['All', 'Programming', 'Data', 'Design'];

export default function Courses() {
  const [q, setQ] = useState('');
  const [category, setCategory] = useState('All');
  const [courses, setCourses] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const t = setTimeout(() => {          // debounce typing
      const p = new URLSearchParams();
      if (q) p.set('q', q);
      if (category !== 'All') p.set('category', category);
      api('/courses?' + p.toString()).then(setCourses).catch((e) => setError(e.message));
    }, 250);
    return () => clearTimeout(t);
  }, [q, category]);

  return (
    <>
      <h1>Start Learning. Finish Strong</h1>
      <div className="filters">
        <input type="search" placeholder="Search courses" value={q} onChange={(e) => setQ(e.target.value)}
               aria-label="Search courses" />
        <div className="chips">
          {CATEGORIES.map((c) => (
            <button key={c} className={c === category ? 'chip active' : 'chip'} onClick={() => setCategory(c)}>{c}</button>
          ))}
        </div>
      </div>
      {error && <p className="error" role="alert">{error}</p>}
      {!courses && !error && <p>Loading courses…</p>}
      {courses && courses.length === 0 && <p>No courses match. Clear the search or pick another category.</p>}
      <div className="grid">{courses?.map((c) => <CourseCard key={c.id} course={c} />)}</div>
    </>
  );
}

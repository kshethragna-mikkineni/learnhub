import { Link } from 'react-router-dom';

export default function CourseCard({ course }) {
  return (
    <article className="course">
      <p className="meta">{course.category} · {course.level}</p>
      <h3><Link to={`/courses/${course.id}`}>{course.title}</Link></h3>
      <p className="desc">{course.description}</p>
      <p className="foot">{course.instructor} · {course.durationHours} hours</p>
    </article>
  );
}

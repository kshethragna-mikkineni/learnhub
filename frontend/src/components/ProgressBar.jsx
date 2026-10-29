// Ten blocks; each filled block is 10% of the course.
export default function ProgressBar({ value }) {
  const filled = Math.round(value / 10);
  return (
    <div className="progress" role="progressbar" aria-valuenow={value} aria-valuemin={0} aria-valuemax={100}
         aria-label={`${value}% complete`}>
      {Array.from({ length: 10 }, (_, i) => <span key={i} className={i < filled ? 'on' : ''} />)}
      <b>{value}%</b>
    </div>
  );
}

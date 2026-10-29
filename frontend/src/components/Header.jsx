import { NavLink, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

export default function Header() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  return (
    <header className="header">
      <Link to="/" className="brand">CourseClimb</Link>
      <nav>
        <NavLink to="/" end>Courses</NavLink>
        {user ? (
          <>
            <NavLink to="/dashboard">My learning</NavLink>
            <button className="link" onClick={() => { logout(); navigate('/'); }}>Sign out ({user.name.split(' ')[0]})</button>
          </>
        ) : (
          <>
            <NavLink to="/login">Sign in</NavLink>
            <Link to="/register" className="btn small">Create account</Link>
          </>
        )}
      </nav>
    </header>
  );
}

import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

export default function AuthForm({ mode }) {
  const isRegister = mode === 'register';
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const from = useLocation().state?.from || '/dashboard';
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setBusy(true); setError('');
    try {
      isRegister ? await register(form.name, form.email, form.password) : await login(form.email, form.password);
      navigate(from, { replace: true });
    } catch (err) {
      setError(err.message);
    } finally { setBusy(false); }
  };

  return (
    <form className="auth" onSubmit={onSubmit}>
      <h1>{isRegister ? 'Create your account' : 'Sign in'}</h1>
      {isRegister && (
        <label>Name<input required value={form.name} onChange={set('name')} autoComplete="name" /></label>
      )}
      <label>Email<input type="email" required value={form.email} onChange={set('email')} autoComplete="email" /></label>
      <label>Password
        <input type="password" required minLength={isRegister ? 8 : undefined} value={form.password}
               onChange={set('password')} autoComplete={isRegister ? 'new-password' : 'current-password'} />
      </label>
      {isRegister && <small>Use at least 8 characters.</small>}
      {error && <p className="error" role="alert">{error}</p>}
      <button className="btn" disabled={busy}>{isRegister ? 'Create account' : 'Sign in'}</button>
      <p>{isRegister ? <>Already registered? <Link to="/login">Sign in</Link></> : <>New here? <Link to="/register">Create an account</Link></>}</p>
    </form>
  );
}

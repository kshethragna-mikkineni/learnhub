import { createContext, useContext, useState, useCallback } from 'react';
import { api } from './api.js';

const AuthContext = createContext(null);
export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }) {
  const [session, setSession] = useState(() => {
    try { return JSON.parse(localStorage.getItem('learnhub.session')); } catch { return null; }
  });

  const save = (s) => {
    setSession(s);
    s ? localStorage.setItem('learnhub.session', JSON.stringify(s)) : localStorage.removeItem('learnhub.session');
  };

  const login = async (email, password) => save(await api('/auth/login', { method: 'POST', body: { email, password } }));
  const register = async (name, email, password) =>
    save(await api('/auth/register', { method: 'POST', body: { name, email, password } }));
  const logout = () => save(null);

  // Calls the API with the current token; signs the user out if the token has expired.
  const call = useCallback(async (path, opts = {}) => {
    try {
      return await api(path, { ...opts, token: session?.token });
    } catch (e) {
      if (e.status === 401) save(null);
      throw e;
    }
  }, [session]);

  return (
    <AuthContext.Provider value={{ user: session?.user, token: session?.token, login, register, logout, call }}>
      {children}
    </AuthContext.Provider>
  );
}

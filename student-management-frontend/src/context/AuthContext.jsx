import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
} from 'react';

import api from '../api/axios';

const AuthContext = createContext(null);

const VALID_ROLES = ['ADMIN', 'TEACHER', 'STUDENT'];

function normalizeRole(role) {
  const value = String(role || '')
    .toUpperCase()
    .replace(/^ROLE_/, '');

  return VALID_ROLES.includes(value) ? value : '';
}

function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null');
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(
    () => localStorage.getItem('token') || ''
  );

  const [user, setUser] = useState(getStoredUser);

  const login = useCallback(async (email, password) => {
    const { data = {} } = await api.post('/auth/login', {
      email: email.trim(),
      password,
    });

    const jwt =
      data.token ||
      data.accessToken ||
      data.jwt ||
      data.access_token;

    if (!jwt) {
      throw new Error('Login response did not contain a token.');
    }

    const role = normalizeRole(data.role || data.user?.role);

    if (!role) {
      throw new Error(
        'Login response does not contain a supported role.'
      );
    }

    const nextUser = {
      email: data.email || data.user?.email || email.trim(),
      role,
    };

    localStorage.setItem('token', jwt);
    localStorage.setItem('user', JSON.stringify(nextUser));

    setToken(jwt);
    setUser(nextUser);

    return nextUser;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    setToken('');
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token && user?.role),
      login,
      logout,
    }),
    [token, user, login, logout]
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider');
  }

  return context;
}
import { describe, it, expect, beforeEach } from 'vitest';
import authReducer, { setCredentials, logout } from '../store/slices/authSlice';

describe('Auth Reducer', () => {
  const dummyAuthResponse = {
    accessToken: 'jwt-access-token-123',
    refreshToken: 'jwt-refresh-token-456',
    expiresIn: 3600,
    user: {
      id: 'u-1',
      email: 'dev@aicode.com',
      fullName: 'Jane Developer',
      role: 'DEVELOPER' as const,
      emailVerified: true,
      createdAt: '2026-01-01T00:00:00Z',
      updatedAt: '2026-01-01T00:00:00Z',
    },
  };

  beforeEach(() => {
    localStorage.clear();
  });

  it('handles setCredentials and updates state and localStorage', () => {
    const initialState = {
      user: null,
      token: null,
      refreshToken: null,
      isAuthenticated: false,
      isLoading: false,
    };

    const nextState = authReducer(initialState, setCredentials(dummyAuthResponse));
    expect(nextState.isAuthenticated).toBe(true);
    expect(nextState.user?.email).toBe('dev@aicode.com');
    expect(nextState.token).toBe('jwt-access-token-123');
    expect(localStorage.getItem('token')).toBe('jwt-access-token-123');
  });

  it('handles logout and clears storage', () => {
    const loggedInState = {
      user: dummyAuthResponse.user,
      token: dummyAuthResponse.accessToken,
      refreshToken: dummyAuthResponse.refreshToken,
      isAuthenticated: true,
      isLoading: false,
    };

    const nextState = authReducer(loggedInState, logout());
    expect(nextState.isAuthenticated).toBe(false);
    expect(nextState.user).toBeNull();
    expect(nextState.token).toBeNull();
    expect(localStorage.getItem('token')).toBeNull();
  });
});

import GoogleLeaderboards from './NativeGoogleLeaderboards';

export function login(): Promise<string> {
  return GoogleLeaderboards.login();
}

export function login_v2(): Promise<string> {
  return GoogleLeaderboards.login_v2();
}

export function checkAuth(): Promise<string> {
  return GoogleLeaderboards.checkAuth();
}

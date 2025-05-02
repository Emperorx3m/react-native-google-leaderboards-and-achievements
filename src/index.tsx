import GoogleLeaderboards from './NativeGoogleLeaderboards';

export function login(): Promise<string> {
  return GoogleLeaderboards.login();
}


export function checkAuth(): Promise<string> {
  return GoogleLeaderboards.checkAuth();
}


export function submitScore(leaderboardId: string, score: number): Promise<string> {
  return GoogleLeaderboards.submitScore(leaderboardId, score);
}


export function onShowLeaderboardsRequested(): Promise<string> {
  return GoogleLeaderboards.onShowLeaderboardsRequested();
}


export function showLeaderboard(leaderboardId: string): Promise<string> {
  return GoogleLeaderboards.showLeaderboard(leaderboardId);
}
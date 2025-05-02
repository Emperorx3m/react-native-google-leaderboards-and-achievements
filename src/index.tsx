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

// achievements section

export function showAchievements(): Promise<string> {
  return GoogleLeaderboards.showAchievements();
}
 
export function incrementAchievement(my_achievement_id: string, steps: number): Promise<string> {
  return GoogleLeaderboards.incrementAchievement(my_achievement_id, steps);
}

export function unlockAchievement(my_achievement_id: string): Promise<string> {
  return GoogleLeaderboards.unlockAchievement(my_achievement_id);
}
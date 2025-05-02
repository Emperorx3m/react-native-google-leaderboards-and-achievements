"use strict";

import GoogleLeaderboards from './NativeGoogleLeaderboards';
export function login() {
  return GoogleLeaderboards.login();
}
export function checkAuth() {
  return GoogleLeaderboards.checkAuth();
}
export function submitScore(leaderboardId, score) {
  return GoogleLeaderboards.submitScore(leaderboardId, score);
}
export function onShowLeaderboardsRequested() {
  return GoogleLeaderboards.onShowLeaderboardsRequested();
}
export function showLeaderboard(leaderboardId) {
  return GoogleLeaderboards.showLeaderboard(leaderboardId);
}

// achievements section

export function showAchievements() {
  return GoogleLeaderboards.showAchievements();
}
export function incrementAchievement(my_achievement_id, steps) {
  return GoogleLeaderboards.incrementAchievement(my_achievement_id, steps);
}
export function unlockAchievement(my_achievement_id) {
  return GoogleLeaderboards.unlockAchievement(my_achievement_id);
}
//# sourceMappingURL=index.js.map
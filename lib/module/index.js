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
//# sourceMappingURL=index.js.map
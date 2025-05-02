export declare function login(): Promise<string>;
export declare function checkAuth(): Promise<string>;
export declare function submitScore(leaderboardId: string, score: number): Promise<string>;
export declare function onShowLeaderboardsRequested(): Promise<string>;
export declare function showLeaderboard(leaderboardId: string): Promise<string>;
export declare function showAchievements(): Promise<string>;
export declare function incrementAchievement(my_achievement_id: string, steps: number): Promise<string>;
export declare function unlockAchievement(my_achievement_id: string): Promise<string>;
//# sourceMappingURL=index.d.ts.map
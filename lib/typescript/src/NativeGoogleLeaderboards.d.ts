import type { TurboModule } from 'react-native';
export interface Spec extends TurboModule {
    login(): Promise<string>;
    checkAuth(): Promise<string>;
    submitScore(leaderboardId: string, score: number): Promise<string>;
    onShowLeaderboardsRequested(): Promise<string>;
    showLeaderboard(leaderboardId: string): Promise<string>;
    showAchievements(): Promise<string>;
    unlockAchievement(my_achievement_id: string): Promise<string>;
    incrementAchievement(my_achievement_id: string, steps: number): Promise<string>;
}
declare const _default: Spec;
export default _default;
//# sourceMappingURL=NativeGoogleLeaderboards.d.ts.map
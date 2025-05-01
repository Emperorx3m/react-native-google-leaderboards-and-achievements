import type { TurboModule } from 'react-native';
export interface Spec extends TurboModule {
    login(): Promise<string>;
    login_v2(): Promise<string>;
    checkAuth(): Promise<string>;
}
declare const _default: Spec;
export default _default;
//# sourceMappingURL=NativeGoogleLeaderboards.d.ts.map
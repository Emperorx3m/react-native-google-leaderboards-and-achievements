import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  login(): Promise<string>;
  login_v2(): Promise<string>;
  checkAuth(): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('GoogleLeaderboards');

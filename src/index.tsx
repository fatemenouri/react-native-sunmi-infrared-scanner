import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-sunmi-infrared-scanner' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const SunmiInfraredScanner = NativeModules.SunmiInfraredScanner
  ? NativeModules.SunmiInfraredScanner
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function infraredScan(): Promise<any> {
  return SunmiInfraredScanner.openInfraredScanner();
}

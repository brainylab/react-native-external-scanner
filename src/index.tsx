import {NativeModules} from 'react-native';

export {default as ExternalScanner} from './ReactNativeExternalScannerViewNativeComponent';
export * from './ReactNativeExternalScannerViewNativeComponent';

export * from './hooks/useExternalScanner';

const {ReactNativeExternalScannerModule} = NativeModules;

export async function hasExternalScanner(): Promise<boolean> {
  const response = await ReactNativeExternalScannerModule.hasExternalKeyboard();

  return response;
}

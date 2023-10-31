import React, {useCallback, useEffect, useState} from 'react';
import {NativeModules} from 'react-native';
import type {NativeSyntheticEvent, StyleProp, ViewStyle} from 'react-native';

import NativeExternalScanner from './ReactNativeExternalScannerViewNativeComponent';
import type {Event} from './ReactNativeExternalScannerViewNativeComponent';

const {ReactNativeExternalScannerModule} = NativeModules;

export async function hasExternalScanner(): Promise<boolean> {
  const response = await ReactNativeExternalScannerModule.hasExternalKeyboard();

  return response;
}

export function useExternalScanner() {
  const [scannerConnected, setScannerConnected] = useState(false);

  useEffect(() => {
    async function existExternalScanner() {
      const response = await hasExternalScanner();

      setScannerConnected(response);
    }

    existExternalScanner();
  }, []);

  return {scannerConnected};
}

export type ExternalScannerProps = {
  children: React.ReactNode;
  style?: StyleProp<ViewStyle> | undefined;
  active?: boolean;
  focusable?: boolean;
  onCodeScanned?: (code: string) => void;
  onSingleCodeScanned?: (code: string) => void;
  onWithoutFocus?: () => void;
};

export const ExternalScanner = ({
  children,
  active = true,
  onCodeScanned,
  onWithoutFocus,
  onSingleCodeScanned,
  ...props
}: ExternalScannerProps) => {
  const onNativeCodeScanned = useCallback(
    ({nativeEvent}: NativeSyntheticEvent<Event>) => {
      if (nativeEvent && nativeEvent?.value) {
        const code = nativeEvent.value;
        onCodeScanned && onCodeScanned(code);
      }
    },
    [onCodeScanned],
  );

  const onNativeSingleCodeScanned = useCallback(
    ({nativeEvent}: NativeSyntheticEvent<Event>) => {
      if (nativeEvent && nativeEvent?.value) {
        const code = nativeEvent.value;
        onSingleCodeScanned && onSingleCodeScanned(code);
      }
    },
    [onSingleCodeScanned],
  );

  const onNativeWithoutFocus = useCallback(
    () => onWithoutFocus && onWithoutFocus(),
    [onWithoutFocus],
  );

  return (
    <NativeExternalScanner
      active={active}
      onCodeScanned={onNativeCodeScanned}
      onSingleCodeScanned={onNativeSingleCodeScanned}
      onWithoutFocus={onNativeWithoutFocus}
      {...props}>
      {children}
    </NativeExternalScanner>
  );
};

import type {ViewProps} from 'react-native';
import type {DirectEventHandler} from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

export type Event = {
  value: string;
};

interface NativeProps extends ViewProps {
  active?: boolean;
  onCodeScanned?: DirectEventHandler<Event>;
  onSingleCodeScanned?: DirectEventHandler<Event>;
  onWithoutFocus?: DirectEventHandler<Event>;
}
export default codegenNativeComponent<NativeProps>(
  'ReactNativeExternalScannerView',
);

<h1 align="center">React Native External Scanner</h1>

This library was developed to support the new architecture of React Native - Fabric, it does not support old versions of react native.

> Support only New Architecture an react-native > 0.71.0

## Installation

```bash
# use npm
npm install @brainylab/react-native-external-scanner
# or use yarn
yarn add @brainylab/react-native-external-scanner
# or use pnpm
pnpm i @brainylab/react-native-external-scanner
```
# Enable the New Architecture
### on android
You will only need to update your `android/gradle.properties` file as follows:

```diff
-newArchEnabled=false
+newArchEnabled=true
```

<!-- ### on ios
You will only need to reinstall your pods by running `pod install` with the right flag:
```bash
# Run pod install with the flag:
RCT_NEW_ARCH_ENABLED=1 bundle exec pod install
``` -->

## Usage


```typescript
import * as React from 'react';
import {Text, TouchableOpacity, View} from 'react-native';

import {
  ExternalScanner,
  useExternalScanner,
} from '@brainylab/react-native-external-scanner';

type ItemProps = {
  index: number;
};

function Item({index}: ItemProps) {
  const [active, setActive] = React.useState(false);
  const [valueText, setValueText] = React.useState('');

  const {scannerConnected} = useExternalScanner();

  return (
    <ExternalScanner
      active={active}
      style={{
        flex: 1,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        height: '100%',
      }}
      onWithoutFocus={() => {
        console.log(index);
        setActive(false);
      }}
      onCodeScanned={value => {
        setValueText(value);
      }}
      onSingleCodeScanned={value => {
        console.log(value);
      }}>
      <View>
        <Text style={{textAlign: 'center', marginBottom: 10}}>
          scanner connected: {scannerConnected ? 'yes' : 'no'}
        </Text>
      </View>

      <View>
        <Text style={{textAlign: 'center', marginBottom: 10}}>
          value scanned: {valueText}
        </Text>
      </View>

      <View>
        <Text style={{textAlign: 'center', marginBottom: 30}}>
          status: {active ? 'activated' : 'disabled'}
        </Text>
      </View>

      <TouchableOpacity
        style={{backgroundColor: 'green', padding: 8, width: '50%'}}
        onPress={() => setActive(prev => !prev)}>
        <Text style={{textAlign: 'center', color: 'white'}}>
          Activate/Disable
        </Text>
      </TouchableOpacity>
    </ExternalScanner>
  );
}

export default function App() {
  return (
    <>
      <Item index={1} />
      <Item index={2} />
    </>
  );
}

```

### API

| APIs  | Value  | iOS | Android |
| -------------- | -------------  | -------------- | --------------- |
| `focusable` |  activates option to manually focus the view, `default: false` | ❌  | ✅ |
| `active` |  active scanner in view, `default: true` | ❌  | ✅ |
| `onCodeScanned` |  Receives the value of the External Scanner | ❌  | ✅ |
| `onSingleCodeScanned` |  Receives the single value of the External Scanner | ❌  | ✅ |
| `onWithoutFocus` |  receives event when the view loses focus | ❌  | ✅ |

### Examples

The source code for the example (showcase) app is under the Example/ directory. If you want to play with the API but don't feel like trying it on a real app, you can run the example project.

## License

MIT

---

Development by [BrainyLab Development](https://brainylab.com.br)

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
        console.log(value === 'A000A');
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

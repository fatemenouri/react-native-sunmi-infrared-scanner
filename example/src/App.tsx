import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import { infraredScan } from 'react-native-sunmi-infrared-scanner';

export default function App() {
  async function infraredScanner() {
    let result = await infraredScan();
    console.log('result:', result);
  }

  return (
    <View style={styles.container}>
      <Button title="infrared scan" onPress={infraredScanner}></Button>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});

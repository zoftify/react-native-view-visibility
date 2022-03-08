import * as React from 'react';

import { StyleSheet, View, Image } from 'react-native';
import ViewVisibility from 'react-native-view-visibility';

export default function App() {
  return (
    <View style={styles.container}>
      <ViewVisibility
        idBanner={'12345678'}
        style={styles.box}
        onAdVisibleChange={(visibilityType: number) =>
          console.log('The visibility has changed', visibilityType)
        }
      >
        <Image
          source={{
            uri: 'https://computingforgeeks.com/wp-content/uploads/2018/10/clear-log-file-linux-min.png?ezimgfmt=ng:webp/ngcb23',
          }}
          style={{ width: 300, height: 100 }}
        />
      </ViewVisibility>
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

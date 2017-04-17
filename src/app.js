/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React,{Component} from "react";
import {
  AppRegistry,
  Platform,
  TouchableOpacity,
  StyleSheet,
  Navigator,
  View,
  Image,
  Text,
} from 'react-native';
import Score from './Score';
import ViewPager from "./ViewPager";
import RefreshControl from "./RefreshControl";
import Linking from "./Linking";

const defaultRoute = {
  title:"平通车",
  rightButton:true,
  component: Score
};
//=============================================================================


var tmp = 123;

if (true) {
    tmp123 = 'abc'; // ReferenceError
    console.log(tmp123);
    let tmp;
    tmp = "cccc";
    console.log(tmp);
}


var obj = {
    p: [
        'Hello',
        { y: 'World' }
    ]
};

var { p: [x, { y }] } = obj;
console.log(x+" "+y+" "+obj.p);




var node = {
    loc: {
        start: {
            line: 1,
            column: 5
        }
    }
};

var { loc: { start: { line,column }} } = node;

console.log(line+" "+column);













//=============================================================================
class navigation extends Component {
  _renderScene(route, navigator) {
    let Component = route.component;
    return (
      <Component {...route.params} navigator={navigator} />
    );
  }
  _renderNavBar() {
    const styles = {
      title: {
        flex: 1, alignItems: 'flex-start', justifyContent: 'center'
      },
      icon:{
        width:30,height:30
      },
      button: {
        flex: 1, width: 50, alignItems: 'center', justifyContent: 'center'
      },
      buttonText: {
        fontSize: 18, color: '#FFFFFF'
      }
    }
    var routeMapper = {
      LeftButton(route, navigator, index, navState) {
        if(index > 0) {
          return (
            <TouchableOpacity
              onPress={() => navigator.pop()}
              style={styles.button}>
              <Text style={styles.buttonText}>Back</Text>
            </TouchableOpacity>
          );
        } else {
          return (
            <TouchableOpacity
              onPress={() => navigator.pop()}
              style={styles.button}>
              <Image
                  style={styles.icon}
                  source={require('../img/favicon.png')}
                />
            </TouchableOpacity>
          );
        }
      },
      RightButton(route, navigator, index, navState) {

        console.log(index+" "+route.rightButton);

        if(index > 0 && route.rightButton) {
          return (
            <TouchableOpacity
              onPress={() => navigator.pop()}
              style={styles.button}>
              <Text style={styles.buttonText}>Second</Text>
            </TouchableOpacity>
          );
        } else {
          return null;
        }
      },
      Title(route, navigator, index, navState) {
        return (
          <View style={styles.title}>
            <Text style={styles.buttonText}>{route.title ? route.title : '积分'}</Text>
          </View>
        );
      }
    };
    return (
      <Navigator.NavigationBar
        style={{
          alignItems: 'center',
          backgroundColor: '#1E8CD4',
          height:55,
          shadowOffset:{
              width: 1,
              height: 0.1,
          },
          shadowColor: '#1E8CDD',
          shadowOpacity: 0.1,
          }}
        routeMapper={routeMapper}
      />
    );
  }
  render() {
    return (
      <Navigator
        initialRoute={defaultRoute}
        renderScene={this._renderScene}
        sceneStyle={{paddingTop: (Platform.OS === 'android' ? 55 : 74)}}
        navigationBar={this._renderNavBar()}

        />
    );
  }
}


AppRegistry.registerComponent('ReNaProject', () => navigation);

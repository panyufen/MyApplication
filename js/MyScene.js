  import React, { Component,PropTypes } from 'react';
import { View,Image,StyleSheet, Text,TouchableHighlight } from 'react-native';

const styles = StyleSheet.create({
  bigblue: {

    fontWeight: 'bold',
    fontSize: 30,
  },
  red: {
    color: 'red',
  },
  icon:{
    width:30,
    height:30,
    margin:10
  },
  blue:{
     backgroundColor: '#1E8CD4'
  }
});


export default class MyScene extends Component {
  static propTypes = {
      title: PropTypes.string.isRequired,
    }
    render() {
      return (
        <View style={{
                flexDirection: 'row',
                backgroundColor: '#1E8CD4',
                }}>
            <Image
                style={styles.icon}
                source={require('../img/ic_pt_logo.png')}
              />

            <Text style={{color:"white",fontSize:20,textAlignVertical:"center"}} >平通车</Text>

        </View>
      )
    }

}

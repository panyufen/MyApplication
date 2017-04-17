/**

* Sample React Native App

* https://github.com/facebook/react-native

* @flow

*/



import React, { Component } from 'react';

import {

 AppRegistry,

 StyleSheet,

 Text,

 WebView,

 View,

 ToastAndroid,

 ToolbarAndroid

} from 'react-native';
class VideoPage extends Component {

 constructor(props) {

   super(props);



   this.state = {
    actionText:"aaaaaa",

     isloading:true,

   };

 }

 _onActionSelected(position){
   this.setState({
       actionText: 'Selected ' + toolbarActions[position].title,
     });

 }




 render() {





   return (

     <View style={styles.container}>

     <ToolbarAndroid

               actions={toolbarActions}

               onActionSelected={(p)=>{this._onActionSelected(p)}}

              textID = {"标题蓝"}

               style={styles.toolBar}

               navIcon={require('../img/ic_pt_logo.png')}

               title={this.props.title}

               titleColor='white'

               subtitle={this.state.actionText}

               onIconClicked={() => this.backAndroid()}/>


     </View>

   );

 }



 backAndroid(){

      this.props.navigator.pop();

 }



}

var toolbarActions = [
  {title: 'Create', icon: require('../img/ic_car_manage_car_info.png'), show: 'always'},
  {title: 'Filter'},
  {title: 'Settings', icon: require('../img/ic_car_manage_car_info.png'), show: 'always'},
];

const styles = StyleSheet.create({

 container: {

   flex: 1,

   backgroundColor: '#ffffff',

 },

  toolBar: {

   backgroundColor: '#232320',

   height: 56,

 },



});



export default VideoPage;

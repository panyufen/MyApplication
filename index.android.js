// import React, { Component } from 'react';
// import { AppRegistry,Navigator, ListView, Text, View,DeviceEventEmitter,Subscribable } from 'react-native';
// import MyScene from './js/MyScene';
// import PTLog from "./js/PTLog";
//
//
// class ReNaProject extends Component {
//   // 初始化模拟数据
//   constructor(props) {
//     super(props);
//     const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
//     this.state = {
//       dataSource: ds.cloneWithRows([
//         'John', 'Joel', 'James', 'Jimmy', 'Jackson', 'Jillian', 'Julie', 'Devin'
//       ])
//     };
//
//     var request = new XMLHttpRequest();
//     request.onreadystatechange = (e) => {
//       if (request.readyState !== 4) {
//         return;
//       }
//
//       if (request.status === 200) {
//         console.log('success', request.responseText);
        // PTLog.i("success PT");
//       } else {
//         console.warn('error');
//       }
//     };
//
//     request.open('GET', 'https://www.pt18.cn/webapi/v2.5/ver');
//     request.send();
//
//
//
//
//
//
//   }
//   render() {
//     return (
//       <View
//         style={{flex: 1, flexDirection: 'column'}} >
//           <MyScene
//             title= {"平通车"}
//
//            />
//         <ListView
//           style={{backgroundColor:'darkcyan',height:500}}
//           alignItems='center'
//           dataSource={this.state.dataSource}
//           renderRow={(rowData) => <Text style={{height:200,flex:1} }>{rowData}</Text>}
//         />
//       </View>
//     );
//   }
// }
//
// // 注意，这里用引号括起来的'HelloWorldApp'必须和你init创建的项目名一致
// AppRegistry.registerComponent('ReNaProject', () => ReNaProject);
require('./src/app');

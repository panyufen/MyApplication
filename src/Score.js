import React,{Component} from "react";
import {
	View,
	Text,
	ListView,
	Image,
	TouchableHighlight,
	NativeModules,
	ToastAndroid,
	TouchableOpacity
} from 'react-native';
import ItemView from "./ItemView";

import Login from './Login';
import PTLog from "../js/PTLog";
import PTDialog from "../js/PTDialog";
import Activity from "../js/ActivityAndroid";
var Toast = NativeModules.ToastAndroid;

class Score extends React.Component {

	constructor(props) {
		super(props);
		const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
		var rows = [
			{"id":"1","name":"玻璃水","ns":100,"total":100,"rest":84,"url":"http://www.pt18.cn/ptpgps/upload/20160401091004.jpg","et":"2017-12-23 0:00:00","owner":"车联网平台","type":"1"},
			{"id":"2","name":"润滑油","ns":100,"total":100,"rest":99,"url":"http://www.pt18.cn/ptpgps/upload/20160401091352.jpg","et":"2019-12-26 0:00:00","owner":"车联网平台","type":"1"},
			{"id":"3","name":"轮胎","ns":100,"total":100,"rest":99,"url":"http://www.pt18.cn/ptpgps/upload/20160401091418.jpg","et":"2019-11-3 0:00:00","owner":"车联网平台","type":"1"},
			{"id":"4","name":"折扣券","ns":300,"total":100,"rest":95,"url":"http://www.pt18.cn/ptpgps/upload/20160401091555.jpg","et":"2019-9-1 0:00:00","owner":"车联网平台","type":"0"},
			{"id":"5","name":"优惠劵","ns":300,"total":100,"rest":94,"url":"http://www.pt18.cn/ptpgps/upload/20160401091708.jpg","et":"2019-11-6 0:00:00","owner":"车联网平台","type":"0"},
			{"id":"6","name":"昆仑山矿泉水","ns":50,"total":100,"rest":93,"url":"http://www.pt18.cn/ptpgps/upload/20160401092232.jpg","et":"2019-11-6 0:00:00","owner":"车联网平台","type":"1"}
		];
		this.state = {
	    dataSource: ds.cloneWithRows(rows),
			score:0,
			rows:rows,
			time:2
	  };

		// List<carData> carlist =new ArrayList<>();
    // carlist.add(new carData("a "," b"," asdff"));
    // carlist.add(new carData("a "," b"," asdff"));
    // carlist.add(new carData("a "," b"," asdff"));
    // carlist.add(new carData("a "," b"," asdff"));


		// this._requestShop();

		this._requestScore();
	}


	_requestShop(){
		var request1 = new XMLHttpRequest();
    request1.onreadystatechange = (e) => {
      if (request1.readyState !== 4) {
        return;
      }

      if (request1.status === 200) {
        console.log('success', request1.responseText);
				var result1 = eval("("+request1.responseText+")");
				// PTLog.i("ptlog "+result.r+" "+result.d);
				this._updateListView(result1.d);

      } else {
        console.warn('error');
      }
    };
		request1.open('GET', 'https://www.pt18.cn/webapi/v2.5/app.ashx?action=getshop&id=466');
    request1.send();
	}

	_requestScore(){
		var request = new XMLHttpRequest();
    request.onreadystatechange = (e) => {
      if (request.readyState !== 4) {
        return;
      }
      if (request.status === 200) {
        console.log('success', request.responseText);
				var resultScore = eval("("+request.responseText+")");
				// PTLog.i("ptlog "+result.r+" "+result.d);
				this._updateScore(resultScore.s);

      } else {
        console.warn('error');
      }
    };
		request.open('GET', 'https://www.pt18.cn/webapi/v2.5/app.ashx?action=getscoring&id=466&vid=1445');
    request.send();

	}

	_updateScore( score ){
		this.setState({value:score})
	}

	_updateListView( newData ){
		PTLog.i("load newData "+newData.length);
		this.state.rows = this.state.rows.concat(newData);

		this.setState({
			dataSource: this.state.dataSource.cloneWithRows(this.state.rows),
		});
	}


	selectCar(){
		// zipPath：zip的路径
		// documentPath：解压到的目录
		Zip.unzip(zipPath, documentPath, (err)=>{
			if (err) {
				// 解压失败
			} else {
				// 解压成功，将zip删除
				fs.unlink(zipPath).then(() => {
					// 通过解压得到的补丁文件生成最新版的jsBundle
				});
			}
		});





			// this.carBrand = a;
			// this.carSeries = b;
			// this.carNumber = c;
			var list = [
				{"carBrand":"aa","carSeries":"bb","carNumber":"cc"},
				{"carBrand":"aa","carSeries":"bb","carNumber":"cc"},
				{"carBrand":"aa","carSeries":"bb","carNumber":"cc"},
				{"carBrand":"aa","carSeries":"bb","carNumber":"cc"},
				{"carBrand":"aa","carSeries":"bb","carNumber":"cc"}
		];
			PTDialog.show(
				list,
				(index)=>{
					this._callBack(index);
				}
			);
	}




	_callBack(index){

			Toast.show("点击了 "+index,Toast.SHORT);

	}



	_openPage() {
		this.props.navigator.push({
			title: 'Login',
			rightButton:true,
			component: Login
		})
	}

	_onListViewItemClick(sectionID,rowID,highlightRow){
			Activity.startActivity("com.renaproject.SecondActivity",(msg)=>{
					Toast.show(msg,Toast.SHORT);
			});
	}


	_getItem(rowData, sectionID, rowID, highlightRow){
		return(
				<TouchableHighlight onPress={()=>{
					PTLog.i("点击了 "+sectionID+" "+rowID+" "+highlightRow);
					NativeModules.ToastAndroid.show("点击了 "+sectionID+" "+rowID,NativeModules.ToastAndroid.SHORT);
					this._onListViewItemClick(sectionID,rowID,highlightRow);


				}}>
				<View style={{
					flex:1,
					flexDirection:"row",
					height:60,
					backgroundColor:"#fff",
					paddingLeft:20,
					paddingRight:20,
					alignItems:"center",
					borderBottomWidth:0.5,
					borderBottomColor:"#949494"}} >
						<Image
							style={{width:40,height:40,marginRight:10,borderRadius:20}}
							source={{uri:rowData.url}}
						/>
						<Text style={{color:"#404040",fontSize:18}}>{rowData.name}</Text>

						<View style={{flex:1,flexDirection:"row",justifyContent:"flex-end"}}>
								<Text style={{fontSize:24,color:"#404040",marginRight:5,fontWeight:"bold"}}>{rowData.ns}</Text>
								<View style={{flexDirection:"row",alignItems:"center"}}>
									<Text style={{fontSize:12,color:"#949494"}}>积分</Text>
								</View>
						</View>



				</View>
				</TouchableHighlight>
		);
	}

	// <TouchableOpacity onPress={this._openPage.bind(this)}>
	// 	<Text style={{ color: '#55ACEE' }}>Open New Page</Text>
	// </TouchableOpacity>
	_scrollEndCall(){
		var that = this;
		PTLog.i("load scrollend ");
		setTimeout(function(){
			PTLog.i("load setTimeout start");
			that._requestShop();
		},2000);

		// setInterval("alert('aaaa')",1000);

	}

	// anim(){
		// if( this.state.time >= 1 ){
		// 	var tempTime = this.state.time-1;
		// 	this.setState({time:tempTime});
		// }else{
			// clearInterval(this.interval);
		// }
	// }

	render() {
		return (
			<View style={{flex:1,flexDirection:"column",backgroundColor: '#EDF0F0' }}>
				<ItemView icon={require("../img/ic_car_manage_car_info.png")} title={"选择车辆"} style={{height:55}} parent={this} clickFunc={this.selectCar.bind(this)}></ItemView>
				<ItemView icon={require("../img/icon_tab_point_focus.png")} name={"当前可用虚拟积分"} style={{height:55}} canTouch={false} enable={false} value={this.state.value}></ItemView>
				<View style={{marginTop:20,borderTopColor:"#949494",borderTopWidth:0.5}}></View>
				<ListView
	        style={{flex:1}}
	        dataSource={this.state.dataSource}
	        renderRow={(rowData, sectionID, rowID, highlightRow)=>{return this._getItem(rowData, sectionID, rowID, highlightRow)}}
					onEndReached={()=>{
							// if( this.state.rows.length > 6 ){
								this._scrollEndCall();
							// }
					}}
					renderFooter={()=>{
						return(
							<View style={{height:60,flex:1,flexDirection:"row",alignItems:"center",justifyContent:"center"}}>
								<Text style={{color:"#000",fontSize:20,}}>{this.state.rows.length}</Text>
								<Text style={{color:"#000",fontSize:12,marginLeft:20}}>{this.state.time}</Text>
								</View>
						)
					}}
	      />
			</View>
		);
	}
}

export default Score;

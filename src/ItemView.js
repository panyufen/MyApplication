import React, { Component,PropTypes } from 'react';

import {
    View,
    Image,
    Text,
    StyleSheet,
    TouchableHighlight,
    NativeModules,
    } from "react-native";
import PTLog from "../js/PTLog";
import PTDialog from "../js/PTDialog";

var Toast = NativeModules.ToastAndroid;


const styles = StyleSheet.create({
    bigblue: {

        fontWeight: 'bold',
        fontSize: 30,
    },
    red: {
        color: 'red',
    },
    icon: {
        width: 20,
        height: 20,
        margin: 10
    },
    blue: {
        backgroundColor: '#1E8CD4'
    }
});

export default class ItemView extends Component {
    constructor(props) {
        super(props);
    }

    // static propTypes = {
    //   title: PropTypes.string,
    //   name: PropTypes.string,
    //   canTouch: PropTypes.bool,
    //   value:PropTypes.string,
    //   icon:PropTypes.number,
    //   clickFunc:PropTypes.func,
    // }

    tempvalue = 0;


    _touchEvent() {
        Toast.show("点击了" + this.tempvalue, Toast.SHORT);
        // PTDialog.show();
    }

    render() {

        if (typeof(this.props.enable) != "undefined" && !this.props.enable) {
            return this._showItem();

        } else {
            return (
                <TouchableHighlight onPress={()=>{this.props.clickFunc(); }}>
                    {this._showItem()}
                </TouchableHighlight>

            )
        }
    }

    _showItem() {
        // var url = "ic_car_manage_car_info.png";
        var source = this.props.icon ? this.props.icon : require("../img/ic_car_manage_car_info.png");
        return (
            <View style={this.props.style?this.props.style:null}>
                <View style={{
                    flex:1,
                    flexDirection: 'row',
                    backgroundColor: 'white',
                    alignItems:"center",
                    paddingLeft:10,
                    paddingRight:10,borderBottomWidth:0.5,borderBottomColor:"#949494"
                    }}>
                    <Image
                        style={styles.icon}
                        source={source}
                        />
                    {this._showContent()}
                </View>
            </View>
        )
    }

    _showContent() {
        if (typeof(this.props.title) != "undefined" && typeof(this.props.name) != "undefined") {
            return (
                <View style={{flex:1,flexDirection: 'row',alignItems:"center"}}>
                    <Text
                        style={{color:"#404040",fontSize:16,fontWeight:"bold",textAlignVertical:"center"}}>{this.props.title}</Text>
                    <View style={{flex:1,justifyContent:"flex-end",flexDirection:"row"}}>
                        <Text style={{color:"#404040",fontSize:16,textAlignVertical:"center"}}>{this.props.name}</Text>
                        {this._showCanTouch()}
                    </View>
                </View>
            )
        } else if (typeof(this.props.title) != "undefined") {
            return (
                <View style={{flex:1,flexDirection: 'row',alignItems:"center"}}>
                    <Text
                        style={{color:"#404040",fontSize:16,fontWeight:"bold",textAlignVertical:"center"}}>{this.props.title}</Text>
                    <View style={{flex:1,justifyContent:"flex-end",flexDirection:"row"}}>
                        {this._showCanTouch()}
                    </View>
                </View>
            )
        } else if (typeof(this.props.name) != "undefined") {
            return (
                <View style={{flex:1,flexDirection: 'row',alignItems:"center"}}>
                    <Text style={{color:"#404040",fontSize:16,textAlignVertical:"center"}}>{this.props.name}</Text>
                    <View style={{flex:1,justifyContent:"flex-end",flexDirection:"row"}}>
                        {this._showCanTouch()}
                    </View>
                </View>
            )
        }
    }

    _showCanTouch() {
        if (typeof(this.props.canTouch) != "undefined" && !this.props.canTouch) {
            if (typeof(this.props.value) != "undefined") {
                return (
                    <View style={{flex:1,flexDirection:"row",justifyContent:"flex-end"}}>
                        <Text
                            style={{fontSize:24,color:"#404040",marginRight:5,fontWeight:"bold"}}>{this.props.value}</Text>
                        <View style={{flexDirection:"row",alignItems:"center"}}>
                            <Text style={{fontSize:12,color:"#949494"}}>积分</Text>
                        </View>
                    </View>
                )
            } else {
                return null;
            }

        } else {
            return (
                <Image
                    style={{width:30,height:30,margin:10}}
                    source={require('../img/icon_next_normal.png')}
                    />
            )

        }
    }

}

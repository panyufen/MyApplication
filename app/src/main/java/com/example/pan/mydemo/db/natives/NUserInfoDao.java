package com.example.pan.mydemo.db.natives;

import android.database.Cursor;

import com.example.pan.mydemo.pojo.UserInfo;

/**
 * Created by PAN on 2017/12/14.
 */

public class NUserInfoDao extends DatabaseDao {


    public NUserInfoDao() {
        super();
    }

    @Override
    public void create(String table) {
        super.create("userinfo");
    }

    @Override
    Object getObject(Cursor cursor) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Long.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
        userInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
        userInfo.setAge(cursor.getString(cursor.getColumnIndex("age")));
        userInfo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
        userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        return userInfo;
    }
}

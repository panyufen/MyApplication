package com.example.pan.mydemo.db.natives;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.pojo.UserInfo;

/**
 * Created by PAN on 2017/12/14.
 */
public class NUserInfoDao extends DatabaseDao {

    @Override
    protected String getTableName() {
        return "userinfo";
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String sql = getCreateTableSql(table);
        LogUtils.i(sql);
        db.execSQL(sql);
    }

    private String getCreateTableSql(String tableName) {
        return "create table if not exists " + tableName + "(id INTEGER PRIMARY KEY  AUTOINCREMENT,userid INTEGER,name TEXT,age INTEGER,mobile TEXT"
                + ",address TEXT);createTable index userinfo_id_index on " + tableName + "(id);";
    }

    @Override
    public Object getDataItem(Cursor cursor) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Long.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
        userInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
        userInfo.setAge(cursor.getString(cursor.getColumnIndex("age")));
        userInfo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
        userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        return userInfo;
    }
}

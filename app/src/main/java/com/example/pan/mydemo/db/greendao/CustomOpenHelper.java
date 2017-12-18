package com.example.pan.mydemo.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.qiufeng.greendao.greendao.gen.DaoMaster;
import com.qiufeng.greendao.greendao.gen.UserInfoDao;

import org.greenrobot.greendao.database.Database;

/**
 * 自定义OpenHelper
 * Created by PAN on 2017/12/18.
 */
public class CustomOpenHelper extends DaoMaster.DevOpenHelper {

    public CustomOpenHelper(Context context, String name) {
        super(context, name);
    }

    public CustomOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, UserInfoDao.class);
    }
}

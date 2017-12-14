package com.example.pan.mydemo.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pan.mydemo.application.MyApplication;
import com.qiufeng.greendao.greendao.gen.DaoMaster;
import com.qiufeng.greendao.greendao.gen.DaoSession;

/**
 * Created by PAN on 2017/12/11.
 */

public class GreenHelper {

    private static GreenHelper greenHelper;
    private DaoMaster.DevOpenHelper helper;

    private SQLiteDatabase db;

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    private GreenHelper(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "database_green", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static GreenHelper getInstance() {
        if (greenHelper == null) {
            synchronized (GreenHelper.class) {
                if (greenHelper == null) {
                    greenHelper = new GreenHelper(MyApplication.app);
                }
            }
        }
        return greenHelper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

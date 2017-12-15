package com.example.pan.mydemo.db.greendao;

import android.content.Context;

import com.example.pan.mydemo.application.MyApplication;
import com.qiufeng.greendao.greendao.gen.DaoMaster;
import com.qiufeng.greendao.greendao.gen.DaoSession;

/**
 * Created by PAN on 2017/12/11.
 */

public class GreenHelper {

    private static GreenHelper greenHelper;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private final String PASSWORD = "panyufen";

    private GreenHelper(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "database_green", null);
//        daoMaster = new DaoMaster(helper.getEncryptedWritableDb(PASSWORD));
        daoMaster = new DaoMaster(helper.getWritableDb());
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

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

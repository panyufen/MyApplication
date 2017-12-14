package com.example.pan.mydemo.db.natives;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.application.MyApplication;
import com.example.pan.mydemo.db.DBInterface;

/**
 * Created by PAN on 2017/12/11.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper dataBaseHelper;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database_native";
    private String tableName;
    private SQLiteDatabase db = null;
    private DBInterface dbInterface;


    private DataBaseHelper(Context context,DBInterface dbInterface) {
        super(context, DB_NAME, null, DB_VERSION);
        this.dbInterface = dbInterface;
    }

    public static DataBaseHelper getInstance(DBInterface dbInterface) {
        if (dataBaseHelper == null) {
            synchronized (DataBaseHelper.class) {
                if (dataBaseHelper == null) {
                    dataBaseHelper = new DataBaseHelper(MyApplication.app,dbInterface);
                }
            }
        }
        return dataBaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        dbInterface.create(null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        while (oldVersion < newVersion) {
            LogUtils.i("onUpgrade " + oldVersion + " " + newVersion);
            switch (oldVersion++) {

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

            }
        }
    }

    public void setDbDao(DBInterface dbDao) {
        dbInterface = dbDao;
    }


}

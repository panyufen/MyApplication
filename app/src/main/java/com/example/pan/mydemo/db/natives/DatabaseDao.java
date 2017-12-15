package com.example.pan.mydemo.db.natives;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cus.pan.library.utils.FieldUtil;
import com.example.pan.mydemo.db.DBInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAN on 2017/12/13.
 */
public abstract class DatabaseDao implements DBInterface {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    protected String table;

    private DatabaseInterface DatabaseInterface = new DataBaseInterImpl();

    public DatabaseDao() {
        table = getTableName();
        this.dataBaseHelper = DataBaseHelper.getInstance(DatabaseInterface);
        db = dataBaseHelper.getWritableDatabase();
    }

    protected abstract String getTableName();

    public abstract void createTable(SQLiteDatabase db);

    public abstract Object getDataItem(Cursor cursor);

    @Override
    public void insert(Object obj) {
        String table = getTableName(obj);
        ContentValues contentValues = pariseValues(obj);
        if (contentValues.size() > 0) {
            db.insert(table, null, contentValues);
        }

    }

    @Override
    public void update(Object obj, String[] whereColums, String[] values) {
        String table = getTableName(obj);
        StringBuilder whereStr = new StringBuilder();
        for (int i = 0; i < whereColums.length; i++) {
            whereStr.append(whereColums[i]);
            whereStr.append("=? ");
            if (i < whereColums.length - 1) {
                whereStr.append("and ");
            }
        }
        db.update(table, pariseValues(obj), whereStr.toString(), values);
    }

    @Override
    public <T> List<T> query(T obj, String id) {
        return null;
    }

    public <T> List<T> queryAll() {
        String sql = "select * from " + table + " where 1=1";
        if (db.isOpen()) {
            Cursor cur = db.rawQuery(sql, null);

            List<T> list = new ArrayList<>();
            try {
                while (cur.moveToNext()) {
                    T t = (T) getDataItem(cur);
                    list.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cur.close();
            return list;
        }
        return null;
    }

    @Override
    public void delete(Object obj, String id) {

    }

    protected String getTableName(Object obj) {
        return obj.getClass().getSimpleName().toLowerCase();
    }

    private String[] getTableColums(Object obj) {
        List<Field> fieldList = FieldUtil.getFields(obj.getClass());
        List<String> fieldNameList = new ArrayList<>();
        try {
            for (Field field : fieldList) {
                if (Modifier.isPrivate(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {//如果是private static transient 则忽略
                    continue;
                }
                fieldNameList.add(field.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] fields = new String[fieldNameList.size()];
        return fieldNameList.toArray(fields);
    }

    private ContentValues pariseValues(Object object) {
        ContentValues contentValues = new ContentValues();
        List<Field> fieldList = FieldUtil.getFields(object.getClass());
        try {
            for (Field f : fieldList) {
                if (Modifier.isTransient(f.getModifiers())
                        || Modifier.isStatic(f.getModifiers())) {//如果是static transient 则忽略
                    continue;
                }
                String fieldName = f.getName();
                Object v = f.get(object);
                if (v != null) {
                    contentValues.put(fieldName, FieldUtil.toString(v));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    public class DataBaseInterImpl implements DatabaseInterface {
        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    interface DatabaseInterface {
        void onCreate(SQLiteDatabase db);

        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }
}

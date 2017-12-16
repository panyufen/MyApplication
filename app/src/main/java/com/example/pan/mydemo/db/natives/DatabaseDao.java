package com.example.pan.mydemo.db.natives;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cus.pan.library.utils.FieldUtil;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.db.DBInterface;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
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

    public abstract void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion);

    public abstract Object getDataItem(Cursor cursor);

    public abstract String[] getColums(Object obj);

    public abstract String[] getValus(Object obj);

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
        LogUtils.i("where " + whereStr.toString() + " " + new Gson().toJson(values));
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
    public void delete(Object obj, String[] whereColums, String[] values) {
        String table = getTableName(obj);
        StringBuilder whereStr = new StringBuilder();
        for (int i = 0; i < whereColums.length; i++) {
            whereStr.append(whereColums[i]);
            whereStr.append("=? ");
            if (i < whereColums.length - 1) {
                whereStr.append("and ");
            }
        }
        db.delete(table, whereStr.toString(), values);
    }

    private String getTableName(Object obj) {
        return obj.getClass().getSimpleName().toLowerCase();
    }

    String[] getTableColums(Object obj) {
        List<Field> fieldList = FieldUtil.getFields(obj.getClass());
        List<String> fieldNameList = new ArrayList<>();
        try {
            for (Field field : fieldList) {
                if (Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {
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

    String[] getTableValues(Object obj) {
        List<Field> fieldList = FieldUtil.getFields(obj.getClass());
        List<String> valueList = new ArrayList<>();
        try {
            for (Field f : fieldList) {
                if (Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {//如果是static transient 则忽略
                    continue;
                }
                Annotation[] annotations = f.getAnnotations();
                if (annotations != null) {
                    continue;
                }
                Object v = f.get(obj);
                if (v != null) {
                    valueList.add(FieldUtil.toString(v));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] values = new String[valueList.size()];
        return valueList.toArray(values);
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
                Annotation[] annotations = f.getAnnotations();
                if (annotations != null) {
                    continue;
                }
                String fieldName = f.getName();
                Object v = f.get(object);
                LogUtils.i("value " + v.toString());

                String value = "";
                if (v != null) {
                    value = FieldUtil.toString(v);
                }
                contentValues.put(fieldName, value);
                LogUtils.i("values " + value);
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
            upgradeTable(db, oldVersion, newVersion);
        }
    }


    interface DatabaseInterface {
        void onCreate(SQLiteDatabase db);

        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }
}

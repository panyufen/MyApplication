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

    public DatabaseDao() {
        this.dataBaseHelper = DataBaseHelper.getInstance(this);
        db = dataBaseHelper.getWritableDatabase();
    }

    @Override
    public void create(String table) {
        db.execSQL(getCreateTableSql(table));
    }

    @Override
    public void insert(Object obj) {
        String table = getTableName(obj);
        db.insert(table, null, pariseValues(obj));
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
        String table = getTableName(obj);
        String sql = "select * from " + table + " where 1=1";
        if (db.isOpen()) {
            Cursor cur = db.rawQuery(sql, null);

            List<T> list = new ArrayList<>();
            try {
                while (cur.moveToNext()) {
                    T t = (T) getObject(cur);
                    list.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cur.close();
        }

        return null;
    }

    abstract Object getObject(Cursor cursor);

    @Override
    public void delete(Object obj, String id) {

    }

    public String getCreateTableSql(String tableName) {
        return "create table if not exists " + tableName + "(id INTEGER NOT NULL,userid INTEGER,name TEXT,age INTEGER,mbile TEXT,"
                + ");create index userinfo_id_index on " + tableName + "(id);";
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
                String fieldName = f.getName();
                Object v = f.get(object);
                if( v != null ) {
                    contentValues.put(fieldName, FieldUtil.toString(v));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }
}

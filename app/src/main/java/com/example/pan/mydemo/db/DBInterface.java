package com.example.pan.mydemo.db;

import java.util.List;

/**
 * Created by PAN on 2017/12/11.
 */
public interface DBInterface {

    void create(String table);

    void insert(Object obj);

    void update(Object obj, String[] whereColums, String[] values);

    <T> List<T> query(T obj, String id);

    void delete(Object obj, String id);

}

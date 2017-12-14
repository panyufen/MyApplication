package com.cus.pan.library.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by roy on 2016/1/4.
 */
public class FieldUtil {
    private static final Set<Class<?>> BASE_CLASS_SET = new HashSet<Class<?>>();

    static {
        BASE_CLASS_SET.add(String.class);
        BASE_CLASS_SET.add(CharSequence.class);
        BASE_CLASS_SET.add(Byte.class);
        BASE_CLASS_SET.add(Short.class);
        BASE_CLASS_SET.add(Integer.class);
        BASE_CLASS_SET.add(Float.class);
        BASE_CLASS_SET.add(Long.class);
        BASE_CLASS_SET.add(Double.class);
    }

    //查询是否是 基础类型
    public static final boolean isBaseType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isPrimitive() || BASE_CLASS_SET.contains(clazz);
    }

    //获取参数列表
    public static final List<Field> getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        while (clazz != null && clazz.equals(Object.class) == false) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                list.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    //反射值
    public static final String toString(Object val) {
        if (val instanceof String) {
            return (String) val;
        } else if (isBaseType(val.getClass())) {
            return String.valueOf(val);
        }
        return val.toString();
    }

    //反射值
    public static final Object parser(String s, Field field) {
        return parser(s, field.getType());
    }

    //反射值
    public static final Object parser(String s, Class<?> clazz) {
        if (isBaseType(clazz)) {
            // boolean, byte, char, short, int, long, float, and double.
            if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
                return Boolean.parseBoolean(s);
            } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                return Integer.parseInt(s);
            } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                return Long.parseLong(s);
            } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                return Float.parseFloat(s);
            } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                return Double.parseDouble(s);
            } else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                return Byte.parseByte(s);
            } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                return Short.parseShort(s);
            }
        }
        return s;
    }
}

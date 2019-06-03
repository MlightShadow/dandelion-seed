package com.company.project.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DataCopyUtil {
    public static void copyEntity(Object father, Object child) {
        if (!(child.getClass().getSuperclass() == father.getClass())) {
            System.err.println("child不是father的子类");
        }
        Class<?> fatherClass = father.getClass();
        Class<?> childClass = child.getClass();
        Field ff[] = fatherClass.getDeclaredFields();
        for (int i = 0; i < ff.length; i++) {
            Field f = ff[i];// 取出每一个属性，如deleteDate
            Class<?> type = f.getType();
            try {
                Method get = fatherClass.getMethod("get" + upperHeadChar(f.getName()));// 方法getDeleteDate
                Object obj = get.invoke(father);// 取出属性值

                if (obj == null) {
                    continue;
                }

                Method set = childClass.getMethod("set" + upperHeadChar(f.getName()), type);
                set.invoke(child, obj);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }
}
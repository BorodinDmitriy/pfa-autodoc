package com.autodoc.pfa.pfaautodoc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ObjectInspector {
    public static <T> List<String> inspect(Class<T> klazz) {
        List<String> result = new ArrayList<>();
        Field[] fields = klazz.getDeclaredFields();
        for (Field field : fields) {
            result.add(field.getName());
        }
        return result;
    }

    public static String getValueByField(Object o, String fieldName) {
        Class<?> c = o.getClass();

        try {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);

            String result = (String) f.get(o);
            return result;
        } catch (NoSuchFieldException ex) {
            try {
                Class<?> d = o.getClass().getSuperclass();
                Field m = d.getDeclaredField(fieldName);
                m.setAccessible(true);

                String result = (String) m.get(o);
                return result;
            } catch (NoSuchFieldException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }


        } catch (IllegalAccessException e) {
            return null;
        }

    }
}

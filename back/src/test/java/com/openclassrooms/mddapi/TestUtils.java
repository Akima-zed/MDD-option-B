package com.openclassrooms.mddapi;

import java.lang.reflect.Field;
import com.openclassrooms.mddapi.TestUtils;



public class TestUtils {

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
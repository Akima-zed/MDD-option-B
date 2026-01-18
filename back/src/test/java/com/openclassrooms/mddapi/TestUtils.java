package com.openclassrooms.mddapi;

import java.lang.reflect.Field;
import com.openclassrooms.mddapi.TestUtils;

/**
 * Classe utilitaire utilisée uniquement dans les tests.
 *
 * Elle permet de changer la valeur d'un champ privé d'un objet,
 * par exemple quand Spring ne peut pas l'initialiser dans un test unitaire.
 *
 * Cela aide à tester une classe toute seule, sans charger Spring.
 */
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
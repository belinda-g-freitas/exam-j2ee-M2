/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class ReflectUtils {
    
    /**
     * Retourne une méthode déclarée dans la classe et ses sous-classes et interfaces
     * @param classe
     * @param exp
     * @return la méthode correspondante, sans arguments
     */
    public static Method getMethod(Class classe, String exp) {
        return getMethod(classe, exp, false);
    }
    
    /**
     * Retourne une méthode déclarée dans la classe et ses sous-classes et interfaces
     * @param classe la classe concernée par les opérations
     * @param exp l'expression à traiter
     * @param withArgs si la méthode comporte des arguments ou pas
     * @return la méthode correspondante
     */
    public static Method getMethod(Class classe, String exp, boolean withArgs) {
        String[] names = StringUtils.split(exp, ".");
        Method result = null;
        try {
            if (names.length <= 1) {
                // On récupère la méthode
                result = getDeclaredMethod(classe, names[0].replace("()", ""), withArgs);
            } else {
                // On itère sur les champs
                Field field = getField(classe, String.join(".", Arrays.copyOfRange(names, 0, names.length - 1)));
                // On récupère la méthode
                result = getDeclaredMethod(field.getDeclaringClass(), names[names.length - 1].replace("()", ""), withArgs);
            }
        } catch (SecurityException | NoSuchMethodException ex) {
            Logger.getLogger(ReflectUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static Field getField(Class classe, String exp) {
        String[] fieldsName = StringUtils.split(exp, ".");
        Field result = null;
        int i = 0, end = (fieldsName.length - 1);
        try {
            result = getDeclaredField(classe, fieldsName[i++]);
            while (i <= end) {
                result = getDeclaredField(result.getType(), fieldsName[i++]);
            }
        } catch (SecurityException ex) {
            Logger.getLogger(ReflectUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static Field getDeclaredField(Class classe, String fieldName)  {
        try {
            return FieldUtils
                    .getAllFieldsList(classe)
                    .stream()
                    .filter((t) -> {
                        return t.getName().equals(fieldName);
                    })
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static Method getDeclaredMethod(Class classe, String methodName, boolean withArgs) throws NoSuchMethodException {
        try {
            return getAllMethodsList(classe)
                    .stream()
                    .filter((t) -> {
                        return t.getName().equals(methodName) && 
                                (withArgs ? t.getParameters().length != 0 : t.getParameters().length == 0);
                    })
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new NoSuchMethodException();
        }
    }

    public static List<Method> getAllMethodsList(Class classe) throws NoSuchMethodException {
        return Arrays.asList(getAllMethods(classe));
    }

    public static Method[] getAllMethods(Class classe) throws NoSuchMethodException {
        /* if (classe.getSuperclass() != null) {
            return (Method[]) ArrayUtils.addAll(getAllMethods(classe.getSuperclass()), classe.getDeclaredMethods());
        } else {
            return classe.getDeclaredMethods();
        } */
        Method[] result = new Method[]{};
        // DeclaredMethod 
        result = ArrayUtils.addAll(classe.getDeclaredMethods(), result);
        // SuperClass extended
        if (classe.getSuperclass() != null) {
            result = (Method[]) ArrayUtils.addAll(getAllMethods(classe.getSuperclass()), result);
        }
        // Interfaces implemented
        if (classe.getInterfaces().length != 0) {
            for (Class c : classe.getInterfaces()) {
                result = (Method[]) ArrayUtils.addAll(getAllMethods(c), result);
            }
        }
        return result;
    }

    public static Method getDeclaredMethod(Class classe, String methodName, Class... parametersTypes) throws NoSuchMethodException {
        try {
            return getAllMethodsList(classe, methodName, parametersTypes)
                    .stream()
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new NoSuchMethodException();
        }
    }

    public static List<Method> getAllMethodsList(Class classe, String methodName, Class... parametersTypes) {
        return Arrays.asList(getAllMethods(classe, methodName, parametersTypes));
    }

    public static Method[] getAllMethods(Class classe, String methodName, Class... parametersTypes) {
        Method[] result = new Method[]{};
        try {
            // DeclaredMethod
            result = ArrayUtils.addAll(new Method[]{classe.getDeclaredMethod(methodName, parametersTypes)}, result);
        } catch (NoSuchMethodException | SecurityException ex) {}
        // SuperClass extended
        if (classe.getSuperclass() != null) {
            result = (Method[]) ArrayUtils.addAll(getAllMethods(classe.getSuperclass(), methodName, parametersTypes), result);
        }
        // Interfaces implemented
        if (classe.getInterfaces().length != 0) {
            for (Class c : classe.getInterfaces()) {
                result = (Method[]) ArrayUtils.addAll(getAllMethods(c, methodName, parametersTypes), result);
            }
        }
        return result;
    }

    public static Class[] getClasses(Object... objects) {
        Class[] result = new Class[objects.length];
        for (int i = 0, end = objects.length - 1; i <= end; i++) {
            result[i] = objects[i].getClass();
        }
        return result;
    }
}

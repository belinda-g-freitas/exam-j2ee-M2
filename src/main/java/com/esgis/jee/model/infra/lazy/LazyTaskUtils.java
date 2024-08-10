/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.lazy;

import com.esgis.jee.model.infra.reflect.ReflectUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;



/**
 * <pre>Cette classe est un utilitaire pour les tâches de Lazy loading</pre>
 *
 * @author gyldas_atta_kouassi
 */
public class LazyTaskUtils {
	
	private static final Logger LOGGER = Logger.getLogger(LazyTaskUtils.class.getName());
	
	public static Object getFieldValueByExp(Object object, String fieldName) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//return getAccessibleFieldByExp(object, fieldName).get(object);
		return BeanUtils.getProperty(object, fieldName);
	}
	
	public static Field getAccessibleFieldByExp(Object object, String fieldExp) throws NullPointerException {
		return getAccessibleFieldByExp(object.getClass(), fieldExp);
	}
	
	/**
	 * Retourne le champ de l'expression EL sur la classe
	 * NB : les methodes sont proscriptes de l'expression EL
	 * @param classe
	 * @param fieldExpName : l'expression qui referencie un champ
	 * @return
	 * @throws NullPointerException
	 */
	public static Field getAccessibleFieldByExp(Class<?> classe, String fieldExp) throws NullPointerException {
		String[] names = StringUtils.split(fieldExp, ".");
        Class<?> aClass = classe;
        Field result = null;
        int i = 0, end = (names.length - 1);
        try {
            do {
                result = getAccessibleField(aClass, names[i++]);
                aClass = result.getType();
            } while (i <= end);
        } catch (IllegalArgumentException | NullPointerException | NoSuchFieldException ex) {
        	LOGGER.log(Level.SEVERE, null, ex);
        }
        return result;
	}
	
	public static Method getAccessibleMethod(Class<?> classe, String methodName, Class<?>... parametersTypes) throws NullPointerException, NoSuchMethodException {
		Method result = ReflectUtils.getDeclaredMethod(classe, methodName, parametersTypes);
		result.setAccessible(true);
		return result;
	}
	
	public static Field getAccessibleField(Class<?> classe, String fieldName) throws NullPointerException, NoSuchFieldException {
		Field result = ReflectUtils.getDeclaredField(classe, fieldName);
		result.setAccessible(true);
		return result;
	}
	
	public static Field getAccessibleField(Object object, String fieldName) throws NullPointerException, NoSuchFieldException {
		return getAccessibleField(object.getClass(), fieldName);
	}
	
	public static Object getFieldValue(Object object, String fieldName) throws NullPointerException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		return getAccessibleField(object, fieldName).get(object);
	}
	
    public static boolean isMethod(String exp) {
        return exp.endsWith("()");
    }
    
    /**
     * Retourne la valeur de l'expression EL sur l'objet
     * @param object l'objet concerne
     * @param exp l'expression (qui peut referencer un champ ou une methode)
     * @return
     */
    public static Object getValue(Object object, String exp) {
        String[] names = StringUtils.split(exp, ".");
        Object result = object;
        try {
            // On récupère la valeur de la 1ère expression par méthode si c'est une méthode, par champ si c'est un champ
            result = isMethod(names[0]) ? getValueByMethodExp(object, names[0]) : getValueByFieldExp(object, names[0]);
            // Si l'expression est composée on réitère
            if (names.length > 1) {
                result = getValue(result, String.join(".", Arrays.copyOfRange(names, 1, names.length)));
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {}
        return result;
    }
    
    /**
     * Retourne la valeur de l'expression EL (etant une methode) sur l'objet
     * @param object l'objet concerne
     * @param exp l'expression qui reference forcement une methode
     * @return
     */
    public static Object getValueByMethodExp(Object object, String exp) {
        String[] names = StringUtils.split(exp, ".");
        Object result = object;
        try {
            if (names.length <= 1) {
                // On récupère la valeur par la méthode
                result = (ReflectUtils.getMethod(result.getClass(), names[0])).invoke(object);
            } else {
                // On itère sur les champs
                result = getValueByFieldExp(result, String.join(".", Arrays.copyOfRange(names, 0, names.length - 1)));
                // On récupère la valeur par la méthode
                result = (ReflectUtils.getMethod(result.getClass(), names[names.length - 1])).invoke(result);
            }
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException | NullPointerException ex) {}
        return result;
    }
    
    /**
     * Retourne la valeur de l'expression EL (etant un champ) sur l'objet
     * @param object l'objet concerne
     * @param exp l'expression qui reference forcement un champ
     * @return
     */
    public static Object getValueByFieldExp(Object object, String exp) {
        String[] names = StringUtils.split(exp, ".");
        Object result = object;
        Field field;
        int i = 0, end = (names.length - 1);
        try {
            while (i <= end) {
                field = ReflectUtils.getField(result.getClass(), names[i++]);
                field.setAccessible(true);
                result = field.get(result);
                field.setAccessible(false);
            }
        } catch (IllegalArgumentException | IllegalAccessException | NullPointerException ex) {}
        return result;
    }
}

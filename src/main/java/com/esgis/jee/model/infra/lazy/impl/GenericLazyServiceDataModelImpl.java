package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyServiceDataModel;
import com.esgis.jee.model.infra.reflect.ReflectUtils;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;


public class GenericLazyServiceDataModelImpl<T extends Serializable, K> extends GenericLazyDataModelImpl<T, K> implements GenericLazyServiceDataModel<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Method methodToInvoke;
	protected final Object[] methodParameters;
	protected final Service<T, K> service;
    
	public GenericLazyServiceDataModelImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String methodToInvokeName, Class<?>[] methodParametersTypes, Object... methodParameters) {
        this.service = service;
        this.dataClass = dataClass;
		this.idClass = idClass;
        try {
            this.methodToInvoke = ReflectUtils.getDeclaredMethod(serviceClass, methodToInvokeName, methodParametersTypes);
        } catch (NoSuchMethodException | SecurityException ex) {
        	LOGGER.log(Level.SEVERE, null, ex);
        }
        this.methodParameters = methodParameters;
    }
    
	public GenericLazyServiceDataModelImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String methodToInvokeName, Object... methodParameters) {
        this(service, serviceClass, dataClass, idClass, methodToInvokeName, ReflectUtils.getClasses(methodParameters), methodParameters);
    }
	
	public GenericLazyServiceDataModelImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String dataTbName, String methodToInvokeName, Class<?>[] methodParametersTypes, Object... methodParameters) {
        this(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
        this.dataTbName = dataTbName;
    }
    
	public GenericLazyServiceDataModelImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String dataTbName, String methodToInvokeName, Object... methodParameters) {
        this(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParameters);
        this.dataTbName = dataTbName;
    }
	
	public GenericLazyServiceDataModelImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields, String methodToInvokeName, Object... methodParameters) {
        this(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParameters);
        this.globalFilterFields = globalFilterFields;
    }
	
	@SuppressWarnings("unchecked")
	protected List<T> invokeMethod() {
		try {
			return ((List<T>) methodToInvoke.invoke(service, methodParameters));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, null, e);
			return new ArrayList<>();
		}
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return new GenericLazyCollectionDataModelImpl<>(invokeMethod(), getGlobalFilterFields())
				.count(filterBy);
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		// Sauvegarde des parametres de Filtre et de Tri
		defineSortAndFilter(sortBy, filterBy);
		// Load
		// La necessite d'affectation du data fait partie des Effets de bord de la programmation imperative
		data = new GenericLazyCollectionDataModelImpl<>(invokeMethod(), getGlobalFilterFields())
				.load(first, pageSize, sortBy, filterBy);
		return (List<T>) data;
	}
	
	//TODO Manage Criteria Queries or JPQL Queries
	
}

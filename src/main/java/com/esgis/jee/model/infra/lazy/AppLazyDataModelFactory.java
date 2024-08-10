/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.lazy;

import com.esgis.jee.model.infra.lazy.impl.GenericLazyCollectionDataModelImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyCollectionDataModelWithTempDataImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyDBDataModelImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyDBDataModelWithTempDataImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyServiceDataModelImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyServiceDataModelWithTempDataImpl;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.util.Collection;


/**
 *
 * @author gyldas_atta_kouassi
 */
public class AppLazyDataModelFactory {
    
	private AppLazyDataModelFactory() {}
	
	/*
	 * Specific Data Models
	 */
	
	/*
	 * 
	 * Generic Data Models
	 * 
	 */
	public static <K, T extends Serializable> GenericLazyCollectionDataModel<T, K>  createLazyCollectionDataModel(
			Collection<T> dataSource, 
			Class<T> dataClass, 
			Class<K> idClass) {
		return new GenericLazyCollectionDataModelImpl<>(dataSource, dataClass, idClass);
	}
	
	public static <K, T extends Serializable> GenericLazyCollectionDataModel<T, K>  createLazyCollectionDataModel(
			Collection<T> dataSource, 
			Class<T> dataClass, 
			Class<K> idClass,
			String[] globalFilterFields) {
		return new GenericLazyCollectionDataModelImpl<>(dataSource, dataClass, idClass, globalFilterFields);
	}
	
	public static <K, T extends Serializable> GenericLazyCollectionDataModel<T, K>  createLazyCollectionDataModel(
			Collection<T> dataSource, 
			Class<T> dataClass, 
			Class<K> idClass,
			String dataTbName) {
		return new GenericLazyCollectionDataModelImpl<>(dataSource, dataClass, idClass, dataTbName);
	}
	
	public static <K, T extends Serializable> GenericLazyCollectionDataModelWithTempData<T, K> createLazyCollectionDataModelWithTempData(
			Collection<T> dataSource, 
			Service<T, K> repository,
			Class<T> dataClass, 
			Class<K> idClass, 
			String dataTbName) {
		return new GenericLazyCollectionDataModelWithTempDataImpl<>(
                        dataSource, 
        		repository, 
        		dataClass,
        		idClass, 
        		dataTbName);
	}
	
	public static <K, T extends Serializable> GenericLazyDataModel<T, K> createLazyCollectionDataModelWithTempData(
			Collection<T> dataSource,
			Service<T, K> repository,
			Class<T> dataClass, 
			Class<K> idClass,
			String[] globalFilterFields) {
		return new GenericLazyCollectionDataModelWithTempDataImpl<>(dataSource, repository , dataClass, idClass, globalFilterFields);
	}
	
	public static <K, T extends Serializable> GenericLazyDBDataModelWithTempData<T, K> createLazyDBDataModelWithTempData(
			Service<T, K> repository, 
			Class<T> dataClass, 
			Class<K> idClass, 
			String dataTbName) {
		return new GenericLazyDBDataModelWithTempDataImpl<>(
        		repository, 
        		dataClass,
        		idClass,
        		dataTbName);
	}
	
	public static <K, T extends Serializable> GenericLazyDBDataModelWithTempData<T, K> createLazyDBDataModelWithTempData(
			Service<T, K> repository, 
			Class<T> dataClass, 
			Class<K> idClass, 
			String[] globalFilterFields) {
		return new GenericLazyDBDataModelWithTempDataImpl<>(
        		repository, 
        		dataClass,
        		idClass,
        		globalFilterFields);
	}
	
	public static <K, T extends Serializable> GenericLazyDBDataModel<T, K> createLazyDBDataModel(
			Service<T, K> repository, 
			Class<T> dataClass, 
			Class<K> idClass, 
			String[] globalFilterFields) {
		return new GenericLazyDBDataModelImpl<>(
        		repository, 
        		dataClass,
        		idClass,
        		globalFilterFields);
	}
	
	public static <K, T extends Serializable> GenericLazyDBDataModel<T, K> createLazyDBDataModel(
			Service<T, K> repository, 
			Class<T> dataClass, 
			Class<K> idClass, 
			String dataTbName) {
		return new GenericLazyDBDataModelImpl<>(
        		repository, 
        		dataClass,
        		idClass,
        		dataTbName);
	}
	
	public static <K, T extends Serializable> GenericLazyServiceDataModel<T, K> createLazyServiceDataModel(
			Service<T, K> service, 
			Class<?> serviceClass, 
			Class<T> dataClass, 
			Class<K> idClass,
			String dataTbName, 
			String methodToInvokeName,
			Object... methodParameters){
		return new GenericLazyServiceDataModelImpl<>(service, serviceClass, dataClass, idClass, dataTbName, methodToInvokeName, methodParameters);
	}
	
	public static <K, T extends Serializable> GenericLazyServiceDataModel<T, K> createLazyServiceDataModel(
			Service<T, K> service, 
			Class<?> serviceClass, 
			Class<T> dataClass, 
			Class<K> idClass,
			String[] globalFilterFields, 
			String methodToInvokeName,
			Object... methodParameters){
		return new GenericLazyServiceDataModelImpl<>(service, serviceClass, dataClass, idClass, globalFilterFields, methodToInvokeName, methodParameters);
	}

	public static <K, T extends Serializable> GenericLazyServiceDataModel<T, K> createLazyServiceDataModelWithTempData(
			Service<T, K> service, 
			Class<?> serviceClass, 
			Class<T> dataClass, 
			Class<K> idClass,
			String[] globalFilterFields, 
			String methodToInvokeName,
			Object... methodParameters) {
		return new GenericLazyServiceDataModelWithTempDataImpl<>(service, serviceClass, dataClass, idClass, globalFilterFields, methodToInvokeName, methodParameters);
	}
	
	public static <K, T extends Serializable> GenericLazyServiceDataModel<T, K> createLazyServiceDataModelWithTempData(
			Service<T, K> service, 
			Class<?> serviceClass, 
			Class<T> dataClass, 
			Class<K> idClass,
			String dataTbName,
			String methodToInvokeName,
			Object... methodParameters) {
		return new GenericLazyServiceDataModelWithTempDataImpl<>(service, serviceClass, dataClass, idClass, dataTbName, methodToInvokeName, methodParameters);
	}
}

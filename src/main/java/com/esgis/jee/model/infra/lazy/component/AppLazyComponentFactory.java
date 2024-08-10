package com.esgis.jee.model.infra.lazy.component;

import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyCollectionDataModelImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyDBDataModelImpl;
import com.esgis.jee.model.infra.lazy.impl.GenericLazyServiceDataModelImpl;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.util.Collection;


public class AppLazyComponentFactory {
	
	private AppLazyComponentFactory() {}
	
	/*
	 * 
	 * SelectOneMenu Lazy Component
	 * 
	 */
	// Collection
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyCollectionSelectOneMenuComponentManager (
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields,
			Collection<T> dataCollection) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyCollectionDataModelImpl<>(dataCollection, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyCollectionSelectOneMenuComponentManager (
			T noSelectionObject,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields,
			Collection<T> dataCollection) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyCollectionDataModelImpl<>(dataCollection, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyCollectionSelectOneMenuComponentManager (
			T noSelectionObject,
			String dialogTitle,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields,
			Collection<T> dataCollection) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyCollectionDataModelImpl<>(dataCollection, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, dialogTitle);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyCollectionSelectOneMenuComponentManager (
			T noSelectionObject,
			String destFieldName,
			String dialogTitle,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields,
			Collection<T> dataCollection) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyCollectionDataModelImpl<>(dataCollection, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyCollectionSelectOneMenuComponentManager (
			T noSelectionObject,
			Object destObject,
			String destFieldName,
			String dialogTitle,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields,
			Collection<T> dataCollection) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyCollectionDataModelImpl<>(dataCollection, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destObject, destFieldName, dialogTitle);
    }
	
	// DB
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			T noSelectionObject,
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			T noSelectionObject,
			String dialogTitle,
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, dialogTitle);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			T noSelectionObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			T noSelectionObject,
			Object destObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String[] filtrableFields) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass, filtrableFields);
		//dataModel.setGlobalFilterFields();
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K>  LazySelectOneMenuComponentManager<T, K> createLazyDBSelectOneMenuComponentManager (
			T noSelectionObject,
			Object destObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<T> modelClass,
			Class<K> idClass,
			String dataTbName) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyDBDataModelImpl<>(service, modelClass, idClass);
		dataModel.setDataTbName(dataTbName);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destObject, destFieldName, dialogTitle);
    }
	
	// Service
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName,
			Class<?>[] methodParametersTypes, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName,
			Class<?>[] methodParametersTypes, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass, 
			Class<T> modelClass, 
			Class<K> idClass, 
			String[] filtrableFields,
			String methodToInvokeName,
			Class<?>[] methodParametersTypes, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, dialogTitle);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass, 
			Class<T> modelClass, 
			Class<K> idClass, 
			String[] filtrableFields,
			String methodToInvokeName,
			Class<?>[] methodParametersTypes, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			Object destObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass, 
			Class<T> modelClass, 
			Class<K> idClass, 
			String[] filtrableFields,
			String methodToInvokeName,
			Class<?>[] methodParametersTypes, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			Service<T, K> service,
			Class<?> serviceClass, 
			Class<T> dataClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName,
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName,
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, dialogTitle);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destFieldName, dialogTitle);
    }
	
	public static <T extends Serializable, K> LazySelectOneMenuComponentManager<T, K> createLazyServiceSelectOneMenuComponentManager(
			T noSelectionObject,
			Object destObject,
			String destFieldName,
			String dialogTitle,
			Service<T, K> service,
			Class<?> serviceClass,
			Class<T> modelClass, 
			Class<K> idClass,
			String[] filtrableFields,
			String methodToInvokeName, 
			Object... methodParameters) {
		GenericLazyDataModel<T, K> dataModel = new GenericLazyServiceDataModelImpl<>(service, serviceClass, modelClass, idClass, methodToInvokeName, methodParameters);
		dataModel.setGlobalFilterFields(filtrableFields);
        return new LazySelectOneMenuComponentManager<>(dataModel, noSelectionObject, destObject, destFieldName, dialogTitle);
    }
}
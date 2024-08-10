package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModelWithTempData;
import com.esgis.jee.model.infra.lazy.GenericLazyServiceDataModelWithTempData;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;


public class GenericLazyServiceDataModelWithTempDataImpl<T extends Serializable, K> extends GenericLazyServiceDataModelImpl<T, K> implements GenericLazyServiceDataModelWithTempData<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected GenericLazyCollectionDataModelWithTempData<T, K> dataModel;
	protected final Collection<T> tempCreatedDataCollection = new ArrayList<>(); // Pour les cas de nouvelles donnees elles peuvent avoir le meme id == null -> ArrayList pour les creations multiples
	protected final Collection<T> tempUpdatedDataCollection = new HashSet<>();
	protected final Collection<T> tempDeletedDataCollection = new HashSet<>();
    
	public GenericLazyServiceDataModelWithTempDataImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String methodToInvokeName, Class<?>[] methodParametersTypes, Object... methodParameters) {
        super(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParametersTypes, methodParameters);
    }
    
	public GenericLazyServiceDataModelWithTempDataImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String methodToInvokeName, Object... methodParameters) {
        super(service, serviceClass, dataClass, idClass, methodToInvokeName, methodParameters);
    }
	
	public GenericLazyServiceDataModelWithTempDataImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String dataTbName, String methodToInvokeName, Class<?>[] methodParametersTypes, Object... methodParameters) {
        super(service, serviceClass, dataClass, idClass, dataTbName, methodToInvokeName, methodParametersTypes, methodParameters);
    }
    
	public GenericLazyServiceDataModelWithTempDataImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String dataTbName, String methodToInvokeName, Object... methodParameters) {
        super(service, serviceClass, dataClass, idClass, dataTbName, methodToInvokeName, methodParameters);
    }
	
	public GenericLazyServiceDataModelWithTempDataImpl(Service<T, K> service, Class<?> serviceClass, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields, String methodToInvokeName, Object... methodParameters) {
		super(service, serviceClass, dataClass, idClass, globalFilterFields, methodToInvokeName, methodParameters);
    }
	
	protected void createDataModel() {
		dataModel = new GenericLazyCollectionDataModelWithTempDataImpl<>(invokeMethod(), service, getDataClass(), getIDClass(), getGlobalFilterFields());
		dataModel.addTempCreatedData(tempCreatedDataCollection);
		dataModel.addTempUpdatedData(tempUpdatedDataCollection);
		dataModel.addTempDeletedData(tempDeletedDataCollection);
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		createDataModel();
		return dataModel.count(filterBy);
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		// Sauvegarde des parametres de Filtre et de Tri
		defineSortAndFilter(sortBy, filterBy);
		createDataModel();
        // Application du load
		// La necessite d'affectation du data fait partie des Effets de bord de la programmation imperative
		data = dataModel.load(first, pageSize, sortBy, filterBy);
		return (List<T>) data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addTempCreatedData(T... data) {
		addTempCreatedData(Arrays.asList(data));
	}

	@Override
	public void addTempCreatedData(Collection<T> dataCollection) {
		this.tempCreatedDataCollection.addAll(dataCollection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addTempUpdatedData(T... data) {
		addTempUpdatedData(Arrays.asList(data));
	}

	@Override
	public void addTempUpdatedData(Collection<T> dataCollection) {
		// this.tempUpdatedDataCollection.addAll(dataCollection);
		// On ajoute que les donnees a mettre a jour dans la Source de donnees
		// Les donnees en creation ne sont pas a MAJ
		for(T data : dataCollection) {
			if(!tempCreatedDataCollection.contains(data)) {
				this.tempUpdatedDataCollection.add(data);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addTempDeletedData(T... data) {
		addTempDeletedData(Arrays.asList(data));
	}

	@Override
	public void addTempDeletedData(Collection<T> dataCollection) {
		this.tempDeletedDataCollection.addAll(dataCollection);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteDataTemp(T... data) {
		deleteDataTemp(Arrays.asList(data));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteDataTemp(Collection<T> dataCollection) {
		for(T t : dataCollection) {
			if(tempCreatedDataCollection.contains(t)) {
				tempCreatedDataCollection.remove(t);
			} else {
				addTempDeletedData(t);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeTempCreatedData(T... data) {
		removeTempCreatedData(Arrays.asList(data));
	}

	@Override
	public void removeTempCreatedData(Collection<T> dataCollection) {
		this.tempCreatedDataCollection.removeAll(dataCollection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeTempUpdatedData(T... data) {
		removeTempUpdatedData(Arrays.asList(data));
	}

	@Override
	public void removeTempUpdatedData(Collection<T> dataCollection) {
		this.tempUpdatedDataCollection.removeAll(dataCollection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeTempDeletedData(T... data) {
		removeTempDeletedData(Arrays.asList(data));
	}

	@Override
	public void removeTempDeletedData(Collection<T> dataCollection) {
		this.tempDeletedDataCollection.removeAll(dataCollection);
	}

	@Override
	public void clearTempCreatedDataCollection() {
		this.tempCreatedDataCollection.clear();
	}

	@Override
	public void clearTempUpdatedDataCollection() {
		this.tempUpdatedDataCollection.clear();
	}

	@Override
	public void clearTempDeletedDataCollection() {
		this.tempDeletedDataCollection.clear();
	}

	@Override
	public void doDBCreate(boolean clearTempData) {
		for(T object : tempCreatedDataCollection) {
            object = (T) service.create(object);
        }
		if(clearTempData) {
			clearTempCreatedDataCollection();
		}
	}

	@Override
	public void doDBUpdate(boolean clearTempData) {
		for(T object : tempUpdatedDataCollection) {
            object = (T) service.update(object);
        }
		if(clearTempData) {
			clearTempUpdatedDataCollection();
		}
	}

	@Override
	public void doDBDelete(boolean clearTempData) {
		for(T object : tempDeletedDataCollection) {
			service.delete(object);
        }
		if(clearTempData) {
			clearTempDeletedDataCollection();
		}
	}
	
	@Override
	public Collection<T> getTempCreatedData() {
		return tempCreatedDataCollection;
	}

	@Override
	public Collection<T> getTempUpdatedData() {
		return tempUpdatedDataCollection;
	}

	@Override
	public Collection<T> getTempDeletedData() {
		return tempDeletedDataCollection;
	}
	
	@Override
	public boolean hasCreatedData() {
		return !tempCreatedDataCollection.isEmpty();
	}

	@Override
	public boolean hasUpdatedData() {
		return !tempUpdatedDataCollection.isEmpty();
	}

	@Override
	public boolean hasDeletedData() {
		return !tempDeletedDataCollection.isEmpty();
	}
	
	@Override
	public int countTempCreatedData() {
		return tempCreatedDataCollection.size();
	}
	
	@Override
	public int countTempUpdatedData() {
		return tempUpdatedDataCollection.size();
	}
	
	@Override
	public int countTempDeletedData() {
		return tempDeletedDataCollection.size();
	}
}

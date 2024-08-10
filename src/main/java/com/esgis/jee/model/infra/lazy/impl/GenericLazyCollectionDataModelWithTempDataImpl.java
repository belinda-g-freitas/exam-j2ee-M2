package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModelWithTempData;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;

public class GenericLazyCollectionDataModelWithTempDataImpl<T extends Serializable, K > extends GenericLazyCollectionDataModelImpl<T, K> implements GenericLazyCollectionDataModelWithTempData<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Collection<T> tempCreatedDataCollection = new ArrayList<>(); // Pour les cas de nouvelles données elles peuvent avoir le même id == null -> ArrayList pour les creations multiples
	protected final Collection<T> tempUpdatedDataCollection = new HashSet<>();
	protected final Collection<T> tempDeletedDataCollection = new HashSet<>();
	protected Service<T, K> repository;
	
	public GenericLazyCollectionDataModelWithTempDataImpl(Collection<T> dataSource, Service<T, K> repository, Class<T> dataClass, Class<K> idClass) {
		super(dataSource, dataClass, idClass);
		this.repository = repository;
	}
	
	public GenericLazyCollectionDataModelWithTempDataImpl(Collection<T> dataSource, Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String dataTbName) {
		this(dataSource, repository, dataClass, idClass);
		this.dataTbName = dataTbName;
	}
	
	public GenericLazyCollectionDataModelWithTempDataImpl(Collection<T> dataSource, Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields) {
		this(dataSource, repository, dataClass, idClass);
		this.globalFilterFields = globalFilterFields;
	}
	
	@Override
	protected boolean globalMatch(String filterStringValue, T object, String fieldName) {
		if(tempDeletedDataCollection.contains(object)) {
			return false;
		} else {
			return super.globalMatch(filterStringValue, object, fieldName);
		}
	}
	
	@Override
	protected boolean match(Object filterValue, Object object, String fieldName, MatchMode matchMode) {
		if(tempDeletedDataCollection.contains(object)) {
			return false;
		} else {
			return super.match(filterValue, object, fieldName, matchMode);
		}
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// Compter les occurrences dans le dataSource qui correspondent
		int result = super.count(filterBy);
		// Ajouter les créés non supprimés qui correspondent
		List<T> createdNotDeleted = new ArrayList<>(tempCreatedDataCollection);
		createdNotDeleted.removeAll(tempDeletedDataCollection);
		result = result +  new GenericLazyCollectionDataModelImpl<>(createdNotDeleted, getGlobalFilterFields()).count(filterBy);
		// Retirer les supprimés en dataSource qui correspondent
		List<T> deletedInDatasource = new ArrayList<>(tempDeletedDataCollection);
		deletedInDatasource.removeAll(tempCreatedDataCollection);
		result = result -  new GenericLazyCollectionDataModelImpl<>(deletedInDatasource, getGlobalFilterFields()).count(filterBy);
		return result;
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		List<T> result = super.load(first, pageSize + tempDeletedDataCollection.size(), sortBy, filterBy);
		// Prise en compte des données temporaires
		result.addAll(tempCreatedDataCollection); // Ajout des nouvelles données
		int index;
        for(T t : tempUpdatedDataCollection) {
            if((index = result.indexOf(t)) != -1) {
                result.set(index, t); // Mise à jour des donnees
            }
        }
        result.removeAll(tempDeletedDataCollection); // Suppression des donnees
        // Application du load
        // La necessite d'affectation du data fait partie des Effets de bord de la programmation imperative
        data = load(result, 0, pageSize, sortBy, filterBy);
        return (List<T>) data;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public T getRowData(String rowKey) {
		K id = getIDClass().equals(Integer.class) ? 
				(K) Integer.valueOf(rowKey) :
				getIDClass().equals(Long.class) ?
					(K) Long.valueOf(rowKey) :
					(K) rowKey;
		Optional<T> result = repository.findById(id);
		if(result.isPresent())
			return result.get();
		else
			return null;
	}
	
	@Override
	public String getRowKey(T object) {
		try {
			return String.valueOf(LazyTaskUtils.getFieldValue(object, "id"));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, null, e);
			return "";
		}
	}*/

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
            object = (T) repository.create(object);
        }
		if(clearTempData) {
			clearTempCreatedDataCollection();
		}
	}

	@Override
	public void doDBUpdate(boolean clearTempData) {
		for(T object : tempUpdatedDataCollection) {
            object = (T) repository.update(object);
        }
		if(clearTempData) {
			clearTempUpdatedDataCollection();
		}
	}

	@Override
	public void doDBDelete(boolean clearTempData) {
		for(T object : tempDeletedDataCollection) {
			repository.delete(object);
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
	public void addTempCreatedDataToDataSource() {
		dataSource.addAll(tempCreatedDataCollection);
		clearTempCreatedDataCollection();
	}

	@Override
	public void updateTempUpdatedDataToDataSource() {
		int index;
		for(T t : tempUpdatedDataCollection) {
            if((index = dataSource.indexOf(t)) != -1) {
                dataSource.set(index, t); // Mise a jour des donnees
            }
        }
		clearTempUpdatedDataCollection();
	}

	@Override
	public void deleteTempDeletedDataFromDataSource() {
		dataSource.removeAll(tempCreatedDataCollection);
		clearTempDeletedDataCollection();
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

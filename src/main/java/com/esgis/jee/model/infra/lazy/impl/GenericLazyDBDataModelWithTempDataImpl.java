package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyDBDataModelWithTempData;
import static com.esgis.jee.model.infra.lazy.GenericLazyDataModel.LOGGER;
import com.esgis.jee.model.infra.lazy.LazyTaskUtils;
import com.esgis.jee.model.infra.service.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.shiro.util.CollectionUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;


public class GenericLazyDBDataModelWithTempDataImpl<T extends Serializable, K> extends GenericLazyDBDataModelImpl<T, K> implements GenericLazyDBDataModelWithTempData<T, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Collection<T> tempCreatedDataCollection = new ArrayList<>(); // Pour les cas de nouvelles donnees elles peuvent avoir le meme id == null -> ArrayList pour les creations multiples
	protected final Collection<T> tempUpdatedDataCollection = new HashSet<>();
	protected final Collection<T> tempDeletedDataCollection = new HashSet<>();
	
	public GenericLazyDBDataModelWithTempDataImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass) {
		super(repository, dataClass, idClass);
	}
	
	public GenericLazyDBDataModelWithTempDataImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String[] globalFilterFields) {
		this(repository, dataClass, idClass);
		this.globalFilterFields = globalFilterFields;
	}
	
	public GenericLazyDBDataModelWithTempDataImpl(Service<T, K> repository, Class<T> dataClass, Class<K> idClass, String dataTbName) {
		this(repository, dataClass, idClass);
		this.dataTbName = dataTbName;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Predicate[] constructFilterExpressions(Map<String, FilterMeta> filterBy, CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> root) {
		Predicate[] result = super.constructFilterExpressions(filterBy, cb, criteriaQuery, root);
		// On enleve les donnees supprimees temporairement (et qui sont en BD)
		List<K> ids = tempDeletedDataCollection
				.stream()
				.map((t) -> {try {
					return (K) LazyTaskUtils.getFieldValue(t, "id");
				} catch (NullPointerException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
					LOGGER.log(Level.SEVERE, null, e);
					return null;
				}})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(ids)) {
			Predicate[] newResult = Arrays.copyOf(result, result.length + 1);
			newResult[newResult.length - 1] = cb.not(root.get("id").in(ids)); 
			result = newResult;
		}
		return result;
	}
	
	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// Compter les occurrences en BD qui correspondent (retire celles qui sont supprimées de la BD)
		int result = super.count(filterBy);
		// Ajouter les créés non supprimés qui correspondent
		List<T> createdNotDeleted = new ArrayList<>(tempCreatedDataCollection);
		createdNotDeleted.removeAll(tempDeletedDataCollection);
		result = result +  new GenericLazyCollectionDataModelImpl<>(createdNotDeleted, getGlobalFilterFields()).count(filterBy);
		// Retirer les supprimés en BD qui correspondent : déjà pris en compte
//		List<T> deletedInDB = new ArrayList<>(tempDeletedDataCollection);
//		deletedInDB.removeAll(tempCreatedDataCollection);
//		result = result -  new GenericLazyCollectionDataModelImpl<>(deletedInDB, getGlobalFilterFields()).count(filterBy);
		return result;
	}
	
	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		List<T> result = super.load(first, pageSize + tempDeletedDataCollection.size(), sortBy, filterBy);
		// Prise en compte des donnees temporaires
		result.addAll(tempCreatedDataCollection); // Ajout des nouvelles donnees
		int index;
        for(T t : tempUpdatedDataCollection) {
            if((index = result.indexOf(t)) != -1) {
                result.set(index, t); // Mise a jour des donnees
            }
        }
        result.removeAll(tempDeletedDataCollection); // Suppression des donnees
        // Application de Lazy Load collection
        // La necessite d'affectation du data fait partie des Effets de bord de la programmation imperative
        data = new GenericLazyCollectionDataModelImpl<>(result, getGlobalFilterFields())
        		.load(0, pageSize, sortBy, filterBy);
        return (List<T>) data;
	}
	
//	@Override
//    public T getRowData(String rowKey) {
//        T result = null;
//        try {
//            //Method hashCodeMethod = LazyTaskUtils.getAccessibleMethod(getDataClass(), "hashCode");
//            result = data
//                    .stream()
//                    .filter((t) -> {
//                        try {
//                            //return rowKey.equals(String.valueOf(hashCodeMethod.invoke(t)));
//                        	return rowKey.equals(String.valueOf(Objects.hashCode(t)));
//                        } catch (Exception ex) {
//                        	LOGGER.log(Level.SEVERE, null, ex);
//                            return false;
//                        }
//                    })
//                    .findFirst()
//                    .get();
//        } catch (Exception ex) {
//        	LOGGER.log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }
	
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
		// On ajoute que les donnees a mettre a jour dans la BD
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

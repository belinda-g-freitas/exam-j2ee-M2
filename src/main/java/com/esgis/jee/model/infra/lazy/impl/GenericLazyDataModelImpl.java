package com.esgis.jee.model.infra.lazy.impl;

import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GenericLazyDataModelImpl<T extends Serializable, K> extends LazyDataModel<T> implements GenericLazyDataModel<T, K> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String dataTbName = ":form1:dataTb";
	protected String[] globalFilterFields;
	protected Class<T> dataClass;
	protected Class<K> idClass;
	protected Collection<T> data = new ArrayList<>();
	protected Map<String, FilterMeta> filterBy;
	protected Map<String, SortMeta> sortBy;
	
	protected GenericLazyDataModelImpl() {}
	
	@Override
	public void setGlobalFilterFields(String[] globalFilterFields) {
		this.globalFilterFields = globalFilterFields;
	}
	
	@Override
	public boolean hasData() {
		return !CollectionUtils.isEmpty(data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getData(int rowIndex) {
		return (T) data.toArray()[rowIndex];
	}
	
	@Override
	public T getDataFromDisplayedData(int rowIndex) {
		return load(rowIndex, 1, sortBy, filterBy).get(0);
	}
	
	/**
	 * Definit dans le DataModel les parametres actuels de tri et de filtre
	 * Necessaire pour faire fonctionner correctement la methode {@link public T getDataFromDisplayedData(int rowIndex);}
	 */
	protected void defineSortAndFilter(Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		this.sortBy = sortBy;
		this.filterBy = filterBy;
	}
	
	/**
	 * Verifie si une classe est celle d'une date
	 * @param classe la classe de check
	 * @return Oui si c'est une date. Non sinon
	 */
	protected boolean isDate(Class<?> classe) {
		return Date.class.equals(classe) ||
				Calendar.class.equals(classe) ||
				LocalDate.class.equals(classe) || 
				LocalTime.class.equals(classe) || 
				LocalDateTime.class.equals(classe) || 
				ZonedDateTime.class.equals(classe);
	}
	
	/**
	 * Verifie si une classe est celle d'un type primitif, nombre
	 * @param classe la classe de check
	 * @return oui c'est un type primitif, nombre. Non sinon
	 */
	protected boolean isPrimNombre(Class<?> classe) {
		return classe.equals(int.class) || 
				classe.equals(long.class) || 
				classe.equals(float.class) || 
				classe.equals(double.class) || 
				classe.equals(byte.class) || 
				classe.equals(short.class);
	}
	
	@Override
	public String[] getGlobalFilterFields() {
		if(globalFilterFields == null) {
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(dataTbName);
			UIComponent columnsRow = dataTable
					.getColumnGroup("header")
					.getChildren().get(2); // Le row des colonnes du RowGroup
			globalFilterFields = columnsRow
				.getChildren()
				.stream()
				.filter(t -> ((UIColumn)t).isFilterable() && !StringUtils.isBlank(((UIColumn)t).getField())) // On suppose que le champ Field est defini
				.map(t -> ((UIColumn)t).getField())
				.collect(Collectors.toList())
				.toArray(new String[0]);
		}
		return globalFilterFields;
	}
	
	/**
     * <pre>NB : À redéfinir si la méthode hashCode n'est pas utilisée pour identifier les objets</pre>
     *
     * @param rowKey
     * @return
     */
    @Override
    public T getRowData(String rowKey) {
        T result = null;
        try {
            //Method hashCodeMethod = LazyTaskUtils.getAccessibleMethod(getDataClass(), "hashCode");
            result = data
                    .stream()
                    .filter((t) -> {
                        try {
                            //return rowKey.equals(String.valueOf(hashCodeMethod.invoke(t)));
                        	return rowKey.equals(String.valueOf(Objects.hashCode(t)));
                        } catch (Exception ex) {
                        	log.error(ex.getMessage());
                            return false;
                        }
                    })
                    .findFirst()
                    .get();
        } catch (Exception ex) {
        	log.error(String.format("Recherche pour le rowKey %s dans la liste %s échouée", rowKey, data));
        }
        return result;
    }

    /**
     * <pre>NB : À redéfinir si la méthode hashCode n'est pas utilisée pour identifier les objets</pre>
     *
     * @param object
     * @return
     */
    @Override
    public String getRowKey(T object) {
    	String result = null;
        try {
        	result = String.valueOf(Objects.hashCode(object));
            //result = String.valueOf(LazyTaskUtils.getAccessibleMethod(object.getClass(), "hashCode").invoke(object));
        } catch (Exception ex) {
        	log.error(String.format("Récupération du rowKey de %s échouée", object));
        }
        return result;
    }
	
	/**
	 * Retourne la classe de la donnee manipulee par le LazyModel
	 * @return la classe resultante
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getDataClass() {
		if(dataClass == null) {
			dataClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; // Fonctionne si une classe avec des parametres statiques a ete definie
		}
		return dataClass;
	}
	
	/**
	 * Retourne la classe de l'ID de la donnee manipulee par le LazyModel
	 * @return la classe resultante
	 */
	@SuppressWarnings("unchecked")
	protected Class<K> getIDClass() {
		if(idClass == null) {
			idClass = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]; // Fonctionne si une classe avec des parametres statiques a ete definie
		}
		return idClass;
	}
	
	public void setDataTbName(String dataTbName) {
		this.dataTbName = dataTbName;
	}
}

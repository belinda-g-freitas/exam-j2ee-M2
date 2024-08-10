package com.esgis.jee.model.infra.lazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

public interface GenericLazyDataModel<T extends Serializable, K> {
	
	public static final String GLOBAL_FILTER_NAME = "globalFilter";
	public static final Logger LOGGER = Logger.getLogger(GenericLazyDataModel.class.getName());
	
	/**
	 * Charge les donnees
	 * @param first
	 * @param pageSize
	 * @param sortBy
	 * @param filterBy
	 * @return
	 */
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);
	
	/**
	 * Compte les donnees
	 * @param filterBy
	 * @return
	 */
	public int count(Map<String, FilterMeta> filterBy);
	
	/**
	 * Retourne les champs de filtre global
	 * @return
	 */
	public String[] getGlobalFilterFields();
	
	/**
	 * Defini les champs de filtre
	 * @param globalFilterFields
	 */
	public void setGlobalFilterFields(String[] globalFilterFields);
	
	/**
	 * Retourne la donnee a l'index specifie, parmis les donnees paginees
	 * @param rowIndex : index ne devrant jamais exceder la taille de la pagination
	 * @return
	 */
	public T getData(int rowIndex);
	
	/**
	 * Retourne la donnee a l'index specifie, parmis toutes les donnees actuellement affichees du DataTable (apres filtres et tris)
	 * @param rowIndex : index ne devrant jamais exceder la taille des donnees actuellement affichees
	 * @return
	 */
	public T getDataFromDisplayedData(int rowIndex);
	
	/**
	 * Verifie si le modele dispose de donnees ou pas
	 * @return true s'il y a des donnees, false sinon
	 */
	public boolean hasData();
	
	/**
	 * 
	 */
	public void setDataTbName(String dataTbName);
}
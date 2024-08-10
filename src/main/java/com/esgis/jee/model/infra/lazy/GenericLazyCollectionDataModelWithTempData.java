package com.esgis.jee.model.infra.lazy;

import java.io.Serializable;

public interface GenericLazyCollectionDataModelWithTempData<T extends Serializable, K> extends GenericLazyCollectionDataModel<T, K>, HasTempData<T>, HasDBActions<T> {
	
	/**
	 * Ajoute les donnees temporaires de creation a la source de donnees
	 * @param data
	 */
	public void addTempCreatedDataToDataSource();
	
	/**
	 * MAJ les donnees temporaires de mise a jour a la source de donnees
	 * @param data
	 */
	public void updateTempUpdatedDataToDataSource();
	
	/**
	 * Supprimer les donnees temporaires de suppression de la source de donnees
	 * @param data
	 */
	public void deleteTempDeletedDataFromDataSource();
}
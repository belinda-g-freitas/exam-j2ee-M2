package com.esgis.jee.model.infra.lazy;

import java.io.Serializable;
import java.util.Collection;

public interface GenericLazyCollectionDataModel<T extends Serializable, K> extends GenericLazyDataModel<T, K> {
	
	/**
	 * Ajoute des donnees a la source de donnees
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void addData(T... data);
	
	/**
	 * Ajoute des donnees a la source de donnees
	 * @param data
	 */
	public void addData(Collection<T> data);
	
	/**
	 * MAJ des donnees a la source de donnees
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void updateData(T... data);
	
	/**
	 * MAJ des donnees a la source de donnees
	 * @param data
	 */
	public void updateData(Collection<T> data);
	
	/**
	 * Supprime des donnees de la source de donnees
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void deleteData(T... data);
	
	/**
	 * Supprime des donnees de la source de donnees
	 * @param data
	 */
	public void deleteData(Collection<T> data);
}
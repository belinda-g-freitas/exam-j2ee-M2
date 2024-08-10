package com.esgis.jee.model.infra.lazy;

import java.util.Collection;

public interface HasTempData<T> {
	
	/**
	 * Compte le nombre donnees temporaires de creation
	 * @return
	 */
	public int countTempCreatedData();
	
	/**
     * <pre>Verifie s'il y a des donnees temporaires de creation</pre>
     * @return true s'il y a des donnees, false sinon
     */
    public boolean hasCreatedData();
	
	 /**
     * <pre>Ajoute une(ou plusieurs) donnée(s) temporaire(s) de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void addTempCreatedData(T... data);
    
    /**
     * <pre>Ajoute plusieurs données temporaires de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @param dataCollection 
     */
    public void addTempCreatedData(Collection<T> dataCollection);
    
    /**
     * <pre>Recupere les données temporaires de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @return
     */
    public Collection<T> getTempCreatedData();
    
    /**
	 * Compte le nombre donnees temporaires de MAJ
	 * @return
	 */
	public int countTempUpdatedData();
    
    /**
     * <pre>Verifie s'il y a des donnees temporaires de mise a jour</pre>
     * @return true s'il y a des donnees, false sinon
     */
    public boolean hasUpdatedData();
    
    /**
     * <pre>Ajoute une(ou plusieurs) donnée(s) temporaire(s) de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void addTempUpdatedData(T... data);
    
    /**
     * <pre>Ajoute plusieurs données temporaires de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @param dataCollection 
     */
    public void addTempUpdatedData(Collection<T> dataCollection);
    
    /**
     * <pre>Recupere les données temporaires de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @return
     */
    public Collection<T> getTempUpdatedData();
    
    /**
	 * Compte le nombre donnees temporaires de suppression
	 * @return
	 */
	public int countTempDeletedData();
    
    /**
     * <pre>Verifie s'il y a des donnees temporaires de suppression</pre>
     * @return true s'il y a des donnees, false sinon
     */
    public boolean hasDeletedData();
    
    /**
     * <pre>Ajoute une(ou plusieurs) donnée(s) temporaire(s) de suppression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void addTempDeletedData(T... data);
    
    /**
     * <pre>Ajoute plusieurs données temporaires de suppression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @param dataCollection 
     */
    public void addTempDeletedData(Collection<T> dataCollection);
    
    /**
     * <pre>Supprime des donnees temporairement</pre>
     * <pre>Ajoute aux donnees temporaires si la donnees n'est pas en creation, et supprime des donnees en creation sinon</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void deleteDataTemp(T... data);
    
    /**
     * <pre>Supprime des donnees temporairement</pre>
     * <pre>Ajoute aux donnees temporaires si la donnees n'est pas en creation, et supprime des donnees en creation sinon</pre>
     * @param dataCollection 
     */
    public void deleteDataTemp(Collection<T> dataCollection);
    
    /**
     * <pre>Recupere les données temporaires de supression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @return
     */
    public Collection<T> getTempDeletedData();
    
    /**
     * <pre>Retire une(ou plusieurs) donnée(s) temporaire(s) de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void removeTempCreatedData(T... data);
    
    /**
     * <pre>Retire plusieurs données temporaires de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @param dataCollection 
     */
    public void removeTempCreatedData(Collection<T> dataCollection);
    
    /**
     * <pre>Retire une(ou plusieurs) donnée(s) temporaire(s) de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void removeTempUpdatedData(T... data);
    
    /**
     * <pre>Retire plusieurs données temporaires de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @param dataCollection 
     */
    public void removeTempUpdatedData(Collection<T> dataCollection);
    
    /**
     * <pre>Retire une(ou plusieurs) donnée(s) temporaire(s) de supression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @param data 
     */
    @SuppressWarnings("unchecked")
	public void removeTempDeletedData(T... data);
    
    /**
     * <pre>Retire plusieurs données temporaires de supression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @param dataCollection 
     */
    public void removeTempDeletedData(Collection<T> dataCollection);
    
    /**
     * <pre>Efface toutes les données temporaires de création</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     */
    public void clearTempCreatedDataCollection();
    
    /**
     * <pre>Efface toutes les données temporaires de mise à jour</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     */
    public void clearTempUpdatedDataCollection();
    
    /**
     * <pre>Efface toutes les données temporaires de supression</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     */
    public void clearTempDeletedDataCollection();

}

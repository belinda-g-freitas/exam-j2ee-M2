package com.esgis.jee.model.infra.lazy;

public interface HasDBActions<T> {
	
	/**
     * <pre>Crée dans la base de données, toutes les données temporaires de création</pre>
     * <pre>Après la création, aucune donnée temporaire de création n'est plus disponible</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de création : Il s'agit d'une donnée en ajout à la source de données</pre>
     * @throws java.lang.Exception
     */
    public void doDBCreate(boolean clearTempData);
    
    /**
     * <pre>Mets à jour dans la base de données, toutes les données temporaires de mise à jour</pre>
     * <pre>Après la mise à jour, aucune donnée temporaire de mise à jour n'est plus disponible</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de mise à jour : Il s'agit d'une donnée en modification à la source de données</pre>
     * @throws java.lang.Exception
     */
    public void doDBUpdate(boolean clearTempData);
    
    /**
     * <pre>Supprime de la base de données, toutes les données temporaires de supression</pre>
     * <pre>Après la suppression, aucune donnée temporaire de supression n'est plus disponible</pre>
     * <pre>Donnée temporaire : Il s'agit d'une donnée non enregistrée dans la source de données mais devant figurer dans le modèle</pre>
     * <pre>Donnée de supression : Il s'agit d'une donnée en suppression de la source de données</pre>
     * @throws java.lang.Exception
     */
    public void doDBDelete(boolean clearTempData);
}

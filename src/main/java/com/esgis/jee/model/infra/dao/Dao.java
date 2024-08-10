/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.infra.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 *
 * @author gyldas_atta_kouassi
 * @param <T>
 * @param <K>
 */
public interface Dao<T extends Serializable, K> extends Serializable{
    
    /**
     * Créer une nouvelle occurrence dans la base
     * @param entity to create
     * @return the entity after its persistence
     * @throws java.lang.RuntimeException
     */
    public T create(T entity) throws RuntimeException;
    
    /**
     * Recherche toutes les occurences dans la base
     * @return result list
     * @throws java.lang.RuntimeException
     */
    public List<T> read() throws RuntimeException;
    
    /**
     * Recherche toutes les occurences dans la base. Si withHints = true on utilise une requête avec les hints
     * @param withHints
     * @return result list
     * @throws java.lang.RuntimeException
     */
    public List<T> read(boolean withHints) throws RuntimeException;
    
    /**
     * Met à jour une occurrence dans la base
     * @param entity to update
     * @return 
     * @throws java.lang.RuntimeException
     */
    public T update(T entity) throws RuntimeException;
    
    /**
     * Supprime une occurrence dans la base
     * @param entity
     * @throws java.lang.RuntimeException
     */
    public void delete(T entity) throws RuntimeException;
    
    /**
     * Supprime les occurrences dans la base
     * @param list
     * @throws java.lang.RuntimeException
     */
    public void delete(List<T> list) throws RuntimeException;
    
    /**
     * Retrouve une entité par son identifiant
     * @param pk of the entity to find
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findByPk(K pk) throws RuntimeException;
    
    /**
     * <pre>Retrouve une entité par son identifiant. Si withHints = true on utilise une requête avec les hints</pre>
     * <pre>NB : On suppose que tous les champs identifiants sont nommés id</pre>
     * @param pk of the entity to find
     * @param withHints
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findByPk(K pk, boolean withHints) throws RuntimeException;
    
    /**
     * Trouve une liste d'entités à partir de certains critères
     * @param criteres
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public List<T> findAllBy(List<Critere> criteres) throws RuntimeException;
    
    /**
     * Trouve une liste d'entités à partir de certains critères. Si withHints = true on utilise une requête avec les hints
     * @param criteres
     * @param withHints
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public List<T> findAllBy(List<Critere> criteres, boolean withHints) throws RuntimeException;
    
    /**
     * Trouve une liste d'entités à partir de certains critères
     * @param criteres
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public List<T> findAllBy(Critere... criteres) throws RuntimeException;
    
    /**
     * Trouve une liste d'entités à partir de certains critères. Si withHints = true on utilise une requête avec les hints
     * @param withHints
     * @param criteres
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public List<T> findAllBy(boolean withHints, Critere... criteres) throws RuntimeException;
    
    /**
     * Trouve une entité à partir de certains critères
     * @param criteres
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(List<Critere> criteres) throws RuntimeException;
    
    /**
     * Trouve une entité à partir de certains critères. Si withHints = true on utilise une requête avec les hints
     * @param criteres
     * @param withHints
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(List<Critere> criteres, boolean withHints) throws RuntimeException;
    
    /**
     * Trouve une entité à partir de certains critères
     * @param criteres
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(Critere... criteres) throws RuntimeException;
    
    /**
     * Trouve une entité à partir de certains critères. Si withHints = true on utilise une requête avec les hints
     * @param withHints
     * @param criteres
     * @return the result entity
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(boolean withHints, Critere... criteres) throws RuntimeException;
    
    /**
     * Trouve une entité à partir d'une colonne et sa valeur
     * @param column
     * @param value
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(String column, Object value) throws RuntimeException;
    
    /**
     * Trouve une entité à partir d'une colonne et sa valeur
     * @param column
     * @param value
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(SingularAttribute<T, Object> column, Object value) throws RuntimeException;
    
    /**
     * Trouve une entité à partir d'une colonne et sa valeur. Si withHints = true on utilise une requête avec les hints
     * @param column
     * @param value
     * @param withHints
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(String column, Object value, boolean withHints) throws RuntimeException;
    
    /**
     * Trouve une entité à partir d'une colonne et sa valeur. Si withHints = true on utilise une requête avec les hints
     * @param column
     * @param value
     * @param withHints
     * @return the result list
     * @throws java.lang.RuntimeException
     */
    public T findOneBy(SingularAttribute<T, Object> column, Object value, boolean withHints) throws RuntimeException;
    
    /**
     * Créer une nouvelle occurrence dans la base
     * @param entity
     * @param flush synchronise avec la base immédiatement
     * @return 
     * @throws java.lang.RuntimeException
     */
    public T create(T entity, boolean flush) throws RuntimeException;
    
    /**
     * Met à jour une occurrence dans la base
     * @param entity
     * @param flush sychronise avec la base immédiatement
     * @return 
     * @throws java.lang.RuntimeException
     */
    public T update(T entity, boolean flush) throws RuntimeException;
    
    /**
     * Supprime une occurrence dans la base
     * @param entity
     * @param flush synchronise avec la base immédiatement
     * @throws java.lang.RuntimeException
     */
    public void delete(T entity, boolean flush) throws RuntimeException;
    
    /**
     * Supprime une liste d'occurrences dans la base
     * @param ids
     * @param flush synchronise avec la base immédiatement
     * @throws java.lang.RuntimeException
     */
    public void delete(List<K> ids, boolean flush) throws RuntimeException;
    
    /**
     * Créer une nouvelle occurrence dans la base
     * @param entity
     * @param flush synchronise avec la base immédiatement
     * @param refresh rafraichit l'entité
     * @return 
     * @throws java.lang.RuntimeException
     */
    public T create(T entity, boolean flush, boolean refresh) throws RuntimeException;
    
    /**
     * Met à jour une occurrence dans la base
     * @param entity
     * @param flush sychronise avec la base immédiatement
     * @param refresh rafraichit l'entité
     * @return 
     * @throws java.lang.RuntimeException
     */
    public T update(T entity, boolean flush, boolean refresh) throws RuntimeException;
    
    /**
     * Supprime une occurrence dans la base
     * @param entity
     * @param flush synchronise avec la base immédiatement
     * @param refresh rafraichit l'entité
     * @throws java.lang.RuntimeException
     */
    public void delete(T entity, boolean flush, boolean refresh) throws RuntimeException;
    
    /**
     * Teste si avec l'Id, l'objet existe dans la BD
     * @param id
     * @return 
     * @throws java.lang.RuntimeException 
     */
    public boolean exists(K id) throws RuntimeException;
    
    /**
     * Teste si l'objet existe dans la BD
     * 
     * Précondition : le champ id est le 1er déclaré dans la classe
     * @param entity
     * @return 
     * @throws java.lang.RuntimeException 
     */
    public boolean exists(T entity) throws RuntimeException;
    
    /**
     * Compte toutes les occurences de la BD
     * @return 
     * @throws java.lang.RuntimeException
    */
    public long countAll() throws RuntimeException;
    
    /**
     * @return the EntityManager
     */
    public EntityManager getEm();
    
    /**
     * Retourne les Hints nécessaires
     * @return 
     */
    public Map<String, Object> getHints();
    
    /**
     * Concatène à la requête les Hints nécessaires
     * @param query
     * @return 
     */
    public Query appendHints(Query query);
    
    /**
     * @return the EntityClass
     */
    public Class<T> findEntityClass();
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.dao;

import com.esgis.jee.model.infra.dao.exception.NoResultException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.CollectionUtils;
import com.esgis.jee.model.infra.reflect.ReflectUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.SingularAttribute;
import java.io.IOException;


/**
 *
 * @author gyldas_atta_kouassi
 * @param <T>
 * @param <K>
 */
public abstract class DaoImpl<T extends Serializable, K> implements Dao<T, K> {

    @PersistenceContext(unitName = PersistenceUtil.UNIT_NAME)
    private EntityManager em;

    public DaoImpl() {
    }

    protected Query appendHints(Query query, Map<String, Object> hints) {
        Iterator<String> it = hints.keySet().iterator();
        String key;
        while (it.hasNext()) {
            key = it.next();
            query = query.setHint(key, hints.get(key));
        }
        return query;
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public T create(T entity) throws RuntimeException {
        return create(entity, false);
    }

    @Override
    public List<T> read() throws RuntimeException {
        return read(false);
    }

    @Override
    public List<T> read(boolean withHints) throws RuntimeException {
        // Pour éviter les dégradations de performance, on n'utilise pas de CriteriaQuery
        return withHints ? appendHints(getEm()
                .createQuery(String.format("SELECT x FROM %s x ", findEntityClass().getName())))
                .getResultList() : getEm()
                        .createQuery(String.format("SELECT x FROM %s x ", findEntityClass().getName()))
                        .getResultList();
    }

    @Override
    public T update(T entity) throws RuntimeException {
        return this.update(entity, false);
    }

    @Override
    public void delete(T entity) throws RuntimeException {
        this.delete(entity, false);
    }

    @Override
    public void delete(List<T> list) throws RuntimeException {
        for (T e : list) {
            delete(e);
        }
    }

    @Override
    public T create(T entity, boolean flush) throws RuntimeException {
        return this.create(entity, flush, false);
    }

    @Override
    public T update(T entity, boolean flush) throws RuntimeException {
        return this.update(entity, flush, false);
    }

    @Override
    public void delete(T entity, boolean flush) throws RuntimeException {
        this.delete(entity, flush, false);
    }

    @Override
    public T create(T entity, boolean flush, boolean refresh) throws RuntimeException {
        getEm().persist(entity);
        if (flush) {
            getEm().flush();
        }
        if (refresh) {
            getEm().refresh(entity);
        }
        return entity;
    }

    @Override
    public T update(T entity, boolean flush, boolean refresh) throws RuntimeException {
        T dbEntity = getEm().merge(entity);
        if (flush) {
            getEm().flush();
        }
        if (refresh) {
            getEm().refresh(dbEntity);
        }
        return dbEntity;
    }

    @Override
    public void delete(T entity, boolean flush, boolean refresh) throws RuntimeException {
        getEm().remove(getEm().merge(entity));
        if (flush) {
            getEm().flush();
        }
        if (refresh) {
            getEm().refresh(entity);
        }
    }

    @Override
    public void delete(List<K> ids, boolean flush) throws RuntimeException {
        getEm()
                .createQuery(String.format("DELETE FROM %s x WHERE x.id IN :p", findEntityClass().getName()))
                .setParameter("p", ids)
                .executeUpdate();
        if (flush) {
            getEm().flush();
        }
    }

    @Override
    public T findByPk(K pk) throws RuntimeException {
        return findByPk(pk, false);
    }

    @Override
    public T findByPk(K pk, boolean withHints) throws RuntimeException {
        if (withHints) {
            /* return getEm().find(findEntityClass(), pk, getHints()); */
            // Pour éviter les dégradations de performance, on n'utilise pas de CriteriaQuery
            List<T> list = appendHints(getEm()
                .createQuery(String.format("SELECT x FROM %s x WHERE x.id = :p1 ", findEntityClass().getName()))
                .setParameter("p1", pk))
                .getResultList();
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } else {
            return getEm().find(findEntityClass(), pk);
        }
    }

    @Override
    public List<T> findAllBy(List<Critere> criteres) throws RuntimeException {
        return findAllBy(criteres, false);
    }

    @Override
    public List<T> findAllBy(List<Critere> criteres, boolean withHints) throws RuntimeException {
        String query = String.format("SELECT x FROM %s x ", findEntityClass().getName());
        int compteur = 0;
        String type;
        for (Critere c : criteres) {
            query += (compteur != 0) ? c.getOp().toString() : "WHERE";
            ++compteur;
            if (c.getValue() != null) {
                /* Vérification de type de cast */
                type = c
                        .getValue()
                        .getClass()
                        .getSimpleName()
                        .equals("Date") ? "DATE" : "VARCHAR(65535)";
                /* Conversion proprement dite */
                query += String.format(
                        " CAST (x.%s AS %s) %s CAST( ?%d AS %s) ",
                        c.getColumn(),
                        type,
                        (type.equals("DATE") ? "=" : "LIKE"),
                        compteur,
                        type);
                //query += " CAST ( x."+c.getColumn()+" AS "+type+") "+(type.equals("DATE")?"=":"LIKE")+" CAST ( ?"+(compteur)+" AS "+type+") ";
            } else {
                query += String.format("x.%s IS NULL ", c.getColumn());
            }
        }
        compteur = 0;
        Query q = getEm().createQuery(query);
        for (Critere c : criteres) {
            ++compteur; // Même si c'est null la logique précédente nous contraint à incrémenter le compteur
            if (c.getValue() != null) {
                q.setParameter(compteur, c.getValue());
            }
        }
        List<T> result = withHints ? appendHints(q)
                .getResultList() : q
                        .getResultList();
        if (CollectionUtils.isEmpty(result)) {
            throw new NoResultException("Aucun résultat");
        }
        return result;
    }

    @Override
    public List<T> findAllBy(Critere... criteres) throws RuntimeException {
        return findAllBy(false, criteres);
    }

    @Override
    public List<T> findAllBy(boolean withHints, Critere... criteres) throws RuntimeException {
        return findAllBy(Arrays.asList(criteres), withHints);
    }

    @Override
    public synchronized T findOneBy(List<Critere> criteres) throws RuntimeException {
        return findOneBy(criteres, false);
    }

    @Override
    public T findOneBy(List<Critere> criteres, boolean withHints) throws RuntimeException {
        return findAllBy(criteres, withHints).get(0);
    }

    @Override
    public synchronized T findOneBy(Critere... criteres) throws RuntimeException {
        return findOneBy(false, criteres);
    }

    @Override
    public synchronized T findOneBy(boolean withHints, Critere... criteres) throws RuntimeException {
        return findAllBy(withHints, criteres).get(0);
    }

    @Override
    public synchronized T findOneBy(String column, Object value) throws RuntimeException {
        return findOneBy(column, value, false);
    }

    @Override
    public synchronized T findOneBy(SingularAttribute<T, Object> column, Object value) throws RuntimeException {
        return findOneBy(column.getName(), value);
    }

    @Override
    public synchronized T findOneBy(String column, Object value, boolean withHints) throws RuntimeException {
        return findOneBy(withHints, new Critere(column, value));
    }

    @Override
    public synchronized T findOneBy(SingularAttribute<T, Object> column, Object value, boolean withHints) throws RuntimeException {
        return findOneBy(column.getName(), value, withHints);
    }

    @Override
    public synchronized boolean exists(K id) throws RuntimeException {
        return (id != null && findByPk(id) != null);
    }

    @Override
    public synchronized boolean exists(T entity) throws RuntimeException {
        Field idField = ReflectUtils.getDeclaredField(findEntityClass(), "id"); // Champ d'identifiant : Id
        idField.setAccessible(true);
        K id = null;
        try {
            id = (K) idField.get(entity);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        idField.setAccessible(false);
        return exists(id);
    }

    @Override
    public Map<String, Object> getHints() {
        return new HashMap<>();
    }

    @Override
    public Query appendHints(Query query) {
        return appendHints(query, getHints());
    }

    @Override
    public synchronized long countAll() throws RuntimeException {
        return (long) getEm()
                .createQuery(String.format("SELECT COUNT(x) FROM %s x ", findEntityClass().getName()))
                .getSingleResult();
    }

    /**
     * Permet de retourner la classe entité
     *
     * @return
     */
    @Override
    public Class<T> findEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Récupère les propriétés inclus dans le fichier app.properties dans le
     * classpath
     *
     * @return le fichier correpondant
     */
    public static Properties getAppProperties() {
        Properties prop = new Properties();
        try {
            prop.load(DaoImpl.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prop;
    }
}

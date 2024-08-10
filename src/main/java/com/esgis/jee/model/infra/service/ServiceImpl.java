/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import com.esgis.jee.model.infra.dao.Critere;
import com.esgis.jee.model.infra.dao.DaoImpl;
import com.esgis.jee.model.infra.dao.exception.NoResultException;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.SingularAttribute;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author gyldas_atta_kouassi
 * @param <T>
 * @param <K>
 */
public abstract class ServiceImpl<T extends Serializable, K> implements Service<T, K> {


    protected T listToSingleResult(List<T> list) throws RuntimeException {
        if(!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        else {
            throw new NoResultException("Aucun résultat");
        }
    }
    
    /**
     * @return
     */
    @Override
    public EntityManager getEm() {
        return getDao().getEm();
    }

    @Override
    public synchronized T create(T entity) throws RuntimeException {
        return getDao().create(entity);
    }

    @Override
    public synchronized List<T> read() throws RuntimeException {
        return getDao().read();
    }

    @Override
    public synchronized List<T> read(Boolean withHints) throws RuntimeException {
        return getDao().read(withHints);
    }

    @Override
    public synchronized T update(T entity) throws RuntimeException {
        return getDao().update(entity);
    }

    @Override
    public synchronized void delete(T entity) throws RuntimeException {
        getDao().delete(entity);
    }

    @Override
    public synchronized void delete(List<T> list) throws RuntimeException {
        getDao().delete(list);
    }
    
    @Override
    public synchronized void delete(List<K> ids, boolean flush) throws RuntimeException {
        getDao().delete(ids, flush);
    }

    @Override
    public synchronized T findByPk(K pk) throws RuntimeException {
        return getDao().findByPk(pk);
    }

    @Override
    public synchronized T findByPk(K pk, boolean withHints) throws RuntimeException {
        return getDao().findByPk(pk, withHints);
    }

    @Override
    public synchronized List<T> findAllBy(List<Critere> criteres) throws RuntimeException {
        return getDao().findAllBy(criteres);
    }

    @Override
    public synchronized List<T> findAllBy(List<Critere> criteres, boolean withHints) throws RuntimeException {
        return getDao().findAllBy(criteres, withHints);
    }

    @Override
    public synchronized List<T> findAllBy(Critere... criteres) throws RuntimeException {
        return getDao().findAllBy(criteres);
    }

    @Override
    public synchronized List<T> findAllBy(boolean withHints, Critere... criteres) throws RuntimeException {
        return getDao().findAllBy(withHints, criteres);
    }

    @Override
    public synchronized T findOneBy(List<Critere> criteres) throws RuntimeException {
        return getDao().findOneBy(criteres);
    }

    @Override
    public synchronized T findOneBy(List<Critere> criteres, boolean withHints) throws RuntimeException {
        return getDao().findOneBy(criteres, withHints);
    }

    @Override
    public synchronized T findOneBy(Critere... criteres) throws RuntimeException {
        return getDao().findOneBy(criteres);
    }

    @Override
    public synchronized T findOneBy(boolean withHints, Critere... criteres) throws RuntimeException {
        return getDao().findOneBy(withHints, criteres);
    }

    @Override
    public synchronized T findOneBy(String column, Object value) throws RuntimeException {
        return getDao().findOneBy(column, value);
    }

    @Override
    public T findOneBy(SingularAttribute<T, Object> column, Object value) throws RuntimeException {
        return getDao().findOneBy(column, value);
    }

    @Override
    public synchronized T findOneBy(String column, Object value, boolean withHints) throws RuntimeException {
        return getDao().findOneBy(column, value, withHints);
    }

    @Override
    public T findOneBy(SingularAttribute<T, Object> column, Object value, boolean withHints) throws RuntimeException {
        return getDao().findOneBy(column, value, withHints);
    }

    @Override
    public synchronized T create(T entity, boolean flush) throws RuntimeException {
        return getDao().create(entity, flush);
    }

    @Override
    public synchronized T update(T entity, boolean flush) throws RuntimeException {
        return getDao().update(entity, flush);
    }

    @Override
    public synchronized void delete(T entity, boolean flush) throws RuntimeException {
        getDao().delete(entity, flush);
    }

    @Override
    public synchronized T create(T entity, boolean flush, boolean refresh) throws RuntimeException {
        return getDao().create(entity, flush, refresh);
    }

    @Override
    public synchronized T update(T entity, boolean flush, boolean refresh) throws RuntimeException {
        return getDao().update(entity, flush, refresh);
    }

    @Override
    public synchronized void delete(T entity, boolean flush, boolean refresh) throws RuntimeException {
        getDao().delete(entity, flush, refresh);
    }

    @Override
    public synchronized boolean exists(K id) throws RuntimeException {
        return getDao().exists(id);
    }

    @Override
    public synchronized boolean exists(T entity) throws RuntimeException {
        return getDao().exists(entity);
    }

    @Override
    public synchronized long countAll() throws RuntimeException {
        return getDao().countAll();
    }

    @Override
    public Map<String, Object> getHints() {
        return getDao().getHints();
    }

    @Override
    public synchronized Query appendHints(Query query) {
        return getDao().appendHints(query);
    }

    @Override
    public synchronized Class<T> findEntityClass() {
        return getDao().findEntityClass();
    }

    @Override
    public synchronized Map<String, String> getStringQueries() {
        Map<String, String> result = new HashMap<>(0);
        for (Field f : getClass().getDeclaredFields()) {
            if (f.getName().contains("StringQuery")) {
                try {
                    f.setAccessible(true);
                    result.put(f.getName(), String.valueOf(f.get(this)));
                    f.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                }
            }
        }
        return result;
    }
    
    public static <T extends Service> T getInstance(String app, Class<T> classe) {
        try {
            InitialContext context = new InitialContext();
            return (T) context.lookup(String.format("java:global/%s/%sImpl", app, classe.getSimpleName()));
        } catch (NamingException ex) {
            return CDI.current().select(classe).get();
        }
    }
    
    public static <T extends Service> T getInstance(Class<T> classe) {
        return getInstance(DaoImpl.getAppProperties().getProperty(APP_NAME_PROPERTY), classe);
    }
}
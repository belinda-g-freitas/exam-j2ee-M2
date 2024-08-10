/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.infra.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;


/**
 *
 * @author GiGi
 * @param <T>
 * @param <K>
 */
public abstract class AdminDaoImpl<T extends Serializable, K> extends DaoImpl<T, K>{
    
    @PersistenceContext(unitName =  PersistenceUtil.ADMIN_UNIT_NAME)
    private EntityManager em;
    
    public AdminDaoImpl() {
    }
    
    @Override
    public EntityManager getEm() {
        return em;
    }
}
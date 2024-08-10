/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.SessionDao;
import com.esgis.jee.model.admin.entity.Session;
import com.esgis.jee.model.admin.service.SessionService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminSessionServiceImpl")
public class SessionServiceImpl extends AdminServiceImpl<Session, Integer> implements SessionService {
    
    @Inject
    private SessionDao dao;
    
    /* Méthodes de classe */
    public static SessionService getInstance() {
        return AdminServiceImpl.getInstance(SessionService.class);
    }
    
    public static SessionService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, SessionService.class);
    }
    
    
    /* Redéfinitions */
    @Override
    public Dao<Session, Integer> getDao() {
        return dao;
    }
}
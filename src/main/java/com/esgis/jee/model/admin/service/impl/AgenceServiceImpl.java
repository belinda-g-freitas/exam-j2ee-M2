/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.AgenceDao;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.service.AgenceService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminAgenceServiceImpl")
public class AgenceServiceImpl extends AdminServiceImpl<Agence, Integer> implements AgenceService {
    
    @Inject
    private AgenceDao dao;
    
    /* Méthodes de classe */
    public static AgenceService getInstance() {
        return AdminServiceImpl.getInstance(AgenceService.class);
    }
    
    public static AgenceService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, AgenceService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<Agence, Integer> getDao() {
        return dao;
    }
}
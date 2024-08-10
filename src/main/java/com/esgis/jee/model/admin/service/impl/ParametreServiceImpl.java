/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.ParametreDao;
import com.esgis.jee.model.admin.entity.Parametre;
import com.esgis.jee.model.admin.service.ParametreService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminParametreServiceImpl")
public class ParametreServiceImpl extends AdminServiceImpl<Parametre, String> implements ParametreService {
    
    @Inject
    private ParametreDao dao;
    
    /* Méthodes de classe */
    public static ParametreService getInstance() {
        return AdminServiceImpl.getInstance(ParametreService.class);
    }
    
    public static ParametreService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, ParametreService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<Parametre, String> getDao() {
        return dao;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.RoleDao;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.service.RoleService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;


/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminRoleServiceImpl")
public class RoleServiceImpl extends AdminServiceImpl<Role, Integer> implements RoleService {
    
    @Inject
    private RoleDao dao;
    
    /* Méthodes de classe */
    public static RoleService getInstance() {
        return AdminServiceImpl.getInstance(RoleService.class);
    }
    
    public static RoleService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, RoleService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<Role, Integer> getDao() {
        return dao;
    }
}
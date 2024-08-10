/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.PermissionRoleDao;
import com.esgis.jee.model.admin.entity.PermissionRole;
import com.esgis.jee.model.admin.entity.PermissionRolePK;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.service.PermissionRoleService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminPermissionRoleServiceImpl")
public class PermissionRoleServiceImpl extends AdminServiceImpl<PermissionRole, PermissionRolePK>
        implements PermissionRoleService {

    @Inject
    private PermissionRoleDao dao;

    private final String trouverParRoleStringQuery = "SELECT  x "
            + "FROM  PermissionRole x "
            + "WHERE x.role = :p1";

    /* Méthodes de classe */
    public static PermissionRoleService getInstance() {
        return AdminServiceImpl.getInstance(PermissionRoleService.class);
    }

    public static PermissionRoleService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, PermissionRoleService.class);
    }

    /* Redéfinitions */
    @Override
    public Dao<PermissionRole, PermissionRolePK> getDao() {
        return dao;
    }

    @Override
    public List<PermissionRole> trouverParRole(Role role) throws Exception {
        return ((List<PermissionRole>) appendHints(getEm()
                .createQuery(trouverParRoleStringQuery)
                .setParameter("p1", role))
                .getResultList());
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.PermissionDao;
import com.esgis.jee.model.admin.entity.Operation;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.service.PermissionService;
import com.esgis.jee.model.infra.dao.Critere;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.dao.OperateurSql;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminPermissionServiceImpl")
public class PermissionServiceImpl extends AdminServiceImpl<Permission, Integer> implements PermissionService {

    @Inject
    private PermissionDao dao;
    
    /* Méthodes de classe */
    public static PermissionService getInstance() {
        return AdminServiceImpl.getInstance(PermissionService.class);
    }
    
    public static PermissionService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, PermissionService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<Permission, Integer> getDao() {
        return dao;
    }

    @Override
    public Permission trouverDoAnythingPerm() throws RuntimeException {
        // (id, id_IHM, operation) = (0, 0, 'ALL')
        return findOneBy(
                new Critere("id", 0),
                new Critere(OperateurSql.AND, "ihm.id", 0),
                new Critere(OperateurSql.AND, "operation", Operation.ALL.getCode())
        );
    }
    
    @Override
    public List<Permission> trouverToutSaufDoAnythingPerm() throws RuntimeException {
        Permission doAnythingPerm = trouverDoAnythingPerm();
        return read()
                .stream()
                .filter((t) -> {
                    return !(t.equals(doAnythingPerm));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> trouverNomPermissionsActives() throws RuntimeException {
        return (List<String>) getEm()
                .createQuery("SELECT x.nom FROM Permission x WHERE x.active = true")
                .getResultList();
    }
    
    @Override
    public List<Permission> trouverPermissionsActives() throws RuntimeException {
        return (List<Permission>) getEm()
                .createQuery("SELECT x FROM Permission x WHERE x.active = true")
                .getResultList();
    }
}

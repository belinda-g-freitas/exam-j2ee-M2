/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.PermissionUtilisateurDao;
import com.esgis.jee.model.admin.entity.PermissionUtilisateur;
import com.esgis.jee.model.admin.entity.PermissionUtilisateurPK;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.PermissionUtilisateurService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminPermissionUtilisateurServiceImpl")
public class PermissionUtilisateurServiceImpl extends AdminServiceImpl<PermissionUtilisateur, PermissionUtilisateurPK> implements PermissionUtilisateurService {
    
    @Inject
    private PermissionUtilisateurDao dao;
    
    private final String trouverParUtilisateurStringQuery
            = "SELECT  x "
            + "FROM    PermissionUtilisateur x "
            + "WHERE   x.utilisateur = :p1";
    
    /* Méthodes de classe */    
    public static PermissionUtilisateurService getInstance() {
        return AdminServiceImpl.getInstance(PermissionUtilisateurService.class);
    }
    
    public static PermissionUtilisateurService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, PermissionUtilisateurService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<PermissionUtilisateur, PermissionUtilisateurPK> getDao() {
        return dao;
    }

    @Override
    public List<PermissionUtilisateur> trouverParUtilisateur(Utilisateur utilisateur) throws Exception {
        return ((List<PermissionUtilisateur>) appendHints(getEm()
                .createQuery(trouverParUtilisateurStringQuery)
                .setParameter("p1", utilisateur))
                .getResultList());
    }
}
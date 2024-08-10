/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.RoleUtilisateurDao;
import com.esgis.jee.model.admin.entity.RoleUtilisateur;
import com.esgis.jee.model.admin.entity.RoleUtilisateurPK;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.RoleUtilisateurService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminRoleUtilisateurServiceImpl")
public class RoleUtilisateurServiceImpl extends AdminServiceImpl<RoleUtilisateur, RoleUtilisateurPK> implements RoleUtilisateurService {
    
    @Inject
    private RoleUtilisateurDao dao;
    
    private final String trouverParUtilisateurStringQuery
            = "SELECT  x "
            + "FROM    RoleUtilisateur x "
            + "WHERE   x.utilisateur = :p1";
    
    /* Méthodes de classe */
    public static RoleUtilisateurService getInstance() {
        return AdminServiceImpl.getInstance(RoleUtilisateurService.class);
    }
    
    public static RoleUtilisateurService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, RoleUtilisateurService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<RoleUtilisateur, RoleUtilisateurPK> getDao() {
        return dao;
    }

    @Override
    public List<RoleUtilisateur> trouverParUtilisateur(Utilisateur utilisateur) throws Exception {
        return ((List<RoleUtilisateur>) appendHints(getEm()
                .createQuery(trouverParUtilisateurStringQuery)
                .setParameter("p1", utilisateur))
                .getResultList());
    }
}
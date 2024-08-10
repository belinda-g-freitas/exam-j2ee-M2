/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import com.esgis.jee.model.admin.dao.ActiviteDao;
import com.esgis.jee.model.admin.entity.Activite;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.ActiviteService;
import com.esgis.jee.model.infra.service.AdminServiceImpl;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.ServiceImpl;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminActiviteServiceImpl")
public class ActiviteServiceImpl extends AdminServiceImpl<Activite, Long> implements ActiviteService {

    @Inject
    private ActiviteDao dao;

    private final String trouverParUtilisateursStringQuery
            = "SELECT  x "
            + "FROM  Activite x "
            + "WHERE x.session.utilisateur IN :p1",
            trouverParAgenceStringQuery
            = "SELECT  x "
            + "FROM  Activite x "
            + "WHERE x.session.utilisateur.agence = :p1";

    /* Méthodes de classe */
    public static ActiviteService getInstance() {
        return ServiceImpl.getInstance(ActiviteService.class);
    }

    public static ActiviteService getInstance(String app) {
        return ServiceImpl.getInstance(app, ActiviteService.class);
    }

    /* Redéfinitions */
    @Override
    public Dao<Activite, Long> getDao() {
        return dao;
    }

    @Override
    public List<Activite> trouverParUtilisateurs(List<Utilisateur> utilisateurs) throws Exception {
        return ((List<Activite>) appendHints(getEm()
                .createQuery(trouverParUtilisateursStringQuery)
                .setParameter("p1", utilisateurs))
                .getResultList());
    }

    @Override
    public List<Activite> trouverParAgence(Agence agence) throws Exception {
        return ((List<Activite>) appendHints(getEm()
                .createQuery(trouverParAgenceStringQuery)
                .setParameter("p1", agence))
                .getResultList());
        //return trouverParUtilisateurs(UtilisateurServiceImpl.getInstance().trouverParAgence(agence));
    }
}

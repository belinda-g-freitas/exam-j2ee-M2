/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.IhmDao;
import com.esgis.jee.model.admin.entity.Ihm;
import com.esgis.jee.model.admin.service.IhmService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminIhmServiceImpl")
public class IhmServiceImpl extends AdminServiceImpl<Ihm, Integer> implements IhmService {
    
    @Inject
    private IhmDao dao;
    
    /* Méthodes de classe */
    public static IhmService getInstance() {
        return AdminServiceImpl.getInstance(IhmService.class);
    }
    
    public static IhmService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, IhmService.class);
    }
    
    /* Redéfinitions */
    @Override
    public Dao<Ihm, Integer> getDao() {
        return dao;
    }

    @Override
    public Ihm trouverChangePasswordIhm() throws Exception{
        return findOneBy("nom", NOM_CHANGE_PASSWORD_IHM);
    }

    @Override
    public Ihm findByNom(String nomIHM) {
        return (Ihm) getEm()
                .createQuery(
                        "SELECT x "
                      + "FROM Ihm x "
                      + "WHERE x.nom = :p1")
                .setParameter("p1", nomIHM)
                .getResultList()
                .get(0);
    }
}
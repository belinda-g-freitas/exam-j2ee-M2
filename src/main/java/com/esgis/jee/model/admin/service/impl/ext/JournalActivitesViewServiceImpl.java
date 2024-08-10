/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl.ext;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.esgis.jee.model.admin.dao.ext.JournalActivitesViewDao;
import com.esgis.jee.model.admin.entity.view.JournalActivitesView;
import com.esgis.jee.model.admin.service.ActiviteService;
import com.esgis.jee.model.admin.service.ext.JournalActivitesViewService;
import com.esgis.jee.model.infra.service.AdminServiceImpl;
import com.esgis.jee.model.infra.dao.Dao;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminJournalActivitesViewServiceImpl")
public class JournalActivitesViewServiceImpl extends AdminServiceImpl<JournalActivitesView, Long> implements JournalActivitesViewService {

    @Inject
    private JournalActivitesViewDao dao;
    
    /* Méthodes de classe */
    public static ActiviteService getInstance() {
        return AdminServiceImpl.getInstance(ActiviteService.class);
    }

    public static ActiviteService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, ActiviteService.class);
    }

    /* Redéfinitions */
    @Override
    public Dao<JournalActivitesView, Long> getDao() {
        return dao;
    }
}

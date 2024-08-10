/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.esgis.jee.model.admin.entity.view.JournalActivitesView;
import com.esgis.jee.model.admin.service.ext.JournalActivitesViewService;
import com.esgis.jee.model.infra.lazy.AppLazyDataModelFactory;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import com.esgis.jee.model.infra.misc.MessagesFactory;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author EL Capitain
 */
@Getter
@Setter
@ViewScoped
@ViewController(view = "adminJournalView")
@Named("adminJournalBean")
public class AdminJournalController extends ViewControllerImpl {

    /* Services */
    @EJB
    @Setter(AccessLevel.NONE)
    private JournalActivitesViewService service;
    
    /* Objets simples */
    private GenericLazyDataModel<JournalActivitesView, Long> dataModel;
    /* Listes */
    
    /* Tableaux */
    private final String[] filterableExps
            = new String[]{
                "idActivite",
                "idAgence",
                "nomAgence",
                "nomUtilisateur",
                "dateDebutSession",
                "dateFinSession",
                "detailsActivite",
                "dateRealisationActivite"};

    /* Types primitifs */
    private boolean createAllUsers;

    /* Beans injectés */
    public AdminJournalController() throws Exception  {
        super(AdminJournalController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            this.createAllUsers = parametreAgenceService.trouverCreateAllUsersParIdAgence(sessionBean.getFormObject().getAgence().getId());
            chargerActivites();
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminJournalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chargerActivites() {
        try {
            if (createAllUsers) {
                dataModel = AppLazyDataModelFactory.createLazyDBDataModel(
                        service,
                        JournalActivitesView.class,
                        Long.class,
                        filterableExps
                        /*"findAll"*/);
            } else {
                dataModel = AppLazyDataModelFactory.createLazyServiceDataModel(
                        service,
                        JournalActivitesViewService.class,
                        JournalActivitesView.class,
                        Long.class,
                        filterableExps,
                        "trouverParIdAgence",
                        sessionBean.getUserIdAgence());
            }
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminJournalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test de rendu sur les composants si permission y est
     *
     * @return
     */
    public boolean renderComponent() {
        return haveReadPermission();
    }
}

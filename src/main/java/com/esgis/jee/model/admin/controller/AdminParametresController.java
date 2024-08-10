/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.esgis.jee.model.admin.entity.ParametreAgence;
import com.esgis.jee.model.admin.entity.ParametreAgencePK;
import com.esgis.jee.model.admin.service.ParametreAgenceService;
import com.esgis.jee.model.infra.lazy.AppLazyDataModelFactory;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModelWithTempData;
import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import com.esgis.jee.model.infra.lazy.HasTempData;
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
@ViewController(view = "adminParametresView")
@Named("adminParametresBean")
public class AdminParametresController extends ViewControllerImpl {

    /* Services */
    @EJB
    @Setter(AccessLevel.NONE)
    private ParametreAgenceService service;

    /* Objets simples */
    private ParametreAgence formObject;
    private GenericLazyDataModel<ParametreAgence, ParametreAgencePK> dataModel;
    
    /* Listes */
    
    /* Tableaux */
    private final String[] filterableExps
            = new String[]{
                "parametre.code",
                "parametre.description",
                "agence.nom",
                "valeur"};
    
    /* Types primitifs */
    private boolean updateAllParams;

    /* Beans injectés */
    public AdminParametresController() throws Exception  {
        super(AdminParametresController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            updateAllParams = service.trouverUpdateAllParamsParIdAgence(sessionBean.getFormObject().getAgence().getId());
            chargerParametres();
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminParametresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chargerParametres() {
        try {
            if (updateAllParams) {
//                dataModel = AppLazyDataModelFactory.createLazyDBDataModelWithTempData(
//                        service,
//                        ParametreAgence.class,
//                        ParametreAgencePK.class,
//                        filterableExps
//                        /*"findAll"*/);
                dataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
                        service,
                        ParametreAgenceService.class,
                        ParametreAgence.class,
                        ParametreAgencePK.class,
                        filterableExps,
                        "read");
                
            } else {
                dataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
                        service,
                        ParametreAgenceService.class,
                        ParametreAgence.class,
                        ParametreAgencePK.class,
                        filterableExps,
                        "trouverParIdAgence",
                        sessionBean.getUserIdAgence());
            }
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminParametresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enregistrer() {
        // Mise à jour
        try {
            if(((HasTempData<ParametreAgence>)dataModel).hasUpdatedData()) {
                for(ParametreAgence o : ((HasTempData<ParametreAgence>)dataModel).getTempUpdatedData()) {
                    o = service.update(o);
                    messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_MERGE);
                    log(String.format("Modification : %s", o));
                }
                if(GenericLazyCollectionDataModelWithTempData.class.isAssignableFrom(dataModel.getClass())) {
                    // Collection based -> on MAJ la source de donnees
                    ((GenericLazyCollectionDataModelWithTempData<?,?>)dataModel).updateTempUpdatedDataToDataSource();
                } else {
                    ((HasTempData<?>)dataModel).clearTempUpdatedDataCollection();
                }
            }
        } catch (ValidationException ex) {
            messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
        } catch (RuntimeException ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_MERGE, ex.getMessage()));
            Logger.getLogger(AdminParametresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void annuler() {
        chargerParametres();
    }

    public void modifierParametre(ParametreAgence object) {
        formObject = object;
    }

    /**
     * Utilisé sur la boîte de dialogue de modification de la valeur du
     * paramètre
     */
    public void modifier() {
        ((HasTempData<ParametreAgence>)dataModel).addTempUpdatedData(formObject);
        messagesBean.addMessageWarn(String.format("%d paramètre(s) modifié(s), enregistrez pour confirmer", ((HasTempData<ParametreAgence>)dataModel).countTempUpdatedData()));
    }

    /**
     * Test de rendu sur les composants si permission y est
     *
     * @return
     */
    public boolean renderComponent() {
        return haveUpdatePermission();
    }
}
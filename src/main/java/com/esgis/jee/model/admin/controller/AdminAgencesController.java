/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Parametre;
import com.esgis.jee.model.admin.entity.ParametreAgence;
import com.esgis.jee.model.admin.entity.ParametreAgencePK;
import com.esgis.jee.model.admin.service.AgenceService;
import com.esgis.jee.model.admin.service.ParametreAgenceService;
import com.esgis.jee.model.admin.service.ParametreService;
import com.esgis.jee.model.infra.lazy.AppLazyDataModelFactory;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModel;
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
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author EL Capitain
 */
@Getter
@Setter
@ViewScoped
@ViewController(view = "adminAgencesView")
@Named("adminAgencesBean")
public class AdminAgencesController extends ViewControllerImpl {

    /* Services */
    @EJB
    @Setter(AccessLevel.NONE)
    private AgenceService service;
    @EJB
    @Setter(AccessLevel.NONE)
    private ParametreService parametreService;

    /* Objets simples */
    private Agence formObject, selectedObject;
    private ParametreAgence editedObject;
    private GenericLazyDataModel<Agence, Integer> dataModel;
    private GenericLazyDataModel<ParametreAgence, ParametreAgencePK> parametreAgenceDataModel;
    
    /* Listes */
    private List<Parametre> parametreList;
    private List<ParametreAgence> parametreAgenceFilteredList;
    
    /* Tableaux */
    private final String[] filterableExps
            = new String[]{
                "code",
                "nom"},
            parametreAgenceFilterableExps
            = new String[]{
                "parametre.code",
                "parametre.description",
                "valeur"};

    /* Types primitifs */
    private boolean updateAllParams;

    /* Beans injectés */
    public AdminAgencesController() throws Exception  {
        super(AdminAgencesController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            parametreList = parametreService.read();
            updateAllParams = parametreAgenceService.trouverUpdateAllParamsParIdAgence(sessionBean.getFormObject().getAgence().getId());
            dataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
                new ArrayList<>(),
                Agence.class,
                Integer.class,
                filterableExps);
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        effacer();
    }
    
    public void charger(boolean isSearchBtn) {
        try {
            if ((isSearchBtn && parametreAgenceService.trouverInitDataAtClickOnSBTNParIdAgence(sessionBean.getFormObject().getAgence().getId())) || (!isSearchBtn)) {
                dataModel = AppLazyDataModelFactory.createLazyServiceDataModel(
                        service, 
                        AgenceService.class,
                        Agence.class,
                        Integer.class,
                        filterableExps,
                        "read");
                // 
            }
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enregistrer() {
        if (selectedObject == null) {
            try {
                // Insertion
                formObject = service.create(formObject, true, true);
                //dataModel.doCreate(formObject);
                if(GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
                    // Collection based -> on ajoute a la source de donnees
                    ((GenericLazyCollectionDataModel<Agence,?>)dataModel).addData(formObject);
                }
                messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_RECORD);
                log(String.format("Insertion : %s", formObject));
                selectedObject = formObject; // On sélectionne l'utilisateur enregistré pour une éventuelle modification
                onSelection(); // On exécute les actions à effectuer lors de la sélection
            } catch (ValidationException ex) {
                messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
            } catch (RuntimeException ex) {
                messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_RECORD, ex.getMessage()));
                Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // Mise à jour
                formObject = selectedObject = service.update(formObject, true, true);
                //dataModel.doUpdate(formObject);
                if(GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
                    // Collection based -> on ajoute à la source de donnees
                    ((GenericLazyCollectionDataModel<Agence,?>)dataModel).updateData(formObject);
                }
                messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_MERGE);
                log(String.format("Modification : %s", formObject));
                onSelection(); // On exécute les actions à effectuer lors de la sélection
            } catch (ValidationException ex) {
                messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
            } catch (RuntimeException ex) {
                messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_MERGE, ex.getMessage()));
                Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void effacer() {
        formObject = new Agence(parametreList);
        try {
            parametreAgenceDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
            		formObject.getParametreAgenceList(),
            		ParametreAgence.class,
            		ParametreAgencePK.class,
            		parametreAgenceFilterableExps);
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectedObject = null;
    }
    
    /**
     * Action à exécuter lors de la sélection d'une facture
     */
    public void onSelection() {
        formObject = selectedObject;
        try {
            parametreAgenceDataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
                    parametreAgenceService,
                    ParametreAgenceService.class,
                    ParametreAgence.class,
                    ParametreAgencePK.class,
                    parametreAgenceFilterableExps,
                    "trouverParAgence",
                    formObject);
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminAgencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onCellEdit(CellEditEvent event) {
        super.onCellEdit(event);
        ParametreAgence updatedObject = parametreAgenceDataModel.getDataFromDisplayedData(event.getRowIndex());
        doUpdateParametreAgence(updatedObject);
    }
    
    @SuppressWarnings("unchecked")
	public void doUpdateParametreAgence(ParametreAgence editedObject) {
    	if(selectedObject != null) {
    		// Service based model
        	((HasTempData<ParametreAgence>)parametreAgenceDataModel).addTempUpdatedData(editedObject);
        	formObject.getParametreAgenceList().set(formObject.getParametreAgenceList().indexOf(editedObject), editedObject);
        }
    }

    /**
     * Test de rendu sur les composants si permission y est
     *
     * @return
     */
    public boolean renderComponent() {
        return (selectedObject == null && haveCreatePermission()) || (selectedObject != null && haveUpdatePermission());
    }

    /**
     * Test d'édition des paramètres de l'agence si permission y est
     *
     * @return true si 
     * <br> 0- l'utilisateur possède les droits de rendu des composants et </br>
     * <br> 1.1- on est en mode création de l'agence ou </br>
     * <br> 1.2- on est en mode édition et l'agence est celui de l'utilisateur ou </br>
     * <br> 1.3- on est en mode édition et l'utilisateur peut modifier tous les paramètres d'agence </br>
     */
    public boolean editParams() {
        return renderComponent() && ((selectedObject == null)
                || (selectedObject != null && formObject.equals(sessionBean.getFormObject().getAgence()))
                || (selectedObject != null && updateAllParams));
    }
}

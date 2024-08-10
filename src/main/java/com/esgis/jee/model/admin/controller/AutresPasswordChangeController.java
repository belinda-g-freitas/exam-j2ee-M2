/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.UtilisateurService;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
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
 * @author Gyldas
 */
@Getter
@Setter
@ViewScoped
@Named("autresPasswordChangeBean")
@ViewController(view = "autresPasswordChangeView")
public class AutresPasswordChangeController extends ViewControllerImpl {

    @EJB
    @Setter(AccessLevel.NONE)
    UtilisateurService service;

    private Utilisateur formObject;

    private String identifiant, motDePasse;

    public AutresPasswordChangeController() throws Exception  {
        super(AutresPasswordChangeController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        identifiant = sessionBean.getIdentifiant();
        motDePasse = sessionBean.getMotDePasse();
        formObject = sessionBean.getFormObject();
    }

    /**
     * Permet de changer le mot de passe du compte courrant
     */
    public void changePassword() {
        try {
            formObject.setMotDePasse(motDePasse);
            formObject.setLongueurMdp(motDePasse.length());
            formObject = service.update(formObject, true, true);
            messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_MERGE);
        } catch (ValidationException ex) {
            messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
        } catch (RuntimeException ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_MERGE, ex.getMessage()));
            Logger.getLogger(AutresPasswordChangeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

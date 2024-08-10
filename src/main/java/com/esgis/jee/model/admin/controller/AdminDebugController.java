/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.misc.MessagesFactory;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 *
 * @author EL Capitain
 */
@Getter
@Setter
@ViewScoped
@ViewController(view = "adminDebugView")
@Named("adminDebugBean")
public class AdminDebugController extends ViewControllerImpl {

    /* Services */

    /* Objets simples */
    
    /* Listes */
    /* Tableaux */

    /* Types primitifs */

    /* Beans injectés */
    public AdminDebugController() throws Exception {
        super(AdminDebugController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            //chargerFacturesSansColis(false);
            //chargerFacturesSoldeesMarqueesNonSoldees(false);
            //chargerEnregsSansColis(false);
            //chargerPaiementsFactRAPIncorrect(false);
            //typeCorrPaiementsFactRAPIncorrect = 'A';
            //chargerPaiementsEnregRAPIncorrect(false);
            //typePaiementsFactMontantIncorrect = 'A';
            //chargerPaiementsFactMontantIncorr(false);
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminDebugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    /**
     * Test de rendu sur les composants si permission y est
     *
     * @return
     */
    public boolean renderComponent() {
        return haveUpdatePermission() || haveReadPermission();
    }
}

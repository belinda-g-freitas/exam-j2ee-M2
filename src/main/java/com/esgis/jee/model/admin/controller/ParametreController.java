/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import com.esgis.jee.model.admin.service.ParametreAgenceService;
import com.esgis.jee.model.infra.controller.MessagesController;
import com.esgis.jee.model.infra.misc.MessagesFactory;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 *
 * @author EL Capitain
 */
@Getter
@Setter
@SessionScoped
@Named("parametreBean")
public class ParametreController implements Serializable {
    
    @EJB
    @Setter(AccessLevel.NONE)
    private ParametreAgenceService service;
    
    /* Types primitifs */
    private Integer idAgence;
    private String monnaie, datePattern, codeAgence, nomAgence, timeZoneId, locale, adresse, nomSociete, domaine, autresContact, nbEnreg, uniteMonn, armoirie, etat;
    
    /* Objets simples */
    
    /* Beans injectés */
    //@ManagedProperty(value = "#{messagesBean}")
    @Inject
    private MessagesController messagesBean;
    //@ManagedProperty(value = "#{sessionBean}")
    @Inject
    private SessionController sessionBean;

    public ParametreController() {
    }
    
    @PostConstruct
    public void init() {
        try {
            idAgence = sessionBean.getFormObject().getAgence().getId();
            codeAgence = sessionBean.getFormObject().getAgence().getCode();
            nomAgence = sessionBean.getFormObject().getAgence().getNom();
            monnaie = service.trouverMonnaieParIdAgence(idAgence);
            datePattern = service.trouverPatternDateParIdAgence(idAgence);
            timeZoneId = service.trouverTimezoneIdParIdAgence(idAgence);
            locale = service.trouverLocaleParIdAgence(idAgence);
            adresse = service.trouverAdresseParIdAgence(idAgence);
            nomSociete = service.trouverNomSocieteParIdAgence(idAgence);
            domaine = service.trouverDomaineParIdAgence(idAgence);
            autresContact = service.trouverAutresContactParIdAgence(idAgence);
            nbEnreg = service.trouverNbEnregParIdAgence(idAgence);
            uniteMonn = service.trouverMonnaieParIdAgence(idAgence);
            armoirie = service.trouverArmoirieParIdAgence(idAgence);
            etat = service.trouverEtatParIdAgence(idAgence);
        } catch (Exception ex) {
            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(ParametreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean filterByDate(Object value, Object filter, Locale locale) {
        if(filter == null) {
            return true;
        }
        if(value == null) {
            return false;
        }
        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
    }
    
    public boolean filterByTime(Object value, Object filter, Locale locale) {
        if(filter == null) {
            return true;
        }
        if(value == null) {
            return false;
        }
        return DateUtils.isSameInstant((Date) filter, (Date) value);
    }
    
    public String dateDefautPattern() {
        return "dd/MM/yyyy";
    }
    
    public String timeDefautPattern() {
        return "HH:mm:ss";
    }
}

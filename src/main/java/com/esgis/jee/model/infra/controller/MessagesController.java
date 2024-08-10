/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Gyldas
 */
@ApplicationScoped
@Named("messagesBean")
public class MessagesController implements Serializable {

    private FacesMessage message;

    public MessagesController() {
        message = new FacesMessage();
    }

    /**
     * Récupère une valeur dans le bundle des ressources
     *
     * @param key
     * @param ressourceBundleVar
     * @return
     */
    public static String getValueFromResBundle(String key, String ressourceBundleVar) {
        FacesContext context = FacesContext
                .getCurrentInstance();
        return context
                .getApplication()
                .evaluateExpressionGet(
                        context,
                        String.format("#{%s['%s']}", ressourceBundleVar, key),
                        String.class);
    }

    public static String getValueFromResBundle(String key) {
        return getValueFromResBundle(key, "bundle");
    }

    private void addMessage(String clientId, FacesMessage.Severity severity, String message, String detail) {
        this.message = new FacesMessage();
        this.message.setSeverity(severity);
        this.message.setSummary(message);
        this.message.setDetail(detail);
        FacesContext.getCurrentInstance().addMessage(clientId, this.message);
    }

    private void addMessage(String clientId, FacesMessage.Severity severity, String message) {
        addMessage(clientId, severity, message, null);
    }

    private void addMessage(FacesMessage.Severity severity, String message, String detail) {
        addMessage(null, severity, message, detail);
    }

    private void addMessage(FacesMessage.Severity severity, String message) {
        addMessage(null, severity, message);
    }

    public void addMessageInfo(String message, String detail) {
        this.addMessage(null, FacesMessage.SEVERITY_INFO, message, detail);
    }

    public void addMessageError(String message, String detail) {
        this.addMessage(null, FacesMessage.SEVERITY_ERROR, message, detail);
    }

    public void addMessageFatal(String message, String detail) {
        this.addMessage(null, FacesMessage.SEVERITY_FATAL, message, detail);
    }

    public void addMessageWarn(String message, String detail) {
        this.addMessage(null, FacesMessage.SEVERITY_WARN, message, detail);
    }

    public void addMessageInfoWithClientId(String clientId, String message) {
        this.addMessage(clientId, FacesMessage.SEVERITY_INFO, message);
    }

    public void addMessageErrorWithClientId(String clientId, String message) {
        this.addMessage(clientId, FacesMessage.SEVERITY_ERROR, message);
    }

    public void addMessageFatalWithClientId(String clientId, String message) {
        this.addMessage(clientId, FacesMessage.SEVERITY_FATAL, message);
    }

    public void addMessageWarnWithClientId(String clientId, String message) {
        this.addMessage(clientId, FacesMessage.SEVERITY_WARN, message);
    }

    public void addMessageInfo(String message) {
        this.addMessage(FacesMessage.SEVERITY_INFO, message);
    }

    public void addMessageError(String message) {
        this.addMessage(FacesMessage.SEVERITY_ERROR, message);
    }

    public void addMessageFatal(String message) {
        this.addMessage(FacesMessage.SEVERITY_FATAL, message);
    }

    public void addMessageWarn(String message) {
        this.addMessage(FacesMessage.SEVERITY_WARN, message);
    }

    public void addMessageInfoWithBundleKey(String key) {
        this.addMessage(FacesMessage.SEVERITY_INFO, getValueFromResBundle(key));
    }

    public void addMessageErrorWithBundleKey(String key) {
        this.addMessage(FacesMessage.SEVERITY_ERROR, getValueFromResBundle(key));
    }

    public void addMessageFatalWithBundleKey(String key) {
        this.addMessage(FacesMessage.SEVERITY_FATAL, getValueFromResBundle(key));
    }

    public void addMessageWarnWithBundleKey(String key) {
        this.addMessage(FacesMessage.SEVERITY_WARN, getValueFromResBundle(key));
    }

    public FacesMessage getMessage() {
        return message;
    }
}

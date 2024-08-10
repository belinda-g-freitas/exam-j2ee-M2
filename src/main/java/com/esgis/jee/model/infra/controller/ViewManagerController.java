/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.esgis.jee.model.admin.entity.Operation;
import com.esgis.jee.model.admin.controller.SessionController;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>Cette classe est utilisée pour gérer les droits au niveau des menus applicatifs</pre>
 * <pre>Elle est utilisée pour éviter l'instanciation des beans eux-mêmes pouvant engendrer un ralentissement considérable</pre>
 * 
 * @author gyldas_atta_kouassi
 */
@SessionScoped
@Named("viewManagerBean")
public class ViewManagerController implements Serializable {
    
    //@ManagedProperty(value = "#{sessionBean}")
    @Inject
    private SessionController sessionBean;
    
    /**
     * Permet d'ouvrir une vue
     *
     * @param view
     * @return
     */
    public String openView(String view) {
        try {
            // Sauvegarde comme dernier écran
            sessionBean.saveLastScreen(view);
            // Redirection vers la vue
            return String.format("%s?faces-redirect=true", view);
        } catch (Exception ex) {
            Logger.getLogger(ViewControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    /**
     * Permet de rediriger vers une page : l'extension par défaut est le xhtml
     *
     * @param view
     */
    public void redirect(String view) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(view + ".xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ViewControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permet de récupérer la valeur d'un paramètre lors d'une requête
     *
     * @param key
     * @return
     */
    public Object getParamFromRq(String key) {
        try {
            return ((HttpServletRequest) FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getRequest())
                    .getParameter(key);
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    /**
     * Permet de détecter si l'utilisateur connecté a au moins une permission sur des vues données
     *
     * @param views
     * @return
     */
    public boolean haveAnyPermission(List<String> views) {
        boolean result = false;
        for(String view : views) {
            result = result || haveAnyPermission(view);
        }
        return result;
    }
    
    /**
     * Permet de détecter si l'utilisateur connecté a au moins une permission sur une vue donnée
     *
     * @param view
     * @return
     */
    public boolean haveAnyPermission(String view) {
        return (haveCreatePermission(view) || haveReadPermission(view) || haveUpdatePermission(view) || haveDeletePermission(view) || havePrintPermission(view) || haveExportPermission(view) || haveImportPermission(view));
    }
    
    public Boolean haveCreatePermission(String view) {
        return sessionBean.havePermission(view, Operation.CREATE);
    }

    public Boolean haveReadPermission(String view) {
        return sessionBean.havePermission(view, Operation.READ);
    }

    public Boolean haveUpdatePermission(String view) {
        return sessionBean.havePermission(view, Operation.UPDATE);
    }

    public Boolean haveDeletePermission(String view) {
        return sessionBean.havePermission(view, Operation.DELETE);
    }

    public Boolean havePrintPermission(String view) {
        return sessionBean.havePermission(view, Operation.PRINT);
    }

    public Boolean haveExportPermission(String view) {
        return sessionBean.havePermission(view, Operation.EXPORT);
    }

    public Boolean haveImportPermission(String view) {
        return sessionBean.havePermission(view, Operation.IMPORT);
    }

    public SessionController getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionController sessionBean) {
        this.sessionBean = sessionBean;
    }
}
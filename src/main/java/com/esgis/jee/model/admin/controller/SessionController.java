/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import com.esgis.jee.model.admin.entity.Activite;
import com.esgis.jee.model.admin.entity.Ihm;
import com.esgis.jee.model.admin.entity.Operation;
import com.esgis.jee.model.admin.entity.Session;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.ActiviteService;
import com.esgis.jee.model.admin.service.IhmService;
import com.esgis.jee.model.admin.service.SessionService;
import com.esgis.jee.model.admin.service.UtilisateurService;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.licence.AppLicencesManager;
import com.esgis.jee.model.infra.misc.MavenPropertiesReader;
import com.esgis.jee.model.infra.misc.MessagesFactory;
import com.esgis.jee.model.infra.security.EntityRealm;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 *
 * @author EL Capitain
 */
@SessionScoped
@ViewController(view = "index")
@Named("sessionBean")
public class SessionController extends ViewControllerImpl {
    
    @EJB
    private UtilisateurService service;
    @EJB
    private SessionService sessionService;
    @EJB
    private ActiviteService activiteService;
    @EJB
    private IhmService  ihmService;

    private Utilisateur formObject;
    private Session session;
    private UsernamePasswordToken token;
    
    private String identifiant, motDePasse;
    
    public SessionController() throws Exception  {
        super(SessionController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        //manageLicence();
    }
    
    public void manageLicence() {
        // Gestion de licence
//        int remainingDays = (int) ((AppLicencesManager.LAZY_LOADING_LICENSE_EXPIRATION_DATE.getTime() - new Date().getTime())
//                                / (1000 * 60 * 60 * 24) );
//        if(remainingDays > 60) {
//            //messagesBean.addMessageInfoWithClientId("licenceMessage", String.format("La licence de l'outil BF007 expire dans %d jour(s)", remainingDays));
//        } else if(remainingDays > 14) {
//            messagesBean.addMessageWarnWithClientId("licenceMessage", String.format("La licence de l'outil BF007 expire dans %d jour(s)", remainingDays));
//        } else if(remainingDays >= 0) {
//            messagesBean.addMessageErrorWithClientId("licenceMessage", String.format("La licence de l'outil BF007 expire dans %d jour(s)", remainingDays));
//        } else {
//            messagesBean.addMessageErrorWithClientId("licenceMessage", "La licence de l'outil BF007 est expirée");
//        }
//        PrimeFaces.current().ajax().update("licenceMessage");
    }
    
    public String lazyloadLicenseDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(AppLicencesManager.LAZY_LOADING_LICENSE_EXPIRATION_DATE);
    }

    public void clean() {
        identifiant = "";
        motDePasse = "";
    }
    
    public String connect() {
        token = new UsernamePasswordToken(identifiant.trim(), motDePasse.trim());
        token.setRememberMe(false);
        System.out.println("Login : "+identifiant+ ", Pass = "+motDePasse);
        try {
            System.out.println("Subject : "+SecurityUtils.getSubject());
            SecurityUtils.getSubject().login(token);
            formObject = EntityRealm.getUtilisateur();
            System.out.println("Utilisateur : "+formObject);
            this.createSession();
            this.log("Connexion");
            return String.format("%s?faces-redirect=true", formObject.getDernierIHM().getNom());
        } catch (UnknownAccountException | LockedAccountException ex) {
            System.out.println("Exceptions : UnknownAccountException, LockedAccountException");
            messagesBean.addMessageError(ex.getMessage());
        } catch (IncorrectCredentialsException ex) {
            System.out.println("Exceptions : IncorrectCredentialsException");
            messagesBean.addMessageError(MessagesFactory.MSG_ERROR_AUTHENTICATE_INCORRECT_CREDENTIALS);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            System.out.println("Exceptions : AuthenticationException : "+ex.getCause().getMessage());
            messagesBean.addMessageError(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exceptions : Exception");
            messagesBean.addMessageError(ex.getMessage());
        }
        return "";
    }
    
    public Integer getUserIdAgence() {
        return formObject.getAgence().getId();
    }
    
    public String versionApp() {
    	return MavenPropertiesReader.getMavenProperty("app.version");
    }

    public String getIndex() {
        return String.format("%s?faces-redirect=true", "index");
    }
    
    public String disconnect() {
        SecurityUtils.getSubject().logout(); // Logout avec apache shiro
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        manageLicence();
        return getIndex();
    }
    
    //@PreDestroy
    public void preDestroyDisconnect() {
        log("Déconnexion");
        closeSession();
        clean();
    }

    protected void createSession() {
        try {
            session = new Session();
            session.setUtilisateur(formObject);
            session.setDateDebut(new Date());
            session = sessionService.create(session);
        } catch (Exception ex) {}
    }

    protected void closeSession() {
        try {
            session.setDateFin(new Date());
            session = sessionService.update(session);
        } catch (Exception ex) {}
    }

    /**
     * Permet de journaliser les opérations
     *
     * @param details
     */
    @Override
    public void log(String details) {
        try {
            activiteService.create(new Activite(details, session));
        } catch (Exception ex) {}
    }

    /**
     * Enregistre la dernière IHM sur laquelle l'utilisateur s'est rendu
     * <pre>
     *      La dernière IHM ne sera jamais l'IHM d'authentification
     *      De peur qu'il soit redirigé après la connexion sur le portail
     * </pre>
     *
     * @param nomIhm
     */
    public void saveLastScreen(String nomIhm) {
        try {
            if (!nomIhm.equalsIgnoreCase(this.getView())) {
                formObject.setDernierIHM(findIhm(nomIhm));
                formObject = service.update(formObject);
            }
        } catch (Exception ex) {}
    }

    /**
     * Trouve une IHM à partir de la liste des permissions
     * <pre>Utilisé pour enregistrer les dernières IHM</pre>
     * <pre>Intérêt : la liste est restreinte puisqu'on itère sur les permissions que possède l'utilisateur</pre>
     * @param nomIHM
     * @return
     */
    public Ihm findIhm(String nomIHM) {
        return ihmService.findByNom(nomIHM);
        /*return EntityRealm
                .getPermissions()
                .stream()
                .map(Permission::getIhm)
                .filter((t) -> {
                    return t.getNom().equalsIgnoreCase(nomEcran);})
                .findFirst()
                .get(); */
    }

    /**
     * Vérifie si l'utilisateur possède une permission
     * Utilisé pour tester les permissions de base : CREATE, READ, UPDATE etc.
     * @param view
     * @param operation
     * @return
     */
    public boolean havePermission(String view, Operation operation) {
        return EntityRealm.getSubject().isPermitted(String.format("%s %s", operation, view)); // Ex : Lire colisFacturationView
    }
    
    /**
     * Vérifie si l'utilisateur possède une permission
     * @param nomPerm
     * @return
     */
    public boolean havePermission(String nomPerm) {
        return SecurityUtils.getSubject().isPermitted(nomPerm);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    /**
     * @return the service
     */
    public UtilisateurService getService() {
        return service;
    }

    /**
     * @return the sessionService
     */
    public SessionService getSessionService() {
        return sessionService;
    }

    /**
     * @return the activiteService
     */
    public ActiviteService getActiviteService() {
        return activiteService;
    }

    /**
     * @return the formObject
     */
    public Utilisateur getFormObject() {
        return formObject;
    }

    /**
     * @param formObject the formObject to set
     */
    public void setFormObject(Utilisateur formObject) {
        this.formObject = formObject;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }
}
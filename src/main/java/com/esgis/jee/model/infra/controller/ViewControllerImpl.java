/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.esgis.jee.model.admin.controller.SessionController;
import com.esgis.jee.model.admin.entity.Operation;
import com.esgis.jee.model.admin.service.ParametreAgenceService;
import com.esgis.jee.model.infra.licence.AppLicencesManager;
import jakarta.ejb.EJB;
import jakarta.el.ELContext;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Gyldas
 */
@Getter
@Setter
public abstract class ViewControllerImpl implements Serializable {

    @EJB
    @Setter(AccessLevel.NONE)
    protected ParametreAgenceService parametreAgenceService;

    protected SessionController sessionBean;
    protected MessagesController messagesBean;

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private ELContext elc;
    
    private Class<?> myClass;
    
    public ViewControllerImpl(Class<?> myClass) throws Exception {
        this.myClass = myClass;
        AppLicencesManager.validerLicenceApplication();
        // Raison du if : éviter une inclusion infinie du SessionController
        System.out.println("Classe = "+myClass);
        if (!SessionController.class.equals(myClass)) {
            initBeans(true); // Initialisation de tous les beans (y compris le sessionBean)
        } else {
            initBeans(false); // Initialisation de tous les beans sauf le sessionBean
        }
    }

//    public ViewControllerImpl() throws Exception {
//        this(null);
//    }
    
    public TimeZone timeZoneJava() {
		return TimeZone.getTimeZone(timeZoneId());
	}
    
    public String timeZoneId() {
        try {
            return parametreAgenceService.trouverTimezoneIdParIdAgence(sessionBean.getUserIdAgence());//"Africa/Libreville";
        } catch (Exception ex) {
            Logger.getLogger(ViewControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    protected Date correctDateFin(Date dateFin) {
    	return correctDate(dateFin, 23, 59, 59, true);
    }
    
    protected Date correctDateDebut(Date dateDebut) {
    	return correctDate(dateDebut, 0, 0, 0, true);
    }
    
    protected Date correctDate(Date date, int hour, int minutes, int seconds, boolean noDateAfterNow) {
        //log.info("Date TS = "+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date));
        //DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime dateTime = ZonedDateTime.of(new java.sql.Timestamp(date.getTime())
                        .toLocalDateTime(), ZoneId.systemDefault()); // Timezone système
        dateTime = dateTime.withZoneSameInstant(timeZoneJava().toZoneId()); // TimeZone client
        //log.info("Date TC = "+format.format(dateTime));
        dateTime = dateTime.withHour(hour);
        dateTime = dateTime.withMinute(minutes);
        dateTime = dateTime.withSecond(seconds);
        ZonedDateTime now = ZonedDateTime.now(timeZoneJava().toZoneId());
        if(noDateAfterNow && dateTime.isAfter(now)) {
                dateTime = now;
        }
        dateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault()); // Timezone système
        //log.info("Date TS = "+format.format(dateTime));
        return Date.from(dateTime.toLocalDateTime().toInstant(dateTime.getOffset()));
    }

    /**
     * Permet d'initialiser la vue (en données)
     */
    public abstract void init();

    public Boolean haveCreatePermission() {
        return this.havePermission(Operation.CREATE);
    }

    public Boolean haveReadPermission() {
        return this.havePermission(Operation.READ);
    }

    public Boolean haveUpdatePermission() {
        return this.havePermission(Operation.UPDATE);
    }

    public Boolean haveDeletePermission() {
        return this.havePermission(Operation.DELETE);
    }

    public Boolean havePrintPermission() {
        return this.havePermission(Operation.PRINT);
    }

    public Boolean haveExportPermission() {
        return this.havePermission(Operation.EXPORT);
    }

    public Boolean haveImportPermission() {
        return this.havePermission(Operation.IMPORT);
    }

    /**
     * Permet de détecter si l'utilisateur connecté a au moins une permission
     * sur la vue courante
     *
     * @return
     */
    public Boolean haveAnyPermission() {
        return (haveCreatePermission() || haveReadPermission() || haveUpdatePermission() || haveDeletePermission() || havePrintPermission() || haveExportPermission() || haveImportPermission());
    }

    /**
     * Permet d'ouvrir la vue associée au controller courant
     *
     * @return
     */
    public String openView() {
        try {
            // Sauvegarde comme dernier écran
            sessionBean.saveLastScreen(getView());
            // Redirection vers la vue
            return String.format("%s?faces-redirect=true", getView());
        } catch (NoViewDefinedException ex) {
            Logger.getLogger(ViewControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Permet d'initialiser les Beans gérés principaux (SessionController &
     * BundleController)
     */
    private void initBeans(boolean withSessionBean) {
        refreshELC();
        if (withSessionBean) {
            sessionBean = getMBeanValue(SessionController.class);
        }
        refreshELC();
        messagesBean = getMBeanValue(MessagesController.class);
    }

    /**
     * Permet de récupérer l'instance courant du contexte d'expression EL
     */
    private void refreshELC() {
        elc = FacesContext.getCurrentInstance().getELContext();
    }

    /**
     * Permet de récupérer le nom donné à un ManagedBean
     */
    private String getMBeanName(Class c) {
        return ((Named) c.getAnnotation(Named.class)).value();
    }

    protected  <T> T getMBeanValue(Class<T> beanClass) {
        //return (T) (elc.getELResolver().getValue(elc, null, getMBeanName(beanClass)));
        return CDI.current().select(beanClass).get();
        //return (T) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elc, null, getMBeanName(beanClass));
        //return (T) elc.getELResolver().getValue(elc, null, getMBeanName(beanClass));
    }

    /**
     * Permet de détecter si l'utilisateur connecté a une permission sur la vue
     * en cours ou pas
     *
     * @param operation
     * @return
     */
    protected Boolean havePermission(Operation operation) {
        try {
            return sessionBean.havePermission(getView(), operation);
        } catch (NoViewDefinedException ex) {
            Logger.getLogger(ViewControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Permet de récupérer le nom de la vue associée au controleur courant en se
     * référant à l'annotation @ViewController
     *
     * @return
     * @throws NoViewDefinedException
     */
    protected String getView() throws NoViewDefinedException {
        try {
            return myClass.getAnnotation(ViewController.class).view();
        } catch (NullPointerException e) {
            throw new NoViewDefinedException("Aucune vue n'a été définie au niveau du controleur");
        }
    }

    /**
     * Récupère une valeur dans le bundle des ressources
     *
     * @param key
     * @param ressourceBundleVar
     * @return
     */
    protected String getValueFromResBundle(String key, String ressourceBundleVar) {
        return MessagesController.getValueFromResBundle(key, ressourceBundleVar);
    }

    protected String getValueFromResBundle(String key) {
        return MessagesController.getValueFromResBundle(key);
    }

    public  void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Edition de cellule de la Grille des données Afficher les anciennes et
     * nouvelles valeurs
     *
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        if ((event.getNewValue()) != null && !(event.getNewValue()).equals(event.getOldValue())) {
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Cellule modifiée ",
                                    "Ancienne valeur : " + (event.getOldValue()) + ", Nouvelle : " + (event.getNewValue()))
                    );
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Ligne modifiée ",
                                "Ligne : " + (event.getObject())));
    }

//    public void postProcessXLS(Object document) throws IOException {
//        AppLicencesManager.validerLicenceServiceExportPDFExcel();
//        HSSFWorkbook wb = (HSSFWorkbook) document;
//        HSSFSheet sheet = wb.getSheetAt(0);
//        HSSFRow header = sheet.getRow(0);
//        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
//        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
//            header.getCell(i).setCellStyle(cellStyle);
//        }
//    }
//
//    public void postProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
//        AppLicencesManager.validerLicenceServiceExportPDFExcel();
//        Document pdf = (Document) document;
//        pdf.addCreationDate();
//        //pdf.addTitle("Voyages / Liste de colis");
//        pdf.add(Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getResource("/resources/img/logo.png")));
//    }

    public void log(String details) {
        sessionBean.log(details);
    }
}

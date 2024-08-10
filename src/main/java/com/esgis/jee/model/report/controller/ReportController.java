///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.esgis.jee.model.report.controller;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRParameter;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import com.esgis.jee.model.admin.controller.ParametreController;
//import com.esgis.jee.model.infra.controller.MessagesController;
//import com.esgis.jee.model.infra.licence.AppLicencesManager;
//import jakarta.enterprise.context.SessionScoped;
//import jakarta.faces.annotation.ManagedProperty;
//import jakarta.faces.context.FacesContext;
//import jakarta.inject.Named;
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.http.HttpServletResponse;
//
///**
// *
// * @author Gyldas
// */
//@SessionScoped
//@Named("reportBean")
//public class ReportController implements Serializable {
//
//    private JasperPrint jasperPrint;
//    
//    @ManagedProperty(value = "#{parametreBean}")
//    private ParametreController parametreBean;
//    @ManagedProperty(value = "#{messagesBean}")
//    private MessagesController messagesBean;
//
//    private JasperPrint createJasperPrint(String report, List list, HashMap params) throws JRException {
//        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(list);
//        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(report);
//        return JasperFillManager.fillReport(reportPath, params, source);
//    }
//
//    private void init(String report, List list, HashMap params) throws JRException {
//        AppLicencesManager.validerLicenceServiceGestionRapports();
//        params.put(JRParameter.REPORT_LOCALE, new Locale(
//                parametreBean.getLocale().split("_")[0],
//                parametreBean.getLocale().split("_")[1]));
//        params.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone(parametreBean.getTimeZoneId()));
//        params.put("nomSoc", parametreBean.getNomSociete());
//        params.put("domaine", parametreBean.getDomaine());
//        params.put("autresContact", parametreBean.getAutresContact());
//        params.put("adresse", parametreBean.getAdresse());
//        jasperPrint = createJasperPrint(report, list, params);
//    }
//
//    private void PDF(String report, List list, HashMap params, String pdf) throws JRException, IOException {
//        init(report, list, params);
//        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + pdf + ".pdf");
//        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//        FacesContext.getCurrentInstance().responseComplete();
//    }
//    
//    public String getResourceRealPath(String name) {
//        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(String.format("resources/%s", name));
//    }
//
////    public void enregistrementPDF(Enregistrement entity) {
////        try {
////            final String report = "reports/enregistrement.jasper";
////            String pdf;
////            pdf = "ENREG_" + entity.getNumero();
////            HashMap<String, Object> params = new HashMap<>();
////            /*
////                Zone Title + pageHeader
////             */
////            params.put("logo", getResourceRealPath("img/logo.png"));
////            params.put("numero", entity.getNumero());
////            params.put("nomCompletExp", entity.trouverExpediteur().genererNomComplet());
////            params.put("contactExp", entity.trouverExpediteur().getContact());
////            params.put("lieuExp", entity.trouverLieuExp().genererNomComplet());
////            params.put("nomsCompletsDest", entity.genererNomsCompletsDest());
////            params.put("contactsDest", entity.genererContactsDest());
////            params.put("lieuxDest", entity.genererNomsCompletsLieuxDest());
////
////            /*
////                Zone Résumé
////             */
////            params.put("frais", entity.getFrais());
////            params.put("nbColis", entity.compterNbColis());
////            params.put("uniteMonn", parametreBean.getUniteMonn());
////
////            /*
////                Zone pageFooter
////             */
////            params.put("nomAgent", entity.getNomAgent());
////            params.put("dateEdition", entity.getDate());
////            params.put("notaBene", parametreBean.getNbEnreg());
////
////            PDF(report, entity.getColisList(), params, pdf);
////        } catch (JRException | IOException ex) {
////            messagesBean.addMessageError(String.format("%s ; Raison : %s", MessagesController.MSG_ERROR_DB_LOAD, ex.getMessage()));
////            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
////        }
////    }
//    
//    /**
//     * @return the parametreBean
//     */
//    public ParametreController getParametreBean() {
//        return parametreBean;
//    }
//
//    /**
//     * @param parametreBean the parametreBean to set
//     */
//    public void setParametreBean(ParametreController parametreBean) {
//        this.parametreBean = parametreBean;
//    }
//
//    public MessagesController getMessagesBean() {
//        return messagesBean;
//    }
//
//    public void setMessagesBean(MessagesController messagesBean) {
//        this.messagesBean = messagesBean;
//    }
//}

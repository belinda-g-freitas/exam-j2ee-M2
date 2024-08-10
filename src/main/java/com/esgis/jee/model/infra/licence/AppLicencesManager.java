/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.licence;

import java.io.IOException;
import java.util.Date;
import org.apache.shiro.authc.AuthenticationException;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class AppLicencesManager {
    
    public static final Date LAZY_LOADING_LICENSE_EXPIRATION_DATE = new Date(125, 2, 1); // 1er Mars
    
    /*
     * ***********************************************************************************************************
     * Licence application
     * ***********************************************************************************************************
     */
    public static void validerLicenceApplication() throws RuntimeException {
        // Temps d'utilisation de l'application
        /* if(new Date().after(new Date(121, 2, 1))) { // 1er Mars 2021
            throw new RuntimeException("La licence d'utilisation de l'application a expiré, renouvelez-la s'il vous plaît");
        } */
    }
    
    /*
     * ***********************************************************************************************************
     * Licence système d'administration applicative
     * ***********************************************************************************************************
     */
    public static void validerLicenceSystemeAdminApplicative() throws AuthenticationException {
        // Temps d'utilisation du système d'administration applicative
        /* if(new Date().after(new Date(119, 0, 27))) { // 27 Janvier 2019 
            throw new AuthenticationException("La licence d'utilisation du système d'administration applicative a expiré, renouvelez-la s'il vous plaît");
        } */
    }
    
    /*
     * ***********************************************************************************************************
     * Licence service de gestion Big data
     * ***********************************************************************************************************
     */
    public static void validerLicenceServiceLazyloading() throws RuntimeException {
        // Temps d'utilisation du service de gestion Big data
//        if(new Date().after(LAZY_LOADING_LICENSE_EXPIRATION_DATE)) {
//            throw new RuntimeException("La licence d'utilisation de l'outil BF007 a expiré, renouvelez-la s'il vous plaît");
//        }
    }
    
    /*
     * ***********************************************************************************************************
     * Licence service de gestion de rapports
     * ***********************************************************************************************************
     */
//    public static void validerLicenceServiceGestionRapports() throws JRException {
//        // Temps d'utilisation du service de gestion de rapports
//        /* if(new Date().after(new Date(120, 6, 25))) { // 25 Juillet 2020
//            throw new JRException("La licence d'utilisation du service de gestion de rapports a expiré, renouvelez-la s'il vous plaît");
//        } */
//    }
    
    /*
     * ***********************************************************************************************************
     * Licence service d’export de fichiers PDF & Excel
     * ***********************************************************************************************************
     */
    public static void validerLicenceServiceExportPDFExcel() throws IOException {
        // Temps d'utilisation du service d’export de fichiers PDF & Excel
        /* if(new Date().after(new Date(120, 11, 19))) { // 19 Décembre 2020
            throw new IOException("La licence d'utilisation du service d’export de fichiers PDF & Excel a expiré, renouvelez-la s'il vous plaît");
        } */
    }
}

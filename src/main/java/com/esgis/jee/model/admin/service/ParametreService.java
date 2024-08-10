/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.Parametre;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface ParametreService extends Service<Parametre, String> {
    
    public static String CODE_TAUX_TVA = "TTVA";
    public static String CODE_TAUX_CSS = "TCSS";
    public static String CODE_MONNAIE = "MONN";
    public static String CODE_PATTERN_DATE = "DATE_PAT";
    public static String CODE_NOM_AGENCE = "NOM_AG";
    public static String CODE_DEFAUT_MOT_DE_PASSE = "DEFAUT_MDP";
    public static String CODE_VOIR_TOUT_SUR_LE_BROUILLARD = "VIEW_ALL_BROUIL";
    public static String CODE_METTRE_A_JOUR_TOUS_LES_PARAMETRES = "UPDATE_ALL_PARAMS";
    public static String CODE_CREER_TOUS_LES_UTILISATEURS = "CREATE_ALL_USERS";
    public static String CODE_INITIALISER_LES_DONNEES_AU_CLICK_SUR_LE_BOUTON_RECHERCHER = "INIT_DATA_AT_CLICK_ON_SBTN";
    public static String CODE_CODE_DES_ETATS_IMPRIMABLES = "CODE_ETATS";
    public static String CODE_ID_TIMEZONE = "TIMEZONE_ID";
    public static String CODE_LOCALE = "LOCALE";
    public static String CODE_VOIR_TOUS_LES_ENREGISTREMENTS = "VIEW_ALL_ENREG";
    public static String CODE_VOIR_TOUTES_LES_FACTURES = "VIEW_ALL_FACT";
    public static String CODE_VOIR_TOUS_LES_RECUS = "VIEW_ALL_RECU";
    public static String CODE_ADRESSE = "ADR";
    public static String CODE_NOM_SOCIETE = "NOM_SOC";
    public static String CODE_DOMAINE = "DOM";
    public static String CODE_AUTRES_CONTACT = "ACONTACT";
    public static String CODE_NB_ENREG = "NB_ENREG";
    public static String CODE_ARMOIRIE = "ARMOIRIE";
    public static String CODE_ETAT = "ETAT";
    
    public static int LONGUEUR_MAX_MDP = 30;
    
}

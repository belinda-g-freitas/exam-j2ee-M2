/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.ParametreAgence;
import com.esgis.jee.model.admin.entity.ParametreAgencePK;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface ParametreAgenceService extends Service<ParametreAgence, ParametreAgencePK> {
    
    /**
     * Trouve la valeur d'un parametre à partir de son code et du code agence
     * 
     * Si la valeur est inexistante, la valeur par défaut du paramètre est retournée
     * @param codeParam
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverValeurParCodeParamEtIdAgence(String codeParam, Integer idAgence) throws Exception;
    
    /**
     * Trouve le taux de la TVA à partir de l'IdAgence
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public float trouverTauxTVAParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le taux de la CSS
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public float trouverTauxCSSParIdAgence(Integer idAgence) throws Exception;

    /**
     * Trouve la monnaie utilisée (XAF, XOF etc.)
     * @param idAgence
     * @return 
     * @throws java.lang.Exception 
     */
    public String trouverMonnaieParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le modèle (pattern) utilisé pour les dates (ex : dd MMM yyyy HH:mm:ss)
     * @param idAgence
     * @return 
     * @throws java.lang.Exception 
     */
    public String trouverPatternDateParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le mot de passe par défaut
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverDefautMdpParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve les paramètres d'une agence donnée
     * @param agence
     * @return
     * @throws Exception 
     */
    public List<ParametreAgence> trouverParAgence(Agence agence) throws Exception;
    
    /**
     * Trouve les paramètres d'une agence donnée
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public List<ParametreAgence> trouverParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre de visualisation de tout sur le brouillard
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverViewAllBrouilParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre de mise à jour de tous les paramètres
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverUpdateAllParamsParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre de création des utilisateurs de toutes les agences
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverCreateAllUsersParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre d'initialisation des données au click sur le bouton de recherche
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverInitDataAtClickOnSBTNParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le code des états imprimables
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverCodeEtatsParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve l'Id du Timezone (ex : Africa/Libreville)
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverTimezoneIdParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le locale officiel (ex : fr_GA)
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverLocaleParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre de visualisation de tous les enregistrements
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverViewAllEnregParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le paramètre de visualisation de tous les enregistrements
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverViewAllFactParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le numéro actuel des reçus
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public boolean trouverViewAllRecuParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve l'adresse de l'agence
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverAdresseParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le nom de société de l'agence
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverNomSocieteParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le domaine de l'agence
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverDomaineParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve les autres contacts de l'agence
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverAutresContactParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve le NB des enregistrements
     * @param idAgence
     * @return
     * @throws Exception 
     */
    public String trouverNbEnregParIdAgence(Integer idAgence) throws Exception;
    
    /**
     * Trouve l'armoirie
     * @param idAgence
     * @return 
     * @throws java.lang.Exception 
     */
    public String trouverArmoirieParIdAgence(Integer idAgence) throws Exception;
    
     /**
     * Trouve l'état
     * @param idAgence
     * @return 
     * @throws java.lang.Exception 
     */
    public String trouverEtatParIdAgence(Integer idAgence) throws Exception;
}

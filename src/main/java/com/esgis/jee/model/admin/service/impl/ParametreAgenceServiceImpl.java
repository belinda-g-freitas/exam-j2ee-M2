/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.apache.commons.text.StringEscapeUtils;
import com.esgis.jee.model.admin.dao.ParametreAgenceDao;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.ParametreAgence;
import com.esgis.jee.model.admin.entity.ParametreAgencePK;
import com.esgis.jee.model.admin.service.ParametreAgenceService;
import static com.esgis.jee.model.admin.service.ParametreService.*;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.service.AdminServiceImpl;

/**
 *
 * @author Gyldas
 */
@Stateless(name = "AdminParametreAgenceServiceImpl")
public class ParametreAgenceServiceImpl extends AdminServiceImpl<ParametreAgence, ParametreAgencePK> implements ParametreAgenceService {

    @Inject
    private ParametreAgenceDao dao;

    private final String trouverParAgenceStringQuery
            = "SELECT x "
            + "FROM   ParametreAgence x "
            + "WHERE  x.agence = :p1 ";
    private final String trouverParIdAgenceStringQuery
            = "SELECT x "
            + "FROM   ParametreAgence x "
            + "WHERE  x.agence.id = :p1 ";

    /* Méthodes de classe */
    public static ParametreAgenceService getInstance() {
        return AdminServiceImpl.getInstance(ParametreAgenceService.class);
    }

    public static ParametreAgenceService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, ParametreAgenceService.class);
    }

    /* Redéfinitions */
    @Override
    public Dao<ParametreAgence, ParametreAgencePK> getDao() {
        return dao;
    }

    @Override
    public String trouverValeurParCodeParamEtIdAgence(String codeParam, Integer idAgence) throws Exception {
        if(idAgence == null) {
    		// On retourne la valeur par défaut du paramètre
            return  StringEscapeUtils.unescapeJava(ParametreServiceImpl
                    .getInstance()
                    .findByPk(codeParam)
                    .getValeurParDefaut());
    	}
    	else {
	        ParametreAgence founded = findByPk(new ParametreAgencePK(idAgence, codeParam));
	        if (founded == null) {
	            // On retourne la valeur par défaut du paramètre
	            return StringEscapeUtils.unescapeJava(ParametreServiceImpl
                            .getInstance()
                            .findByPk(codeParam)
                            .getValeurParDefaut());
	        } else {
	            return StringEscapeUtils.unescapeJava(founded.getValeur());
	        }
    	}
    }

    @Override
    public float trouverTauxTVAParIdAgence(Integer idAgence) throws Exception {
        return Float
                .parseFloat(trouverValeurParCodeParamEtIdAgence(CODE_TAUX_TVA, idAgence));
    }

    @Override
    public float trouverTauxCSSParIdAgence(Integer idAgence) throws Exception {
        return Float
                .parseFloat(trouverValeurParCodeParamEtIdAgence(CODE_TAUX_CSS, idAgence));
    }

    @Override
    public String trouverMonnaieParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_MONNAIE, idAgence);
    }

    @Override
    public String trouverPatternDateParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_PATTERN_DATE, idAgence);
    }

    @Override
    public String trouverDefautMdpParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_DEFAUT_MOT_DE_PASSE, idAgence);
    }

    @Override
    public List<ParametreAgence> trouverParAgence(Agence agence) throws Exception {
        return (List<ParametreAgence>) getEm()
                .createQuery(trouverParAgenceStringQuery)
                .setParameter("p1", agence)
                .getResultList();
    }
    
    @Override
    public List<ParametreAgence> trouverParIdAgence(Integer idAgence) throws Exception {
        return (List<ParametreAgence>) getEm()
                .createQuery(trouverParIdAgenceStringQuery)
                .setParameter("p1", idAgence)
                .getResultList();
    }

    @Override
    public boolean trouverViewAllBrouilParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_VOIR_TOUT_SUR_LE_BROUILLARD, idAgence));
    }

    @Override
    public boolean trouverUpdateAllParamsParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_METTRE_A_JOUR_TOUS_LES_PARAMETRES, idAgence));
    }

    @Override
    public boolean trouverCreateAllUsersParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_CREER_TOUS_LES_UTILISATEURS, idAgence));
    }

    @Override
    public boolean trouverInitDataAtClickOnSBTNParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_INITIALISER_LES_DONNEES_AU_CLICK_SUR_LE_BOUTON_RECHERCHER, idAgence));
    }

    @Override
    public String trouverCodeEtatsParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_CODE_DES_ETATS_IMPRIMABLES, idAgence);
    }

    @Override
    public String trouverTimezoneIdParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_ID_TIMEZONE, idAgence);
    }

    @Override
    public String trouverLocaleParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_LOCALE, idAgence);
    }

    @Override
    public boolean trouverViewAllEnregParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_VOIR_TOUS_LES_ENREGISTREMENTS, idAgence));
    }

    @Override
    public boolean trouverViewAllFactParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_VOIR_TOUTES_LES_FACTURES, idAgence));
    }

    @Override
    public boolean trouverViewAllRecuParIdAgence(Integer idAgence) throws Exception {
        return Boolean
                .parseBoolean(trouverValeurParCodeParamEtIdAgence(CODE_VOIR_TOUS_LES_RECUS, idAgence));
    }

    @Override
    public String trouverAdresseParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_ADRESSE, idAgence);
    }

    @Override
    public String trouverNomSocieteParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_NOM_SOCIETE, idAgence);
    }

    @Override
    public String trouverDomaineParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_DOMAINE, idAgence);
    }

    @Override
    public String trouverAutresContactParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_AUTRES_CONTACT, idAgence);
    }

    @Override
    public String trouverNbEnregParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_NB_ENREG, idAgence);
    }

    @Override
    public String trouverArmoirieParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_ARMOIRIE, idAgence);
    }

    @Override
    public String trouverEtatParIdAgence(Integer idAgence) throws Exception {
        return trouverValeurParCodeParamEtIdAgence(CODE_ETAT, idAgence);
    }
    
    
    
    
}

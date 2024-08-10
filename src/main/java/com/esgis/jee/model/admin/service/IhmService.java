/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.Ihm;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface IhmService extends Service<Ihm, Integer> {

    public static String NOM_CHANGE_PASSWORD_IHM = "autresPasswordChangeView";
    
    /**
     * Trouve l'IHM de changement de mot de passe
     * @return 
     * @throws java.lang.Exception 
     */
    public Ihm trouverChangePasswordIhm() throws Exception;

    /**
     * Trouver une IHM à partir de son nom
     * @param nomIHM
     * @return 
     */
    public Ihm findByNom(String nomIHM);
    
}

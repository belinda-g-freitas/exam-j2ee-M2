/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import com.esgis.jee.model.admin.entity.Activite;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.infra.service.Service;
import jakarta.ejb.Local;

/**
 *
 * @author Gigi
 */
@Local
public interface ActiviteService extends Service<Activite, Long> {
    
    /**
     * Trouve les activités des utilisateurs mentionnés
     * @param utilisateurs
     * @return
     * @throws Exception 
     */
    public List<Activite> trouverParUtilisateurs(List<Utilisateur> utilisateurs) throws Exception;
    
    /**
     * Trouve les activités des utilisateurs mentionnés
     * @param agence
     * @return
     * @throws Exception 
     */
    public List<Activite> trouverParAgence(Agence agence) throws Exception;
}

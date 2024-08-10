/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.RoleUtilisateur;
import com.esgis.jee.model.admin.entity.RoleUtilisateurPK;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface RoleUtilisateurService extends Service<RoleUtilisateur, RoleUtilisateurPK> {
     
    /**
     * Trouve les RoleUtilisateur par utilisateur
     * @param utilisateur
     * @return les RoleUtilisateur correspondants
     * @throws java.lang.Exception 
     */
    public List<RoleUtilisateur> trouverParUtilisateur(Utilisateur utilisateur) throws Exception;
}

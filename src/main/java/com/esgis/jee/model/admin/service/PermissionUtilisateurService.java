/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.PermissionUtilisateur;
import com.esgis.jee.model.admin.entity.PermissionUtilisateurPK;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface PermissionUtilisateurService extends Service<PermissionUtilisateur, PermissionUtilisateurPK> {
    
    /**
     * Trouve les PermissionUtilisateur par utilisateur
     * @param utilisateur
     * @return 
     * @throws java.lang.Exception 
     */
    public List<PermissionUtilisateur> trouverParUtilisateur(Utilisateur utilisateur) throws Exception;
}

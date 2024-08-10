/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.entity.PermissionRole;
import com.esgis.jee.model.admin.entity.PermissionUtilisateur;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface UtilisateurService extends Service<Utilisateur, Integer> {
    
    /**
     * Trouve la liste des permissions (actives ou non actives) du rôle d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<PermissionRole> trouverPermissionsRoles(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des permissions actives du rôle d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<PermissionRole> trouverPermissionsRoleActives(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des permissions spécifiques (actives ou non actives) d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<PermissionUtilisateur> trouverPermissionsUtilisateur(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des permissions spécifiques actives d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<PermissionUtilisateur> trouverPermissionsUtilisateurActives(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste de toutes les permissions (actives ou non actives) d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<Permission> trouverPermissions(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste de toutes les permissions actives d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<Permission> trouverPermissionsActives(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des rôles (actifs ou non actifs) d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<Role> trouverRoles(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des rôles actifs d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<Role> trouverRolesActifs(Utilisateur entity) throws Exception;
    
    /**
     * Trouve la liste des noms de rôles actifs d'un utilisateur
     * @param entity
     * @return
     * @throws Exception 
     */
    public List<String> trouverNomRolesActifs(Utilisateur entity) throws Exception;
    
    /**
     * Fait une authentification partiel (avec l'identifiant uniquement)
     * @param id
     * @return
     */
    public Utilisateur partialAuthById(String id);
    
    /**
     * Trouve la liste des utilisateurs par agence
     * @param agence
     * @return
     * @throws Exception 
     */
    public List<Utilisateur> trouverParAgence(Agence agence) throws Exception;
    
    /**
     * Trouve la liste des noms utilisateurs par agence
     * @param agence
     * @return
     * @throws Exception 
     */
    public List<String> trouverNomsUtilParAgence(Agence agence) throws Exception;
    
    /**
     * Fait une authentification partielle (avec l'identifiant contenant le code agence)
     * @param id contenant l'identifiant et le code de l'agence suivant le pattern suivant : id@code_agence
     * @return
     */
    public Utilisateur partialAuthById2(String id);

    /**
     * 
     * @param entity
     * @param nomPermission
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean hasPermission(Utilisateur entity, String nomPermission) throws Exception;
    
    /**
     * Trouve le nom des permissions actives pour un utilisateur
     * @param utilisateur
     * @return 
     * @throws java.lang.Exception 
     */
    public List<String> trouverNomPermissionsActives(Utilisateur utilisateur) throws Exception;
}
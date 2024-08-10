/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import com.esgis.jee.model.admin.dao.UtilisateurDao;
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.entity.PermissionRole;
import com.esgis.jee.model.admin.entity.PermissionUtilisateur;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.entity.Utilisateur;
import static com.esgis.jee.model.admin.service.ParametreService.LONGUEUR_MAX_MDP;
import com.esgis.jee.model.admin.service.PermissionService;
import com.esgis.jee.model.admin.service.UtilisateurService;
import com.esgis.jee.model.infra.dao.Dao;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
import com.esgis.jee.model.infra.service.AdminServiceImpl;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

/**
 *
 * @author Gigi
 */
@Stateless(name = "AdminUtilisateurServiceImpl")
public class UtilisateurServiceImpl extends AdminServiceImpl<Utilisateur, Integer> implements UtilisateurService {

    @Inject
    private UtilisateurDao dao;
    
    private final String trouverParAgenceStringQuery
            = "SELECT x "
            + "FROM   Utilisateur x "
            + "WHERE  x.agence = :p1 ";

    /* Méthodes de classe */
    public static UtilisateurService getInstance() {
        return AdminServiceImpl.getInstance(UtilisateurService.class);
    }

    public static UtilisateurService getInstance(String app) {
        return AdminServiceImpl.getInstance(app, UtilisateurService.class);
    }

    /* Redéfinitions */
    @Override
    public Dao<Utilisateur, Integer> getDao() {
        return dao;
    }

    @Override
    public synchronized Utilisateur create(Utilisateur entity, boolean flush, boolean refresh) throws RuntimeException {
        if (CollectionUtils.isEmpty(entity.getRoleUtilisateurList())) { // S'il n'y a aucun rôle
            throw new ValidationException("Aucun rôle, veuillez attribuer des rôles à l'utilisateur");
        }
        return super.create(entity, flush, refresh);
    }

    @Override
    public List<PermissionRole> trouverPermissionsRoles(Utilisateur entity) throws RuntimeException {
        return ((List<PermissionRole>) getEm()
                .createQuery(
                        "SELECT  pr "
                        + "FROM  PermissionRole pr "
                        + "WHERE pr.role IN :p ")
                .setParameter("p", trouverRolesActifs(entity))
                .getResultList());
    }

    @Override
    public List<PermissionRole> trouverPermissionsRoleActives(Utilisateur entity) throws RuntimeException {
        return ((List<PermissionRole>) getEm()
                .createQuery(
                        "SELECT  pr "
                        + "FROM  PermissionRole pr "
                        + "WHERE pr.role IN :p AND pr.active = true ")
                .setParameter("p", trouverRolesActifs(entity))
                .getResultList());
    }

    @Override
    public List<PermissionUtilisateur> trouverPermissionsUtilisateur(Utilisateur entity) throws RuntimeException {
        return (List<PermissionUtilisateur>) getEm()
                .createQuery(
                        "SELECT pu "
                        + "FROM PermissionUtilisateur pu "
                        + "WHERE pu.utilisateur = :p ")
                .setParameter("p", entity)
                .getResultList();
    }

    @Override
    public List<PermissionUtilisateur> trouverPermissionsUtilisateurActives(Utilisateur entity) throws RuntimeException {
        return (List<PermissionUtilisateur>) getEm()
                .createQuery(
                        "SELECT pu "
                        + "FROM PermissionUtilisateur pu "
                        + "WHERE pu.utilisateur = :p AND pu.active = true")
                .setParameter("p", entity)
                .getResultList();
    }

    @Override
    public List<Permission> trouverPermissions(Utilisateur entity) throws RuntimeException {
        List<Permission> result = new ArrayList<>();
        trouverPermissionsRoles(entity)
                .stream()
                .forEach((t) -> {
                    result.add(t.getPermission());
                });
        trouverPermissionsUtilisateur(entity)
                .stream()
                .forEach((t) -> {
                    result.add(t.getPermission());
                });
        return result;
    }

    @Override
    public List<Permission> trouverPermissionsActives(Utilisateur entity) throws RuntimeException {
        List<Permission> result = new ArrayList<>();
        trouverPermissionsRoleActives(entity)
                .stream()
                .forEach((t) -> {
                    result.add(t.getPermission());
                });
        trouverPermissionsUtilisateurActives(entity)
                .stream()
                .forEach((t) -> {
                    result.add(t.getPermission());
                });
        return result;
    }

    @Override
    public List<Role> trouverRoles(Utilisateur entity) throws RuntimeException {
        return (List<Role>) getEm()
                .createQuery(
                        "SELECT ru.role "
                        + "FROM RoleUtilisateur ru "
                        + "WHERE ru.utilisateur = :p ")
                .setParameter("p", entity)
                .getResultList();
    }

    @Override
    public List<Role> trouverRolesActifs(Utilisateur entity) throws RuntimeException {
        return (List<Role>) getEm()
                .createQuery(
                        "SELECT ru.role "
                        + "FROM RoleUtilisateur ru "
                        + "WHERE ru.utilisateur = :p AND ru.actif = true ")
                .setParameter("p", entity)
                .getResultList();
    }
    
    @Override
    public List<String> trouverNomRolesActifs(Utilisateur entity) throws RuntimeException {
        return (List<String>) getEm()
                .createQuery(
                        "SELECT ru.role.nom "
                        + "FROM RoleUtilisateur ru "
                        + "WHERE ru.utilisateur = :p AND ru.actif = true ")
                .setParameter("p", entity)
                .getResultList();
    }

    @Override
    public Utilisateur partialAuthById(String id) {
        List<Utilisateur> list = getEm()
                .createQuery(
                        "SELECT  u "
                        + "FROM  Utilisateur u "
                        + "WHERE LOWER(u.identifiant) = LOWER(:p) ")
                .setParameter("p", id)
                .getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Utilisateur> trouverParAgence(Agence agence) throws RuntimeException {
        return (List<Utilisateur>) getEm()
                .createQuery(trouverParAgenceStringQuery)
                .setParameter("p1", agence)
                .getResultList();
    }

    @Override
    public List<String> trouverNomsUtilParAgence(Agence agence) throws RuntimeException {
        return (List<String>) getEm()
                .createQuery(
                        "SELECT u.nom "
                        + "FROM Utilisateur u "
                        + "WHERE u.agence = :p ")
                .setParameter("p", agence)
                .getResultList();
    }

    @Override
    public synchronized Utilisateur update(Utilisateur entity, boolean flush, boolean refresh) throws RuntimeException {
        if (entity.getLongueurMdp() > LONGUEUR_MAX_MDP) {
            throw new ValidationException(String.format("Mot de passe trop long, saisir moins de %d caractères", LONGUEUR_MAX_MDP));
        }
        return super.update(entity, flush, refresh);
    }

    @Override
    public Utilisateur partialAuthById2(String id) {
        if (!id.contains("@")) {
            return partialAuthById(id);
        } else {
            List<Utilisateur> list = getEm()
                    .createQuery(
                            "SELECT  u "
                            + "FROM  Utilisateur u "
                            + "WHERE LOWER(u.identifiant) = LOWER(:p1) "
                            + "AND   LOWER(u.agence.code) = LOWER(:p2) ")
                    .setParameter("p1", id.substring(0, id.lastIndexOf("@")))
                    .setParameter("p2", id.substring(id.lastIndexOf("@") + 1))
                    .getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
    }

    @Override
    public boolean hasPermission(Utilisateur entity, String nomPermission) {
        StoredProcedureQuery storedProcedure = getEm().createStoredProcedureQuery("has_permission");
    	// set parameters
    	/*storedProcedure.registerStoredProcedureParameter("id_User", String.class, ParameterMode.IN);
    	storedProcedure.registerStoredProcedureParameter("nom_Permission", String.class, ParameterMode.IN);
    	storedProcedure.setParameter("id_User", entity.getId().toString());
    	storedProcedure.setParameter("nom_Permission", nomPermission);*/
    	storedProcedure.registerStoredProcedureParameter(0, Integer.class, ParameterMode.IN);
    	storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
    	storedProcedure.setParameter(0, entity.getId());
    	storedProcedure.setParameter(1, nomPermission);
    	// execute SP
    	storedProcedure.execute();
    	// get result
    	return Boolean.parseBoolean(String.valueOf(((Object[])storedProcedure.getSingleResult())[0]));
    }
    
    @Override
    public List<String> trouverNomPermissionsActives(Utilisateur entity) throws RuntimeException {
        Permission doAnyThingPerm = AdminServiceImpl.getInstance(PermissionService.class).trouverDoAnythingPerm();
    	if(hasPermission(entity, doAnyThingPerm.getNom())) {
    		return AdminServiceImpl.getInstance(PermissionService.class).trouverNomPermissionsActives();
        } else {
            List<String> result = ((List<String>) getEm()
                    .createQuery(
                            "SELECT  e2.nom "
                                    //"SELECT  COUNT(e) "
                            + "FROM  PermissionRole e, RoleUtilisateur e1, Permission e2 "
                            + "WHERE e.id.idRole = e1.id.idRole "
                            + "AND   e.id.idPermission = e2.id "
                            + "AND   e1.id.idUtilisateur = :p1 "
                            + "AND   e2.id <> :p2 "
                            + "AND   e.active = true "
                            + "AND   e1.actif = true "
                            + "AND   e2.active = true ")
                    .setParameter("p1", entity.getId())
                    .setParameter("p2", doAnyThingPerm.getId())
                    .getResultList());
            // Ses Permissions utilisateurs
            result.addAll((List<String>) getEm()
                .createQuery(
                        "SELECT  e.permission.nom "
                            //"SELECT  COUNT(e) "
                        + "FROM  PermissionUtilisateur e "
                        + "WHERE e.id.idUtilisateur = :p1 "
                        + "AND   e.id.idPermission <> :p2 "
                        + "AND   e.active = true "
                        + "AND   e.permission.active = true")
                .setParameter("p1", entity.getId())
                .setParameter("p2", doAnyThingPerm.getId())
                .getResultList());
            return result;
        }
    }
}

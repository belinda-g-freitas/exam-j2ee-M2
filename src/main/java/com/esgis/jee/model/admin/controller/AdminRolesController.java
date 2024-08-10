/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.esgis.jee.model.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.entity.PermissionRole;
import com.esgis.jee.model.admin.entity.PermissionRolePK;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.service.IhmService;
import com.esgis.jee.model.admin.service.PermissionRoleService;
import com.esgis.jee.model.admin.service.PermissionService;
import com.esgis.jee.model.admin.service.RoleService;
import com.esgis.jee.model.admin.service.RoleUtilisateurService;
import com.esgis.jee.model.infra.lazy.AppLazyDataModelFactory;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModel;
import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import com.esgis.jee.model.infra.lazy.HasTempData;
import com.esgis.jee.model.infra.misc.MessagesFactory;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author EL Capitain
 */
@Getter
@Setter
@ViewScoped
@ViewController(view = "adminRolesView")
@Named("adminRolesBean")
public class AdminRolesController extends ViewControllerImpl {

    /* Services */
    @EJB
    @Setter(AccessLevel.NONE)
    private RoleService service;
    @EJB
    @Setter(AccessLevel.NONE)
    private RoleUtilisateurService roleUtilisateurService;
    @EJB
    @Setter(AccessLevel.NONE)
    private PermissionService permissionService;
    @EJB
    @Setter(AccessLevel.NONE)
    private PermissionRoleService permissionRoleService;
    @EJB
    @Setter(AccessLevel.NONE)
    private IhmService ihmService;

    /* Objets simples */
    private Role formObject, selectedObject;
    private PermissionRole editedObject;
    private GenericLazyDataModel<Role, Integer> dataModel;
    private GenericLazyDataModel<Permission, Integer> permissionDataModel;
    private GenericLazyDataModel<PermissionRole, PermissionRolePK> permissionRoleDataModel;

    /* Listes */
    private List<Permission> selectedPermissionList;
    private List<PermissionRole> permissionRoleFilteredList;

    /* Tableaux */
    private final String[] filterableExps = new String[] {
            "code",
            "nom",
            "description" },
            permissionFilterableExps = new String[] {
                    "nom",
                    "ihm.nom",
                    "ihm.description",
                    "operation.label",
                    "active" },
            permissionRoleFilterableExps = new String[] {
                    "permission.nom",
                    "permission.ihm.nom",
                    "permission.ihm.description",
                    "permission.operation.label",
                    "active" };

    /* Types primitifs */

    /* Beans injectés */
    public AdminRolesController() throws Exception {
        super(AdminRolesController.class);
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            permissionDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
                    new ArrayList<>(),
                    Permission.class,
                    Integer.class,
                    permissionFilterableExps);
            dataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
                    new ArrayList<>(),
                    Role.class,
                    Integer.class,
                    filterableExps);
        } catch (Exception ex) {
            messagesBean.addMessageError(
                    String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminParametresController.class.getName()).log(Level.SEVERE, null, ex);
        }
        effacer();
    }

    public void chargerRoles(boolean isSearchBtn) {
        try {
            if ((isSearchBtn && parametreAgenceService
                    .trouverInitDataAtClickOnSBTNParIdAgence(sessionBean.getFormObject().getAgence().getId()))
                    || (!isSearchBtn)) {
                dataModel = AppLazyDataModelFactory.createLazyDBDataModel(
                        service,
                        Role.class,
                        Integer.class,
                        filterableExps);
            }
        } catch (Exception ex) {
            messagesBean.addMessageError(
                    String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chargerPermissions(boolean isSearchBtn) {
        try {
            if ((isSearchBtn && parametreAgenceService
                    .trouverInitDataAtClickOnSBTNParIdAgence(sessionBean.getFormObject().getAgence().getId()))
                    || (!isSearchBtn)) {
                permissionDataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
                        permissionService,
                        PermissionService.class,
                        Permission.class,
                        Integer.class,
                        permissionFilterableExps,
                        "trouverPermissionsActives");
                ((HasTempData<Permission>) permissionDataModel).addTempDeletedData(formObject
                        .getPermissionRoleList()
                        .stream()
                        .map(PermissionRole::getPermission)
                        .collect(Collectors.toList())); // On retire les permissions déjà présentes
            }
        } catch (Exception ex) {
            messagesBean.addMessageError(
                    String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enregistrer() {
        if (selectedObject == null) {
            try {
                // Insertion
                formObject = service.create(formObject, true, true);
                if (GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
                    // Collection based -> on ajoute a la source de donnees
                    ((GenericLazyCollectionDataModel<Role, ?>) dataModel).addData(formObject);
                }
                messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_RECORD);
                log(String.format("Insertion : %s", formObject));
                selectedObject = formObject; // On sélectionne le rôle enregistré pour une éventuelle modification
                onSelection(); // On exécute les actions à effectuer lors de la sélection
            } catch (ValidationException ex) {
                messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
            } catch (RuntimeException ex) {
                messagesBean.addMessageError(
                        String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_RECORD, ex.getMessage()));
                Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // Mise à jour
                formObject = selectedObject = service.update(formObject, true, true);
                if (GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
                    // Collection based -> on ajoute a la source de donnees
                    ((GenericLazyCollectionDataModel<Role, ?>) dataModel).updateData(formObject);
                }
                messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_MERGE);
                log(String.format("Modification : %s", formObject));
                onSelection(); // On exécute les actions à effectuer lors de la sélection
            } catch (ValidationException ex) {
                messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
            } catch (RuntimeException ex) {
                messagesBean.addMessageError(
                        String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_MERGE, ex.getMessage()));
                Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void effacer() {
        formObject = new Role();
        selectedObject = null;
        try {
            permissionRoleDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
                    formObject.getPermissionRoleList(),
                    PermissionRole.class,
                    PermissionRolePK.class,
                    permissionRoleFilterableExps);
        } catch (Exception ex) {
            messagesBean.addMessageError(
                    String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ajouterPermissionRole() {
        List<PermissionRole> createList = PermissionRole.createList(formObject, selectedPermissionList);
        createList.forEach((t) -> {
            doCreatePermissionRole(t);
        });
        selectedPermissionList = null;
    }

    public void retirerPermissionRole(PermissionRole object) {
        // formObject.getPermissionUtilisateurList().remove(index);
        if (object.isNew()) {
            doDeleteRoleUtilisateur(object);
        } else {
            object.setActive(!object.isActive());
            doUpdatePermissionRole(object);
        }
    }

    @SuppressWarnings("unchecked")
    public void doDeleteRoleUtilisateur(PermissionRole editedObject) {
        if (selectedObject == null) {
            // Collection based model
            ((GenericLazyCollectionDataModel<PermissionRole, ?>) permissionRoleDataModel).deleteData(editedObject);
        } else {
            // Service based model
            ((HasTempData<PermissionRole>) permissionRoleDataModel).addTempDeletedData(editedObject);
            formObject.getPermissionRoleList().remove(editedObject);
        }
    }

    public void doUpdatePermissionRole(PermissionRole editedObject) {
        if (selectedObject != null) {
            // Service based model
            ((HasTempData<PermissionRole>) permissionRoleDataModel).addTempUpdatedData(editedObject);
            formObject.getPermissionRoleList().set(formObject.getPermissionRoleList().indexOf(editedObject),
                    editedObject);
        }
    }

    public void doCreatePermissionRole(PermissionRole editedObject) {
        if (selectedObject == null) {
            // Collection based Model
            ((GenericLazyCollectionDataModel<PermissionRole, ?>) permissionRoleDataModel).addData(editedObject);
        } else {
            // Service based model
            ((HasTempData<PermissionRole>) permissionRoleDataModel).addTempCreatedData(editedObject);
            formObject.getPermissionRoleList().add(editedObject);
        }
    }

    /**
     * Action à exécuter lors de la sélection d'une facture
     */
    public void onSelection() {
        formObject = selectedObject;
        try {
            permissionRoleDataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
                    permissionRoleService,
                    PermissionRoleService.class,
                    PermissionRole.class,
                    PermissionRolePK.class,
                    permissionRoleFilterableExps,
                    "trouverParRole",
                    formObject);
        } catch (Exception ex) {
            messagesBean.addMessageError(
                    String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
            Logger.getLogger(AdminRolesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onCellEdit(CellEditEvent event) {
        super.onCellEdit(event);
        PermissionRole updatedObject = permissionRoleDataModel.getDataFromDisplayedData(event.getRowIndex());
        doUpdatePermissionRole(updatedObject);
    }

    /**
     * Test de rendu sur les composants si permission y est
     *
     * @return
     */
    public boolean renderComponent() {
        return (selectedObject == null && haveCreatePermission()) || (selectedObject != null && haveUpdatePermission());
    }
}

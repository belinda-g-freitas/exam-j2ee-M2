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
import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.admin.entity.Ihm;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.entity.PermissionUtilisateur;
import com.esgis.jee.model.admin.entity.PermissionUtilisateurPK;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.admin.entity.RoleUtilisateur;
import com.esgis.jee.model.admin.entity.RoleUtilisateurPK;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.admin.service.AgenceService;
import com.esgis.jee.model.admin.service.IhmService;
import com.esgis.jee.model.admin.service.PermissionService;
import com.esgis.jee.model.admin.service.PermissionUtilisateurService;
import com.esgis.jee.model.admin.service.RoleService;
import com.esgis.jee.model.admin.service.RoleUtilisateurService;
import com.esgis.jee.model.admin.service.UtilisateurService;
import com.esgis.jee.model.infra.controller.ViewController;
import com.esgis.jee.model.infra.controller.ViewControllerImpl;
import com.esgis.jee.model.infra.dao.exception.ValidationException;
import com.esgis.jee.model.infra.lazy.AppLazyDataModelFactory;
import com.esgis.jee.model.infra.lazy.GenericLazyCollectionDataModel;
import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import com.esgis.jee.model.infra.lazy.HasTempData;
import com.esgis.jee.model.infra.lazy.component.AppLazyComponentFactory;
import com.esgis.jee.model.infra.lazy.component.LazySelectOneMenuComponentManager;
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
@ViewController(view = "adminUtilisateursView")
@Named("adminUtilisateursBean")
public class AdminUtilisateursController extends ViewControllerImpl {

	/* Services */
	@EJB
	@Setter(AccessLevel.NONE)
	private AgenceService agenceService;
	@EJB
	@Setter(AccessLevel.NONE)
	private UtilisateurService service;
	@EJB
	@Setter(AccessLevel.NONE)
	private RoleService roleService;
	@EJB
	@Setter(AccessLevel.NONE)
	private RoleUtilisateurService roleUtilisateurService;
	@EJB
	@Setter(AccessLevel.NONE)
	private PermissionService permissionService;
	@EJB
	@Setter(AccessLevel.NONE)
	private PermissionUtilisateurService permissionUtilisateurService;
	@EJB
	@Setter(AccessLevel.NONE)
	private IhmService ihmService;

	/* Objets simples */
	private Utilisateur formObject, selectedObject;
	private PermissionUtilisateur editedObject;
	private Ihm changePasswordIhm;
	private Agence agenceParDefaut;
	private GenericLazyDataModel<Utilisateur, Integer> dataModel;
	private GenericLazyDataModel<Permission, Integer> permissionDataModel;
	private GenericLazyDataModel<PermissionUtilisateur, PermissionUtilisateurPK> permissionUtilisateurDataModel;
	private GenericLazyDataModel<RoleUtilisateur, RoleUtilisateurPK> roleUtilisateurDataModel;
	private LazySelectOneMenuComponentManager<Agence, Integer> agenceSOMComponentManager;

	/* Listes */
	private List<Role> roleList;
	private List<Permission> selectedPermissionList;

	/* Tableaux */
	private final String[] filterableExps = new String[] {
			"identifiant",
			"agence.nom",
			"longueurMdp",
			"nom",
			"dateInscription",
			"actif" },
			permissionFilterableExps = new String[] {
					"nom",
					"ihm.nom",
					"ihm.description",
					"operation.label",
					"active" },
			permissionUtilisateurFilterableExps = new String[] {
					"permission.nom",
					"permission.ihm.nom",
					"permission.ihm.description",
					"permission.operation.label",
					"active" },
			roleUtilisateurFilterableExps = new String[] {
					"role.nom",
					"role.description",
					"actif" },
			agenceFilterableExps = new String[] {
					"code",
					"nom" };

	/* Types primitifs */
	private String defautMdp;
	private boolean createAllUsers;

	/* Beans injectés */
	public AdminUtilisateursController() throws Exception {
		super(AdminUtilisateursController.class);
	}

	@Override
	@PostConstruct
	public void init() {
		try {
			this.roleList = roleService.read();
			this.agenceParDefaut = sessionBean.getFormObject().getAgence();
			this.defautMdp = parametreAgenceService
					.trouverDefautMdpParIdAgence(sessionBean.getFormObject().getAgence().getId());
			this.createAllUsers = parametreAgenceService
					.trouverCreateAllUsersParIdAgence(sessionBean.getFormObject().getAgence().getId());
			this.changePasswordIhm = ihmService.trouverChangePasswordIhm();
			permissionDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
					new ArrayList<>(),
					Permission.class,
					Integer.class,
					permissionFilterableExps);
			dataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
					new ArrayList<>(),
					Utilisateur.class,
					Integer.class,
					filterableExps);
		} catch (Exception ex) {
			messagesBean.addMessageError(
					String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
			Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
		}
		effacer();
	}

	public void chargerUtilisateurs(boolean isSearchBtn) {
		try {
			if ((isSearchBtn && parametreAgenceService
					.trouverInitDataAtClickOnSBTNParIdAgence(sessionBean.getFormObject().getAgence().getId()))
					|| (!isSearchBtn)) {
				if (createAllUsers) {
					dataModel = AppLazyDataModelFactory.createLazyServiceDataModel(
							service,
							UtilisateurService.class,
							Utilisateur.class,
							Integer.class,
							filterableExps,
							"read");
				} else {
					dataModel = AppLazyDataModelFactory.createLazyServiceDataModel(
							service,
							UtilisateurService.class,
							Utilisateur.class,
							Integer.class,
							filterableExps,
							"trouverParAgence",
							sessionBean.getFormObject().getAgence());
				}
			}
		} catch (Exception ex) {
			messagesBean.addMessageError(
					String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
			Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
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
						.getPermissionUtilisateurList()
						.stream()
						.map(PermissionUtilisateur::getPermission)
						.collect(Collectors.toList())); // On retire les permissions déjà présentes
			}
		} catch (Exception ex) {
			messagesBean.addMessageError(
					String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
			Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void enregistrer() {
		if (selectedObject == null) {
			try {
				// Insertion
				formObject = service.create(formObject, true, true);
				if (GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
					// Collection based -> on ajoute a la source de donnees
					((GenericLazyCollectionDataModel<Utilisateur, ?>) dataModel).addData(formObject);
				}
				messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_RECORD);
				log(String.format("Insertion : %s", formObject));
				selectedObject = formObject; // On sélectionne l'utilisateur enregistré pour une éventuelle modification
				onSelection(); // On exécute les actions à effectuer lors de la sélection
			} catch (ValidationException ex) {
				log(ex.toString());
				messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
			} catch (RuntimeException ex) {
				messagesBean.addMessageError(
						String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_RECORD, ex.getMessage()));
				Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			try {
				// Mise à jour
				formObject = selectedObject = service.update(formObject, true, true);
				// dataModel.doUpdate(formObject);
				if (GenericLazyCollectionDataModel.class.isAssignableFrom(dataModel.getClass())) {
					// Collection based -> on ajoute a la source de donnees
					((GenericLazyCollectionDataModel<Utilisateur, ?>) dataModel).updateData(formObject);
				}
				messagesBean.addMessageInfo(MessagesFactory.MSG_SUCCESS_DB_MERGE);
				log(String.format("Modification : %s", formObject));
				onSelection(); // On exécute les actions à effectuer lors de la sélection
			} catch (ValidationException ex) {
				messagesBean.addMessageError(String.format("Erreur de validation ; Raison : %s", ex.getMessage()));
			} catch (RuntimeException ex) {
				messagesBean.addMessageError(
						String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_MERGE, ex.getMessage()));
				Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void effacer() {
		formObject = new Utilisateur(defautMdp, changePasswordIhm, agenceParDefaut);
		selectedObject = null;
		try {
			permissionUtilisateurDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
					formObject.getPermissionUtilisateurList(),
					PermissionUtilisateur.class,
					PermissionUtilisateurPK.class,
					permissionUtilisateurFilterableExps);
			roleUtilisateurDataModel = AppLazyDataModelFactory.createLazyCollectionDataModel(
					formObject.getRoleUtilisateurList(),
					RoleUtilisateur.class,
					RoleUtilisateurPK.class,
					roleUtilisateurFilterableExps);
			// Définition des gestionnaires de menus déroulants
			agenceSOMComponentManager = AppLazyComponentFactory.createLazyServiceSelectOneMenuComponentManager(
					agenceParDefaut,
					this,
					"formObject.agence",
					"Groupes utilisateurs",
					agenceService,
					AgenceService.class,
					Agence.class,
					Integer.class,
					agenceFilterableExps, // Les colonnes pour filtre
					"read");
		} catch (Exception ex) {
			messagesBean.addMessageError(
					String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
			Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void ajouterRoleUtilisateur() {
		doCreateRoleUtilisateur(new RoleUtilisateur(true, formObject));
	}

	public void retirerRoleUtilisateur(RoleUtilisateur object) {
		// formObject.getRoleUtilisateurList().remove(index);
		if (object.isNew()) {
			doDeleteRoleUtilisateur(object);
		} else {
			object.setActif(!object.isActif());
			doUpdateRoleUtilisateur(object);
		}
	}

	public void ajouterPermissionUtilisateur() {
		List<PermissionUtilisateur> createList = PermissionUtilisateur.createList(formObject, selectedPermissionList);
		createList.forEach((t) -> {
			doCreatePermissionUtilisateur(t);
		});
		selectedPermissionList = null;
	}

	public void retirerPermissionUtilisateur(PermissionUtilisateur object) {
		// formObject.getPermissionUtilisateurList().remove(index);
		if (object.isNew()) {
			doDeletePermissionUtilisateur(object);
		} else {
			object.setActive(!object.isActive());
			doUpdatePermissionUtilisateur(object/* , formObject.getPermissionUtilisateurList().indexOf(object) */);
		}
	}

	@SuppressWarnings("unchecked")
	public void doDeleteRoleUtilisateur(RoleUtilisateur editedObject) {
		if (selectedObject == null) {
			// Collection based model
			((GenericLazyCollectionDataModel<RoleUtilisateur, ?>) roleUtilisateurDataModel).deleteData(editedObject);
		} else {
			// Service based model
			((HasTempData<RoleUtilisateur>) roleUtilisateurDataModel).addTempDeletedData(editedObject);
			formObject.getRoleUtilisateurList().remove(editedObject);
		}
	}

	@SuppressWarnings("unchecked")
	public void doUpdateRoleUtilisateur(RoleUtilisateur editedObject) {
		if (selectedObject != null) {
			// Service based model
			((HasTempData<RoleUtilisateur>) roleUtilisateurDataModel).addTempUpdatedData(editedObject);
			formObject.getRoleUtilisateurList().set(formObject.getRoleUtilisateurList().indexOf(editedObject),
					editedObject);
		}
	}

	public void doCreateRoleUtilisateur(RoleUtilisateur editedObject) {
		if (selectedObject == null) {
			// Collection based Model
			((GenericLazyCollectionDataModel<RoleUtilisateur, ?>) roleUtilisateurDataModel).addData(editedObject);
		} else {
			// Service based model
			((HasTempData<RoleUtilisateur>) roleUtilisateurDataModel).addTempCreatedData(editedObject);
			formObject.getRoleUtilisateurList().add(editedObject);
		}
	}

	@SuppressWarnings("unchecked")
	public void doDeletePermissionUtilisateur(PermissionUtilisateur editedObject) {
		if (selectedObject == null) {
			// Collection based model
			((GenericLazyCollectionDataModel<PermissionUtilisateur, ?>) permissionUtilisateurDataModel)
					.deleteData(editedObject);
		} else {
			// Service based model
			((HasTempData<PermissionUtilisateur>) permissionUtilisateurDataModel).addTempDeletedData(editedObject);
			formObject.getPermissionUtilisateurList().remove(editedObject);
		}
	}

	@SuppressWarnings("unchecked")
	public void doUpdatePermissionUtilisateur(PermissionUtilisateur editedObject/* , int rowIndex */) {
		if (selectedObject != null) {
			// Service based model
			((HasTempData<PermissionUtilisateur>) permissionUtilisateurDataModel).addTempUpdatedData(editedObject);
			formObject.getPermissionUtilisateurList()
					.set(formObject.getPermissionUtilisateurList().indexOf(editedObject), editedObject);
		}
	}

	@SuppressWarnings("unchecked")
	public void doCreatePermissionUtilisateur(PermissionUtilisateur editedObject) {
		if (selectedObject == null) {
			// Collection based Model
			((GenericLazyCollectionDataModel<PermissionUtilisateur, ?>) permissionUtilisateurDataModel)
					.addData(editedObject);
		} else {
			// Service based model
			((HasTempData<PermissionUtilisateur>) permissionUtilisateurDataModel).addTempCreatedData(editedObject);
			formObject.getPermissionUtilisateurList().add(editedObject);
		}
	}

	/**
	 * Action à exécuter lors du déroulement du menu déroulant de sélection
	 * d'une agence
	 *
	 * @param destFieldName
	 * @param dialogTitle
	 */
	public void onAgenceSelectOneMenuFocus(String destFieldName, String dialogTitle) {
		agenceSOMComponentManager.onFocus(this, destFieldName, dialogTitle);
	}

	/**
	 * Action à exécuter lors de la sélection d'une agence
	 *
	 * @param withValue indique si la sélection est à valeur concrète ou est
	 *                  nulle
	 */
	public void onAgenceSelectOneMenuSelect(boolean withValue) {
		agenceSOMComponentManager.onSelect(withValue);
	}

	/**
	 * Action à exécuter lors de la sélection d'une facture
	 */
	public void onSelection() {
		formObject = selectedObject;
		agenceSOMComponentManager.addSimulatorObject(formObject.getAgence());
		try {
			permissionUtilisateurDataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
					permissionUtilisateurService,
					PermissionUtilisateurService.class,
					PermissionUtilisateur.class,
					PermissionUtilisateurPK.class,
					permissionUtilisateurFilterableExps,
					"trouverParUtilisateur",
					formObject);
			roleUtilisateurDataModel = AppLazyDataModelFactory.createLazyServiceDataModelWithTempData(
					roleUtilisateurService,
					RoleUtilisateurService.class,
					RoleUtilisateur.class,
					RoleUtilisateurPK.class,
					roleUtilisateurFilterableExps,
					"trouverParUtilisateur",
					formObject);
		} catch (Exception ex) {
			messagesBean.addMessageError(
					String.format("%s ; Raison : %s", MessagesFactory.MSG_ERROR_DB_LOAD, ex.getMessage()));
			Logger.getLogger(AdminUtilisateursController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void onCellEdit(CellEditEvent event) {
		super.onCellEdit(event);
		RoleUtilisateur updatedObject = roleUtilisateurDataModel.getDataFromDisplayedData(event.getRowIndex());
		doUpdateRoleUtilisateur(updatedObject);
	}

	/**
	 * Test de rendu sur les composants si permission y est
	 *
	 * @return
	 */
	public boolean renderComponent() {
		return (selectedObject == null && haveCreatePermission()) || (selectedObject != null && haveUpdatePermission());
	}

	public void resetPassword() {
		formObject.setMotDePasse(defautMdp);
		formObject.setDernierIHM(changePasswordIhm);
		messagesBean.addMessageWarn("Mot de passe réinitialisé, enregistrez pour confirmer");
	}
}

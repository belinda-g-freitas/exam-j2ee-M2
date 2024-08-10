package com.esgis.jee.model.admin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * *********************************************************************
 * Module: Utilisateur.java Author: EL Capitain Purpose: Defines the Class
 * Utilisateur
 * *********************************************************************
 */
@Entity
@Table(name = "Utilisateur", uniqueConstraints = {
		@UniqueConstraint(name = "UQ_IDENTIFIANT_UTIL", columnNames = { "identifiant", "id_Agence" }),
		@UniqueConstraint(name = "UQ_NOM_UTIL", columnNames = { "nom" }) })
public class Utilisateur implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "identifiant", length = 30, nullable = false)
	private String identifiant;
	@Column(name = "mot_de_passe", nullable = false) // Mot de passe crypté
	private String motDePasse;
	@Column(name = "longueur_mdp", nullable = false, insertable = false, updatable = false) // Longueur réelle (avant
	// cryptage) : Généré par la
	// BD
	private int longueurMdp;
	@Column(name = "nom", length = 100, nullable = false)
	private String nom;
	@Column(name = "date_inscription", nullable = false)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	private Date dateInscription;
	@Column(name = "actif", nullable = false)
	private boolean actif;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_dernier_ihm", referencedColumnName = "id")
	private Ihm dernierIHM;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_agence", referencedColumnName = "id")
	private Agence agence;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, mappedBy = "utilisateur", fetch = FetchType.EAGER)
	private List<PermissionUtilisateur> permissionUtilisateurList;
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, mappedBy = "utilisateur", fetch = FetchType.EAGER)
	private List<RoleUtilisateur> roleUtilisateurList;

	public Utilisateur() {
		this.dateInscription = new Date();
		this.permissionUtilisateurList = new ArrayList<>();
		this.roleUtilisateurList = new ArrayList<>();
		this.actif = true;
	}

	public Utilisateur(String motDePasse, Ihm dernierIHM) {
		this();
		this.motDePasse = motDePasse;
		this.dernierIHM = dernierIHM;
		this.longueurMdp = motDePasse.length();
	}

	public Utilisateur(String motDePasse, Ihm dernierIHM, Agence agence) {
		this(motDePasse, dernierIHM);
		this.agence = agence;
	}

	public Utilisateur(String identifiant, String motDePasse, String nom, Date dateInscription, boolean actif,
			Ihm dernierIHM) {
		this(motDePasse, dernierIHM);
		this.identifiant = identifiant;
		this.nom = nom;
		this.dateInscription = dateInscription;
		this.actif = actif;
	}

	public Utilisateur(String identifiant, String motDePasse, String nom, Date dateInscription, boolean actif,
			List<RoleUtilisateur> roleUtilisateurList) {
		this();
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.nom = nom;
		this.dateInscription = dateInscription;
		this.actif = actif;
		this.roleUtilisateurList = roleUtilisateurList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	/**
	 * @return the longueurMdp
	 */
	public int getLongueurMdp() {
		return longueurMdp;
	}

	/**
	 * @param longueurMdp the longueurMdp to set
	 */
	public void setLongueurMdp(int longueurMdp) {
		this.longueurMdp = longueurMdp;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	/**
	 * @return the permissionUtilisateurList
	 */
	public List<PermissionUtilisateur> getPermissionUtilisateurList() {
		return permissionUtilisateurList;
	}

	/**
	 * @param permissionUtilisateurList the permissionUtilisateurList to set
	 */
	public void setPermissionUtilisateurList(List<PermissionUtilisateur> permissionUtilisateurList) {
		this.permissionUtilisateurList = permissionUtilisateurList;
	}

	/**
	 * @return the roleUtilisateurList
	 */
	public List<RoleUtilisateur> getRoleUtilisateurList() {
		return roleUtilisateurList;
	}

	/**
	 * @param roleUtilisateurList the roleUtilisateurList to set
	 */
	public void setRoleUtilisateurList(List<RoleUtilisateur> roleUtilisateurList) {
		this.roleUtilisateurList = roleUtilisateurList;
	}

	/**
	 * @return the dernierIHM
	 */
	public Ihm getDernierIHM() {
		return dernierIHM;
	}

	/**
	 * @param dernierIHM the dernierIHM to set
	 */
	public void setDernierIHM(Ihm dernierIHM) {
		this.dernierIHM = dernierIHM;
	}

	public Agence getAgence() {
		return agence;
	}

	public void setAgence(Agence agence) {
		this.agence = agence;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Utilisateur other = (Utilisateur) obj;
		return Objects.equals(this.id, other.id);
	}

	@Override
	public String toString() {
		return "Utilisateur{" + "id=" + id + ", identifiant=" + identifiant + ", motDePasse=" + motDePasse + ", nom="
				+ nom + ", dateInscription=" + dateInscription + ", actif=" + actif + ", dernierIHM=" + dernierIHM
				+ '}';
	}

}

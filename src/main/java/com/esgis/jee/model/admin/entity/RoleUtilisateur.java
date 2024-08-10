/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author EL Capitain
 */
@Entity
@Table(name = "role_utilisateur", uniqueConstraints = {})
public class RoleUtilisateur implements Serializable {

    @EmbeddedId
    private RoleUtilisateurPK id;
    @Column(name = "actif", nullable = false)
    private boolean actif;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Cause : navigabilité
    @JoinColumn(name = "id_utilisateur", referencedColumnName = "id")
    private Utilisateur utilisateur;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

    /* Constructeurs */
    public RoleUtilisateur() {
        this.id = new RoleUtilisateurPK();
        this.actif = true;
    }

    public RoleUtilisateur(boolean actif) {
        this();
        this.actif = actif;
    }

    public RoleUtilisateur(boolean actif, Utilisateur utilisateur) {
        this(new RoleUtilisateurPK(utilisateur.getId(), null), actif);
        this.utilisateur = utilisateur;
    }

    public RoleUtilisateur(RoleUtilisateurPK id, boolean actif) {
        this();
        this.id = id;
        this.actif = actif;
    }

    public RoleUtilisateur(Integer idUtilisateur, Integer idRole, boolean actif) {
        this(new RoleUtilisateurPK(idUtilisateur, idRole), actif);
    }

    /* Méthodes d'instance */
    public boolean isNew() {
        return id == null;
    }

    /* Getters & setters */
    public RoleUtilisateurPK getId() {
        return id;
    }

    public void setId(RoleUtilisateurPK id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * @return the utilisateur
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * @param utilisateur the utilisateur to set
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.getId().setIdUtilisateur(utilisateur.getId());
    }

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
        this.getId().setIdRole(this.role.getId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final RoleUtilisateur other = (RoleUtilisateur) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "RoleUtilisateur{" + "id=" + id + ", actif=" + actif + ", utilisateur=" + utilisateur + ", role=" + role
                + '}';
    }
}

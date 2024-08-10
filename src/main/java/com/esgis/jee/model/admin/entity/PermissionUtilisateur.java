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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author EL Capitain
 */
@Entity
@Table(
        name = "Permission_Utilisateur",
        uniqueConstraints = {}
)
public class PermissionUtilisateur implements Serializable {
    
    @EmbeddedId
    private PermissionUtilisateurPK id;
    @Column(name = "active", nullable = false)
    private boolean active;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Cause : navigabilité
    @JoinColumn(name = "id_Utilisateur", referencedColumnName = "id")
    private Utilisateur utilisateur;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_Permission", referencedColumnName = "id", insertable = false, updatable = false)
    private Permission permission;
    
    public PermissionUtilisateur() {
        this.active = true;
    }

    public PermissionUtilisateur(boolean active) {
        this.active = active;
    }

    public PermissionUtilisateur(boolean actif, Permission permission, Utilisateur utilisateur) {
        this(new PermissionUtilisateurPK(utilisateur.getId(), permission.getId()), actif);
        this.utilisateur = utilisateur;
        this.permission = permission;
    }
    
    public PermissionUtilisateur(PermissionUtilisateurPK id, boolean active) {
        this.id = id;
        this.active = active;
    }
    
    public PermissionUtilisateur(Integer idUtilisateur, Integer idPermission, boolean active) {
        this(new PermissionUtilisateurPK(idUtilisateur, idPermission), active);
    }
    
    public static List<PermissionUtilisateur> createList(Utilisateur u, List<Permission> list) {
        List<PermissionUtilisateur> result = new ArrayList<>();
        list.forEach((p) -> {
            result.add(new PermissionUtilisateur(true, p, u));
        });
        return result;
    }
    
    public boolean isNew() {
        return id == null;
    }
    
    public PermissionUtilisateurPK getId() {
        return id;
    }

    public void setId(PermissionUtilisateurPK id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        this.getId().setIdUtilisateur(this.utilisateur.getId());
    }

    /**
     * @return the permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @param permission the permission to set
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
        this.getId().setIdPermission(this.permission.getId());
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
        final PermissionUtilisateur other = (PermissionUtilisateur) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "PermissionUtilisateur{" + "id=" + id + ", active=" + active + ", utilisateur=" + utilisateur + ", permission=" + permission + '}';
    }
}

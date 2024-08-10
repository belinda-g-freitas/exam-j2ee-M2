/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author EL Capitain
 */
@Embeddable
public class RoleUtilisateurPK implements Serializable {
    
    @Column(name = "id_utilisateur", insertable = false, updatable = false)
    private Integer idUtilisateur;
    @Column(name = "id_role")
    private Integer idRole;

    public RoleUtilisateurPK() {
    }

    public RoleUtilisateurPK(Integer idUtilisateur, Integer idRole) {
        this.idUtilisateur = idUtilisateur;
        this.idRole = idRole;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idUtilisateur);
        hash = 89 * hash + Objects.hashCode(this.idRole);
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
        final RoleUtilisateurPK other = (RoleUtilisateurPK) obj;
        if (!Objects.equals(this.idUtilisateur, other.idUtilisateur)) {
            return false;
        }
        return Objects.equals(this.idRole, other.idRole);
    }

    @Override
    public String toString() {
        return "PermissionUtilisateurPK{" + "idUtilisateur=" + idUtilisateur + ", idPermission=" + idRole + '}';
    }
}

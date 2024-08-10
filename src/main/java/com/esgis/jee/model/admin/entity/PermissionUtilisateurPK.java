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
public class PermissionUtilisateurPK implements Serializable {
    
    @Column(name = "id_Utilisateur", insertable = false, updatable = false)
    private Integer idUtilisateur;
    @Column(name = "id_Permission")
    private Integer idPermission;

    public PermissionUtilisateurPK() {
    }

    public PermissionUtilisateurPK(Integer idUtilisateur, Integer idPermission) {
        this.idUtilisateur = idUtilisateur;
        this.idPermission = idPermission;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(Integer idPermission) {
        this.idPermission = idPermission;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idUtilisateur);
        hash = 89 * hash + Objects.hashCode(this.idPermission);
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
        final PermissionUtilisateurPK other = (PermissionUtilisateurPK) obj;
        if (!Objects.equals(this.idUtilisateur, other.idUtilisateur)) {
            return false;
        }
        return Objects.equals(this.idPermission, other.idPermission);
    }

    @Override
    public String toString() {
        return "PermissionUtilisateurPK{" + "idUtilisateur=" + idUtilisateur + ", idPermission=" + idPermission + '}';
    }
}

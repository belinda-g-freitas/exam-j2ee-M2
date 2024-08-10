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
public class PermissionRolePK implements Serializable {
    
    @Column(name = "id_Role")
    private Integer idRole;
    @Column(name = "id_Permission")
    private Integer idPermission;

    public PermissionRolePK() {
    }

    public PermissionRolePK(Integer idRole, Integer idPermission) {
        this.idRole = idRole;
        this.idPermission = idPermission;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public Integer getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(Integer idPermission) {
        this.idPermission = idPermission;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.idRole);
        hash = 43 * hash + Objects.hashCode(this.idPermission);
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
        final PermissionRolePK other = (PermissionRolePK) obj;
        if (!Objects.equals(this.idRole, other.idRole)) {
            return false;
        }
        return Objects.equals(this.idPermission, other.idPermission);
    }

    @Override
    public String toString() {
        return "PermissionRolePK{" + "idRole=" + idRole + ", idPermission=" + idPermission + '}';
    }
}

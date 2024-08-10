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
@Table(name = "Permission_Role", uniqueConstraints = {})
public class PermissionRole implements Serializable {

    @EmbeddedId
    private PermissionRolePK id;
    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Cause : navigabilité
    @JoinColumn(name = "id_role", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_permission", referencedColumnName = "id", insertable = false, updatable = false)
    private Permission permission;

    public PermissionRole() {
        active = true;
    }

    public PermissionRole(PermissionRolePK id, boolean assigne) {
        this.id = id;
        this.active = assigne;
    }

    public PermissionRole(boolean actif, Permission permission, Role role) {
        this(new PermissionRolePK(role.getId(), permission.getId()), actif);
        this.role = role;
        this.permission = permission;
    }

    public PermissionRole(Integer idRole, Integer idFonctionnalite, boolean assigne) {
        this(new PermissionRolePK(idRole, idFonctionnalite), assigne);
    }

    public static List<PermissionRole> createList(Role r, List<Permission> list) {
        List<PermissionRole> result = new ArrayList<>();
        list.forEach((p) -> {
            result.add(new PermissionRole(true, p, r));
        });
        return result;
    }

    public boolean isNew() {
        return id == null;
    }

    public PermissionRolePK getId() {
        return id;
    }

    public void setId(PermissionRolePK id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final PermissionRole other = (PermissionRole) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "PermissionRole{" + "id=" + id + ", assigne=" + active + ", role=" + role + ", permission=" + permission
                + '}';
    }
}

package com.esgis.jee.model.admin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * *********************************************************************
 * Module: Role.java Author: EL Capitain Purpose: Defines the Class Role
 * *********************************************************************
 */
@Entity
@Table(name = "Role", uniqueConstraints = { @UniqueConstraint(name = "CODE_ROLE_UQ", columnNames = { "code" }) })
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", length = 30, nullable = false)
    private String code;
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, mappedBy = "role", fetch = FetchType.EAGER)
    private List<PermissionRole> permissionRoleList;

    public Role() {
        this.permissionRoleList = new ArrayList<>();
    }

    public Role(String code, String nom, String description) {
        this();
        this.code = code;
        this.nom = nom;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionRole> getPermissionRoleList() {
        return permissionRoleList;
    }

    public void setPermissionRoleList(List<PermissionRole> permissionRoleList) {
        this.permissionRoleList = permissionRoleList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final Role other = (Role) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", code=" + code + ", nom=" + nom + ", description=" + description + '}';
    }

}

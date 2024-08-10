package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * **********************************************************************
 Module: Permission.java Author: EL Capitain Purpose: Defines the Class
 Permission
 **********************************************************************
 */
@Entity
@Table(
        name = "Permission",
        uniqueConstraints = {
            @UniqueConstraint(name = "UQ_OPERATION_id_IHM_PERM", columnNames = {"operation", "id_IHM"}),
            @UniqueConstraint(name = "UQ_NOM_PERMISSION", columnNames = {"nom"})
        }
)
public class Permission implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", length = 6, nullable = false)
    private Operation operation;
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;
    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_IHM", referencedColumnName = "id")
    private Ihm ihm;
    
    @OneToMany(cascade = {}, mappedBy = "permission", fetch = FetchType.LAZY)
    private List<PermissionRole> permissionRoleList;
    
    public Permission() {
        permissionRoleList = new ArrayList<>();
    }

    public Permission(Operation operation, String nom, Ihm ihm) {
        this();
        this.operation = operation;
        this.nom = nom;
        this.ihm = ihm;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Ihm getIhm() {
        return ihm;
    }

    public void setIhm(Ihm ihm) {
        this.ihm = ihm;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
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
        final Permission other = (Permission) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Permission{" + "id=" + id + ", operation=" + operation + ", nom=" + nom + ", ihm=" + ihm + '}';
    }
}

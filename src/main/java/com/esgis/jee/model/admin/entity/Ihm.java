package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Objects;

/**
 * *********************************************************************
 * Module: Ihm.java Author: EL Capitain Purpose: Defines the Class Ihm
 * *********************************************************************
 */
@Entity
@Table(
        name = "Ihm",
        uniqueConstraints = {@UniqueConstraint(name = "NOM_IHM_UQ", columnNames = {"nom"})}
)
public class Ihm implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;
    @Column(name = "description", length = 254, nullable = false)
    private String description;
    
    public Ihm() {
    }

    public Ihm(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final Ihm other = (Ihm) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Ihm{" + "id=" + id + ", nom=" + nom + ", description=" + description + '}';
    }

}

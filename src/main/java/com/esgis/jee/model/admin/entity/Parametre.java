/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author EL Capitain
 */
@Entity
@Table(name = "Parametre")
public class Parametre implements Serializable {
    
    @Id
    @Column(name = "code", length = 30)
    private String code;
    @Column(name = "description", length = 100, nullable = false)
    private String description;
    @Column(name = "valeur_Par_Defaut", length = 50, nullable = false)
    private String valeurParDefaut;

    public Parametre() {
    }
    
    public Parametre(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeurParDefaut() {
        return valeurParDefaut;
    }

    public void setValeurParDefaut(String valeurParDefaut) {
        this.valeurParDefaut = valeurParDefaut;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.code);
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
        final Parametre other = (Parametre) obj;
        return Objects.equals(this.code, other.code);
    }
    
    @Override
    public String toString() {
        return "Parametre{" + "code=" + code + ", description=" + description + ", valeurParDefaut=" + valeurParDefaut + '}';
    }
    
}

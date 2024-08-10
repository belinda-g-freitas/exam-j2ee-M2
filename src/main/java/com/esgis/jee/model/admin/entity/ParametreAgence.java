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
import jakarta.validation.constraints.Size;
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
        name = "Parametre_Agence",
        uniqueConstraints = {}
)
public class ParametreAgence implements Serializable {

    @EmbeddedId
    private ParametreAgencePK id;
    @Size(max = 100, message = "La valeur ne doit pas dépasser 100 caractères")
    @Column(name = "valeur", length = 100, nullable = false)
    private String valeur;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Cause : navigabilité
    @JoinColumn(name = "id_Agence", referencedColumnName = "id", insertable = true, updatable = true)
    private Agence agence;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "code_Parametre", referencedColumnName = "code", insertable = false, updatable = false)
    private Parametre parametre;
    
    public ParametreAgence() {
    }

    public ParametreAgence(String valeur, Agence agence, Parametre parametre) {
        this.valeur = valeur;
        this.agence = agence;
        this.parametre = parametre;
        this.id = new ParametreAgencePK(agence.getId(), parametre.getCode());
    }
    
    public static List<ParametreAgence> createList(Agence agence, List<Parametre> parametres) {
        List<ParametreAgence> result = new ArrayList<>();
        parametres.forEach((p) -> {
            result.add(new ParametreAgence(p.getValeurParDefaut(), agence, p));
        });
        return result;
    }

    public ParametreAgencePK getId() {
        return id;
    }

    public void setId(ParametreAgencePK id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Parametre getParametre() {
        return parametre;
    }

    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final ParametreAgence other = (ParametreAgence) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ParametreAgence{" + "id=" + id + ", valeur=" + valeur + ", agence=" + agence + ", parametre=" + parametre + '}';
    }
    
}

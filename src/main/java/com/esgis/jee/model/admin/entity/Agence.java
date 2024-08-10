/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author gyldas_atta_kouassi
 */
@Entity
@Table(name = "Agence", uniqueConstraints = { @UniqueConstraint(name = "UQ_CODE_AGENCE", columnNames = { "code" }) })
public class Agence implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 1, max = 3, message = "Le code doit être compris entre 1 et 3 caractères")
    @Column(name = "code", length = 3, nullable = false)
    private String code;
    @Size(min = 2, max = 100, message = "Le nom doit être compris entre 2 et 100 caractères")
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, mappedBy = "agence", fetch = FetchType.EAGER)
    private List<ParametreAgence> parametreAgenceList;

    public Agence() {
    }

    public Agence(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    public Agence(List<Parametre> parametres) {
        this();
        this.parametreAgenceList = ParametreAgence.createList(this, parametres);
    }

    public List<ParametreAgence> getParametreAgenceList() {
        return parametreAgenceList;
    }

    public void setParametreAgenceList(List<ParametreAgence> parametreAgenceList) {
        this.parametreAgenceList = parametreAgenceList;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final Agence other = (Agence) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Agence{" + "id=" + id + ", code=" + code + ", nom=" + nom + '}';
    }
}

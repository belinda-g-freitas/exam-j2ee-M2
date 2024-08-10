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
public class ParametreAgencePK implements Serializable {
    
    @Column(name = "id_Agence", insertable = false, updatable = false)
    private Integer idAgence;
    @Column(name = "code_Parametre")
    private String codeParametre;

    public ParametreAgencePK() {
    }

    public ParametreAgencePK(Integer idAgence, String codeParametre) {
        this.idAgence = idAgence;
        this.codeParametre = codeParametre;
    }

    public Integer getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(Integer idAgence) {
        this.idAgence = idAgence;
    }

    public String getCodeParametre() {
        return codeParametre;
    }

    public void setCodeParametre(String codeParametre) {
        this.codeParametre = codeParametre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idAgence);
        hash = 67 * hash + Objects.hashCode(this.codeParametre);
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
        final ParametreAgencePK other = (ParametreAgencePK) obj;
        if (!Objects.equals(this.codeParametre, other.codeParametre)) {
            return false;
        }
        return Objects.equals(this.idAgence, other.idAgence);
    }

    @Override
    public String toString() {
        return "ParametreAgencePK{" + "idAgence=" + idAgence + ", codeParametre=" + codeParametre + '}';
    }
}

package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Client", uniqueConstraints = {})

public class Client implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "identifiant", length = 30, nullable = false)
  private String identifiant;

  @Column(name = "nom", length = 50, nullable = false)
  private String nom;

  @Column(name = "prenoms", length = 150, nullable = false)
  private String prenoms;

  @Column(name = "telephone", length = 8, nullable = false)
  private String telephone;

  @Column(name = "email", length = 100, nullable = false)
  private String email;

  @Column(name = "date_enregistrement")
  @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
  private Date dateEnregistrement;

  @Column(name = "addresse", length = 100, nullable = false)
  private String addresse;

  

  public Client() {
    this.dateEnregistrement = new Date();
  }

  public Client(Integer id, String identifiant, String nom, String prenoms, String telephone, String email,
      Date dateEnregistrement, String addresse) {
    this.id = id;
    this.identifiant = identifiant;
    this.nom = nom;
    this.prenoms = prenoms;
    this.telephone = telephone;
    this.email = email;
    this.dateEnregistrement = dateEnregistrement;
    this.addresse = addresse;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getIdentifiant() {
    return identifiant;
  }

  public void setIdentifiant(String identifiant) {
    this.identifiant = identifiant;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenoms() {
    return prenoms;
  }

  public void setPrenoms(String prenoms) {
    this.prenoms = prenoms;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getDateEnregistrement() {
    return dateEnregistrement;
  }

  public void setDateEnregistrement(Date dateEnregistrement) {
    this.dateEnregistrement = dateEnregistrement;
  }

  public String getAddresse() {
    return addresse;
  }

  public void setAddresse(String addresse) {
    this.addresse = addresse;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((identifiant == null) ? 0 : identifiant.hashCode());
    result = prime * result + ((nom == null) ? 0 : nom.hashCode());
    result = prime * result + ((prenoms == null) ? 0 : prenoms.hashCode());
    result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((dateEnregistrement == null) ? 0 : dateEnregistrement.hashCode());
    result = prime * result + ((addresse == null) ? 0 : addresse.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Client other = (Client) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (identifiant == null) {
      if (other.identifiant != null)
        return false;
    } else if (!identifiant.equals(other.identifiant))
      return false;
    if (nom == null) {
      if (other.nom != null)
        return false;
    } else if (!nom.equals(other.nom))
      return false;
    if (prenoms == null) {
      if (other.prenoms != null)
        return false;
    } else if (!prenoms.equals(other.prenoms))
      return false;
    if (telephone == null) {
      if (other.telephone != null)
        return false;
    } else if (!telephone.equals(other.telephone))
      return false;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (dateEnregistrement == null) {
      if (other.dateEnregistrement != null)
        return false;
    } else if (!dateEnregistrement.equals(other.dateEnregistrement))
      return false;
    if (addresse == null) {
      if (other.addresse != null)
        return false;
    } else if (!addresse.equals(other.addresse))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Client [id=" + id + ", identifiant=" + identifiant + ", nom=" + nom + ", prenoms=" + prenoms
        + ", telephone=" + telephone + ", email=" + email + ", dateEnregistrement=" + dateEnregistrement + ", addresse="
        + addresse + "]";
  }

}

// Faites une simple IHM CRUD et prenez en compte le fait qu’on gère les droits
// d’accès à l’IHM d’édition de la fiche client

// Livrables : Vous fournissez à la fin le script DDL et DML de l’entité Client,
// le
// code source du logiciel et le build .war du projet prêt pour la mise en
// production.

// 1.3.1 Documentation
// Primefaces : https://www.primefaces.org/showcase/index.xhtml?jfwid=51cad
// Apache Shiro : https://shiro.apache.org
// JPA Spec :
// https://jakarta.ee/specifications/persistence/3.0/jakarta-persistence-spec-
// 3.0.pdf
// JPQL :
// https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-
// querylanguage/persistence-querylanguage.html
package com.esgis.jee.model.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * *********************************************************************
 * Module: Activite.java Author: EL Capitain Purpose: Defines the Class Activite
 **********************************************************************
 */

@Entity
@Table(name = "Activite", uniqueConstraints = {})
public class Activite implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "details", length = 1000, nullable = false)
    private String details;
    @Column(name = "date_Realisation", nullable = false)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date dateRealisation;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_session", referencedColumnName = "id")
    private Session session;

    public Activite() {
        dateRealisation = new Date();
    }

    public Activite(String details, Date dateRealisation, Session session) {
        this.details = details;
        this.dateRealisation = dateRealisation;
        this.session = session;
    }

    public Activite(String details, Session session) {
        this();
        this.details = details;
        this.session = session;
    }

    public Activite(String details, Date dateRealisation) {
        this.details = details;
        this.dateRealisation = dateRealisation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(Date dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Activite other = (Activite) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Activite{" + "id=" + id + ", details=" + details + ", dateRealisation=" + dateRealisation + '}';
    }

}

package com.esgis.jee.model.admin.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * *********************************************************************
 * Module: Activite.java Author: EL Capitain Purpose: Defines the Class Activite
 **********************************************************************
 */

@Getter
@Setter
@ToString(callSuper = true, doNotUseGetters = true, includeFieldNames = true, exclude = {})
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, of = { "id" })
@Entity
@Table(name = "journal_activites_view", uniqueConstraints = {})
public class JournalActivitesView implements Serializable {

    @Id
    @Column(name = "id_Activite")
    private Long idActivite;
    @Column(name = "id_Agence")
    private Integer idAgence;
    @Column(name = "nom_Agence", length = 1000, nullable = false)
    private String nomAgence;
    @Column(name = "nom_Utilisateur", length = 1000, nullable = false)
    private String nomUtilisateur;
    @Column(name = "date_debut_Session", nullable = false)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date dateDebutSession;
    @Column(name = "date_fin_Session", nullable = false)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date dateFinSession;
    @Column(name = "details_Activite", length = 1000, nullable = false)
    private String detailsActivite;
    @Column(name = "date_Realisation_Activite", nullable = false)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date dateRealisationActivite;

    public JournalActivitesView() {
    }

}

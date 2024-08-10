/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.controller.ext;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class FilterableExps {
    
    /* Tableaux */
    public static final String[]
            AGENCE
            = new String[]{
                "code",
                "nom"},
            UTILISATEUR
            = new String[]{
                "identifiant",
                "agence.nom",
                "longueurMdp",
                "nom",
                "dateInscription",
                "actif"};
}

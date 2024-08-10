/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.admin.dao;

import jakarta.enterprise.context.Dependent;
import org.apache.commons.collections.CollectionUtils;
import com.esgis.jee.model.admin.entity.Utilisateur;
import com.esgis.jee.model.infra.dao.AdminDaoImpl;
import com.esgis.jee.model.infra.dao.Dao;

/**
 *
 * @author Gyldas
 */
@Dependent
public class UtilisateurDao extends AdminDaoImpl<Utilisateur, Integer> implements Dao<Utilisateur, Integer> {

    @Override
    public Utilisateur create(Utilisateur entity, boolean flush, boolean refresh) throws RuntimeException {
        if (CollectionUtils.isEmpty(entity.getRoleUtilisateurList())) {
            throw new RuntimeException("Aucun rôle, veuillez ajouter des rôles à l'utilisateur");
        }
        return super.create(entity, flush, refresh);
    }
    
    
}
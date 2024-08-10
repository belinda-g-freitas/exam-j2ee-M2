/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.admin.dao;

import jakarta.enterprise.context.Dependent;
import com.esgis.jee.model.admin.entity.RoleUtilisateur;
import com.esgis.jee.model.admin.entity.RoleUtilisateurPK;
import com.esgis.jee.model.infra.dao.AdminDaoImpl;
import com.esgis.jee.model.infra.dao.Dao;

/**
 *
 * @author Gyldas
 */
@Dependent
public class RoleUtilisateurDao extends AdminDaoImpl<RoleUtilisateur, RoleUtilisateurPK> implements Dao<RoleUtilisateur, RoleUtilisateurPK> {
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.admin.dao;

import jakarta.enterprise.context.Dependent;
import com.esgis.jee.model.admin.entity.Session;
import com.esgis.jee.model.infra.dao.AdminDaoImpl;
import com.esgis.jee.model.infra.dao.Dao;

/**
 *
 * @author Gyldas
 */
@Dependent
public class SessionDao extends AdminDaoImpl<Session, Integer> implements Dao<Session, Integer> {
    
}
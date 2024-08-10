/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.admin.dao;

import com.esgis.jee.model.admin.entity.Ihm;
import com.esgis.jee.model.infra.dao.AdminDaoImpl;
import com.esgis.jee.model.infra.dao.Dao;
import jakarta.enterprise.context.Dependent;

/**
 *
 * @author Gyldas
 */
@Dependent
public class IhmDao extends AdminDaoImpl<Ihm, Integer> implements Dao<Ihm, Integer> {
    
}
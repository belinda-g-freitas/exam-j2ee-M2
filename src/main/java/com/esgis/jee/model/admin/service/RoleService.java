/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import jakarta.ejb.Local;

import java.util.List;

import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface RoleService extends Service<Role, Integer> {
}

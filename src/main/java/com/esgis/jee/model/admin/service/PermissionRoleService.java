/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.PermissionRole;
import com.esgis.jee.model.admin.entity.PermissionRolePK;
import com.esgis.jee.model.admin.entity.Role;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface PermissionRoleService extends Service<PermissionRole, PermissionRolePK> {
    
    /**
     * Trouve les PermissionRole par rôle
     * @param role
     * @return 
     * @throws java.lang.Exception 
     */
    public List<PermissionRole> trouverParRole(Role role) throws Exception;
}

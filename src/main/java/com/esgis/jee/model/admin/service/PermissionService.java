/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import java.util.List;
import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.Permission;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface PermissionService extends Service<Permission, Integer> {

    /**
     * Trouver la permission de tout faire
     * @return 
     * @throws java.lang.RuntimeException
     */
    public Permission trouverDoAnythingPerm() throws RuntimeException;

    /**
     * Trouver toutes les permissions excepté la permission de tout faire
     * @return 
     * @throws java.lang.RuntimeException
     */
    public List<Permission> trouverToutSaufDoAnythingPerm() throws RuntimeException;
    
    /**
     * Trouver le nom des permissions actives
     * @return
     * @throws RuntimeException 
     */
    public List<String> trouverNomPermissionsActives() throws RuntimeException;
    
    /**
     * Trouver les permissions actives
     * @return
     * @throws RuntimeException 
     */
    public List<Permission> trouverPermissionsActives() throws RuntimeException;
    
}

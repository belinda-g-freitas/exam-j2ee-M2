/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service;

import com.esgis.jee.model.admin.entity.Agence;
import com.esgis.jee.model.infra.service.Service;
import jakarta.ejb.Local;

/**
 *
 * @author Gigi
 */
@Local
public interface AgenceService extends Service<Agence, Integer> {
    
}

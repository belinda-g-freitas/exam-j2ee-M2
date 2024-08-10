/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.admin.service.ext;

import jakarta.ejb.Local;
import com.esgis.jee.model.admin.entity.view.JournalActivitesView;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author Gigi
 */
@Local
public interface JournalActivitesViewService extends Service<JournalActivitesView, Long> {
    
}

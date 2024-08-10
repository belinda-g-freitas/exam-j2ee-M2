package com.esgis.jee.model.admin.dao;

import jakarta.enterprise.context.Dependent;
import com.esgis.jee.model.admin.entity.Client;
import com.esgis.jee.model.infra.dao.AdminDaoImpl;
import com.esgis.jee.model.infra.dao.Dao;

@Dependent
public class ClientDao extends AdminDaoImpl<Client, Integer> implements Dao<Client, Integer> {
    
}
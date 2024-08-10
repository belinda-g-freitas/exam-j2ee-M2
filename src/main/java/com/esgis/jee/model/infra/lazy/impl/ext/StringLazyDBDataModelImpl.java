/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.lazy.impl.ext;

import com.esgis.jee.model.infra.lazy.impl.GenericLazyServiceDataModelImpl;
import com.esgis.jee.model.infra.service.Service;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class StringLazyDBDataModelImpl extends GenericLazyServiceDataModelImpl<String, String> {
    
    public StringLazyDBDataModelImpl(Service service, String[] filtrableFields, String methodToInvoke, Object... methodParameters) {
        super(service, service.getClass(), String.class, String.class, filtrableFields, methodToInvoke, methodParameters);
    }
    
    @Override
    public String getRowKey(String object) {
        return object;
    }
    
    @Override
    public String getRowData(String rowKey) {
        String result = data
                    .stream()
                    .filter((t) -> {
                        return rowKey.equals(t);
                    })
                    .findFirst()
                    .get();
        return result;
    }
    
}

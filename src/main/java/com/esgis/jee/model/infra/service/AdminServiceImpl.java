/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.infra.service;

import jakarta.enterprise.inject.spi.CDI;
import java.io.Serializable;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *
 * @author GiGi
 * @param <T>
 * @param <K>
 */
public abstract class AdminServiceImpl<T extends Serializable, K> extends ServiceImpl<T, K> {

    public AdminServiceImpl() {
    }
    
    /**
     * 
     * @param <T> la classe modèle concernée par le Service
     * @param app le nom de l'application déployée
     * @param classe
     * @return 
     */
    public static <T extends Service> T getInstance(String app, Class<T> classe) {
        try {
            InitialContext context = new InitialContext();
            return (T) context.lookup(String.format("java:global/%s/Admin%sImpl", app, classe.getSimpleName()));
        } catch (NamingException ex) {
            return CDI.current().select(classe).get();
        }
    }
}
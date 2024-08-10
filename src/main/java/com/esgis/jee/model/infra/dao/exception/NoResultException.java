/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.dao.exception;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class NoResultException extends RuntimeException {

    /**
     * Creates a new instance of <code>NoResultException</code> without detail
     * message.
     */
    public NoResultException() {
    }

    /**
     * Constructs an instance of <code>NoResultException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoResultException(String msg) {
        super(msg);
    }
}

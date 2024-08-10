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
public class ValidationException extends RuntimeException {

    /**
     * Creates a new instance of <code>ValidationException</code> without detail
     * message.
     */
    public ValidationException() {
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }
}

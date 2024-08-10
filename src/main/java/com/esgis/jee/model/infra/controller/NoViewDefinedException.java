/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.infra.controller;

/**
 *
 * @author Gyldas
 */
public class NoViewDefinedException extends Exception {

    /**
     * Creates a new instance of <code>NoViewDefinedException</code> without
     * detail message.
     */
    public NoViewDefinedException() {
    }

    /**
     * Constructs an instance of <code>NoViewDefinedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoViewDefinedException(String msg) {
        super(msg);
    }
}
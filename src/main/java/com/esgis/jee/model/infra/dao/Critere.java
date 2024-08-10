/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.dao;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class Critere {
    
    private OperateurSql op;
    private String column;
    private Object value;

    public Critere() {
    }

    public Critere(OperateurSql op, String column, Object value) {
        this.op = op;
        this.column = column;
        this.value = value;
    }
    
    public Critere(String column, Object value) {
        this.op = OperateurSql.NONE;
        this.column = column;
        this.value = value;
    }
    
    /**
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the op
     */
    public OperateurSql getOp() {
        return op;
    }

    /**
     * @param op the op to set
     */
    public void setOp(OperateurSql op) {
        this.op = op;
    }   
}
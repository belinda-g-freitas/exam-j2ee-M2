/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.lazy;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

/**
 *
 * @author gyldas_atta_kouassi
 * @param <T>
 */
public class LazySorter<T> implements Comparator<T> {

    private final String sortField;
    private final SortOrder sortOrder;

    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
    
	@SuppressWarnings("unchecked")
	@Override
    public int compare(T model1, T model2) {
        int value = 0;
        Object model1Val = null, 
                model2Val = null;
        if(model1 != null) {
            model1Val = LazyTaskUtils.getValue(model1, sortField);
        }
        if(model2 != null) {
            model2Val = LazyTaskUtils.getValue(model2, sortField);
        }
        // Les valeurs nulles sont supérieures aux valeurs non nulles. Raison : les faire remarquer en premier plan
        if(model1Val != null && model2Val == null) {
            //value = 1;
        	value = -1;
        }
        if(model1Val == null && model2Val != null) {
            //value = -1;
        	value = 1;
        }
        //Les valeurs non nulles sont comparables
        if(model1Val != null && model2Val != null) {
            value = ((Comparable<Object>) model1Val).compareTo(model2Val);
        }
        return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
    }
}

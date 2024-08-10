/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.lazy.component;

import com.esgis.jee.model.infra.lazy.GenericLazyDataModel;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gyldas_atta_kouassi
 * @param <T>
 */
@Getter
@Setter
public class LazySelectOneMenuComponentManager<T extends Serializable, K> implements Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1230380600195649460L;

    protected GenericLazyDataModel<T, K> dataModel;
    protected Set<T> simulatorSet;
    protected T noSelectionObject; // Sert pour l'absence de valeur
    protected T modelSomObject; // à définir sur la sélection de la dataTable
    protected Object destObject;
    protected String destFieldName;
    protected String dialogTitle;

    public LazySelectOneMenuComponentManager(GenericLazyDataModel<T, K> dataModel) {
        this.dataModel = dataModel;
        this.simulatorSet = Collections.synchronizedSet(new HashSet<>());
    }

    public LazySelectOneMenuComponentManager(GenericLazyDataModel<T, K> dataModel, T noSelectionObject) {
        this(dataModel);
        this.noSelectionObject = noSelectionObject;
        addSimulatorObject(noSelectionObject);
    }

    public LazySelectOneMenuComponentManager(GenericLazyDataModel<T, K> dataModel, T noSelectionObject, String dialogTitle) {
        this(dataModel, noSelectionObject);
        this.dialogTitle = dialogTitle;
    }

    public LazySelectOneMenuComponentManager(GenericLazyDataModel<T, K> dataModel, T noSelectionObject, String destFieldName, String dialogTitle) {
        this(dataModel, noSelectionObject, dialogTitle);
        this.destFieldName = destFieldName;
    }

    public LazySelectOneMenuComponentManager(GenericLazyDataModel<T, K> dataModel, T noSelectionObject, Object destObject, String destFieldName, String dialogTitle) {
        this(dataModel, noSelectionObject, destFieldName, dialogTitle);
        this.destObject = destObject;
    }

    /**
     * Vérifie si la liste déroulante est vide ou pas
     * @return 
     */
    public boolean isEmpty() {
        return !getDataModel().hasData();
    }
    
    /**
     * Action à exécuter lors du déroulement du menu déroulant de sélection
     * d'une personne
     *
     * @param destObject
     * @param destFieldName
     * @param dialogTitle
     */
    public void onFocus() {}
    
    /**
     * Action à exécuter lors du déroulement du menu déroulant de sélection
     * d'une personne
     *
     * @param destObject
     * @param destFieldName
     * @param dialogTitle
     */
    public void onFocus(Object destObject, String destFieldName, String dialogTitle) {
        setDestObject(destObject);
        setDestFieldName(destFieldName);
        setDialogTitle(dialogTitle);
    }

    /**
     * Action à exécuter lors de la sélection
     *
     * @param withValue indique si la sélection est à valeur concrète ou est à
     * valeur nulle
     */
    public void onSelect(boolean withValue) {
        if (withValue) {
            setValue(modelSomObject);
        } else {
            setValue(noSelectionObject);
        }
    }

    public void setValue(T value) {
        try {
            BeanUtils.setProperty(getDestObject(), getDestFieldName(), value);
            addSimulatorObject(value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(LazySelectOneMenuComponentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
	public T getValue() {
        try {
            return (T) BeanUtils.getProperty(getDestObject(), getDestFieldName());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(LazySelectOneMenuComponentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public final void addSimulatorObject(T value) {
        if (value != null /* && !simulatorSet.contains(value)*/) {
            simulatorSet.add(value);
        }
    }

    public T getNoSelectionObject() {
        return noSelectionObject;
    }

    public void setNoSelectionObject(T noSelectionObject) {
        this.noSelectionObject = noSelectionObject;
    }

    public T getModelSomObject() {
        return modelSomObject;
    }

    public void setModelSomObject(T modelSomObject) {
        this.modelSomObject = modelSomObject;
    }

    public void setDestObject(Object destObject) {
        this.destObject = destObject;
    }

    public Object getDestObject() {
        return destObject;
    }

    public GenericLazyDataModel<T, K> getDataModel() {
        return dataModel;
    }

    public void setDataModel(GenericLazyDataModel<T, K> dataModel) {
        this.dataModel = dataModel;
    }

    public String getDestFieldName() {
        return destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public Set<T> getSimulatorSet() {
        return simulatorSet;
    }

    public void setSimulatorSet(Set<T> simulatorSet) {
        this.simulatorSet = simulatorSet;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.esgis.jee.model.business.presentation.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

/**
 *
 * @author Gyldas
 */
@FacesValidator(value = "qteValid")
public class QteValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if(o instanceof Integer) {
            int min = ((Double) uic.getAttributes().get("min")).intValue();
            int max = ((Double) uic.getAttributes().get("max")).intValue();
            if(((Integer)o) < min || ((Integer)o) > max) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format("La qté doit être >= %d, <= %d", min, max), null));
            }
        }
    }
}

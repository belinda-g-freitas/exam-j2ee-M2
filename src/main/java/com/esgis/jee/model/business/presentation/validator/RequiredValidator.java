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
@FacesValidator(value = "requiredAndNotBlankValid")
public class RequiredValidator implements Validator{

	@Override
	public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
		if(o instanceof String) {
			String val = String.valueOf(o).trim();
			if(val == null || val.isEmpty()) {
				boolean required = ((boolean) uic.getAttributes().get("required"));
				if(required)
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format("Obligatoire"), null));
				else
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format("Non vide"), null));
			}
		}
	}
}

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
import java.math.BigInteger;

/**
 *
 * @author Gyldas
 */
@FacesValidator(value = "montantValid")
public class MontantValidator implements Validator {

	@Override
	public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
		if (o instanceof BigInteger && ((BigInteger) o).intValue() < 1) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Le montant doit être >= 1", null));
		}
	}
}

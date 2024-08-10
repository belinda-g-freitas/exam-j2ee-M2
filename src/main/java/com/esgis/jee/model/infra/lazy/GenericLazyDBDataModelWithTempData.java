package com.esgis.jee.model.infra.lazy;

import java.io.Serializable;

public interface GenericLazyDBDataModelWithTempData<T extends Serializable, K> extends GenericLazyDBDataModel<T, K>, HasTempData<T>, HasDBActions<T> {
	
}

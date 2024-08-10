package com.esgis.jee.model.infra.lazy;

import java.io.Serializable;

public interface GenericLazyServiceDataModelWithTempData<T extends Serializable, K> extends GenericLazyServiceDataModel<T, K>, HasTempData<T>, HasDBActions<T> {
	
}
package com.simicart.core.base.networkcloud.delegate;

import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.networkcloud.request.error.SimiError;

public interface ModelDelegate {
	public abstract void onFail(SimiError error);

	public abstract void onSuccess(SimiCollection collection);
}

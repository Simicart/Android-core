package com.simicart.core.base.networkcloud.delegate;

import com.simicart.core.base.networkcloud.response.CoreResponse;

public interface NetWorkDelegate {
	public abstract void callBack(CoreResponse coreResponse, boolean isSuccess);
}

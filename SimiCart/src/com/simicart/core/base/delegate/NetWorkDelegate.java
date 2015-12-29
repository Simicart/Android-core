package com.simicart.core.base.delegate;

import com.simicart.core.base.network.response.CoreResponse;

public interface  NetWorkDelegate{
	public abstract void callBack(CoreResponse coreResponse, boolean isSuccess);
}

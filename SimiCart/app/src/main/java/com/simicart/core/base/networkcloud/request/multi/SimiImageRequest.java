package com.simicart.core.base.networkcloud.request.multi;

import com.simicart.core.base.networkcloud.delegate.NetWorkDelegate;
import com.simicart.core.base.networkcloud.response.CoreResponse;

public class SimiImageRequest extends SimiRequest {

	public SimiImageRequest(String url, NetWorkDelegate delegate) {
		super(url, delegate);
	}

	@Override
	public CoreResponse parseNetworkResponse(SimiNetworkResponse response) {
		byte[] data = response.getData();

		return null;
	}

	@Override
	public void deliveryCoreResponse(CoreResponse response) {
		if (null != response) {
			mDelegate.callBack(response, true);
		} else {
			mDelegate.callBack(response, false);
		}

	}

}

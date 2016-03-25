package com.simicart.core.base.networkcloud.request.multi;

import org.apache.http.HttpResponse;

public class SimiUrlStack implements SimiHttpStack {

	@Override
	public HttpResponse performRequest(SimiRequest request) {
		SimiUrlConnection urlConnection = new SimiUrlConnection();
		return urlConnection.makeUrlConnection(request);
	}

}

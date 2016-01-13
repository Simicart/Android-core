package com.simicart.core.base.network.request;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface SimiHttpStack {

	public HttpResponse performRequest(SimiRequest request)
			throws ClientProtocolException, IOException;
}

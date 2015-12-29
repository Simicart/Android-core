package com.simicart.core.base.network.mock;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class MockHttpClient implements HttpClient {
	private int mStatusCode = HttpStatus.SC_OK;
	private HttpEntity mResponseEntity = null;

	@Override
	public HttpResponse execute(HttpUriRequest request) throws IOException,
			ClientProtocolException {
		// TODO Auto-generated method stub
		return execute(request, (HttpContext) null);
	}

	public void setResponseData(HttpEntity entity) {
		mStatusCode = HttpStatus.SC_OK;
		mResponseEntity = entity;
	}

	public void setErrorCode(int statusCode) {
		if (statusCode == HttpStatus.SC_OK) {
			throw new IllegalArgumentException(
					"statusCode cannot be 200 for an error");
		}
		mStatusCode = statusCode;
	}

	public HttpUriRequest requestExecuted = null;

	// This is the only one we actually use.
	@Override
	public HttpResponse execute(HttpUriRequest request, HttpContext context) {
		requestExecuted = request;
		StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP",
				1, 1), mStatusCode, "");
		HttpResponse response = new BasicHttpResponse(statusLine);
		response.setEntity(mResponseEntity);

		return response;
	}

	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request)
			throws IOException, ClientProtocolException {
		return execute(target, request, (HttpContext) null);
	}

	@Override
	public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request,
			HttpContext context) throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public <T> T execute(HttpUriRequest arg0,
			ResponseHandler<? extends T> arg1, HttpContext arg2)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2) throws IOException,
			ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientConnectionManager getConnectionManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpParams getParams() {
		// TODO Auto-generated method stub
		return null;
	}

}

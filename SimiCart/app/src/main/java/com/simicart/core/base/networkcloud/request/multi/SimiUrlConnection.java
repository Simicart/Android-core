package com.simicart.core.base.networkcloud.request.multi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.networkcloud.request.multi.SimiRequest.Method;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;

public class SimiUrlConnection {

	// private static SimiUrlConnection instance;
	static final String COOKIES_HEADER = "Set-Cookie";
	static CookieManager cookieManager = new CookieManager();
	static boolean isSet = false;

	public SimiUrlConnection() {
		if (!isSet) {
			isSet = true;
			CookieHandler.setDefault(cookieManager);
		}
	}

	public HttpResponse makeUrlConnection(SimiRequest request) {

		String url_extended = request.getUrl();

		Log.e("SimiUrlConnection ", "makeUrlConnection " + url_extended);

		// if (url_extended.equals(Constants.SIGN_IN)) {
		// request.onStopRequestQueue();
		// }

		String url = Config.getInstance().getBaseCloudUrl() + url_extended;

		if (request.getTypeMethod() == Method.GET) {
			String parameters = getParameterURL_GET(request.getDataParameter());
			if (Utils.validateString(parameters)) {
				Log.e("SimiURLConnection", "Parameter " + parameters);
				url = url + "?" + parameters;
			}
		}

		Log.e("SimiUrlConnection ", "URL " + url);

		try {
			int type = request.getTypeMethod();
			HttpURLConnection urlConnection = null;
			URL url_connection = new URL(url);
			urlConnection = (HttpURLConnection) url_connection.openConnection();

			urlConnection.setDoInput(true);
			if (type == Method.GET || type == Method.DELETE) {
				urlConnection.setDoOutput(false);
			} else {
				urlConnection.setDoOutput(true);
			}

			// add Authorization, Content-Type Header
			String token = "Bearer " + Config.getInstance().getSecretCloudKey();

			Log.e("SimiUrlConnection ", "token " + token);

			urlConnection.setRequestProperty("Authorization", token);
			urlConnection
					.setRequestProperty("Content-Type", "application/json");

			// add header
			HashMap<String, String> headerAddtional = request
					.getHeaderAddtional();
			if (null != headerAddtional) {
				for (String key : headerAddtional.keySet()) {
					urlConnection.setRequestProperty(key,
							headerAddtional.get(key));
				}
			}

			Log.e("SimiUrlConnection ", "Type " + type);

			// set type method for request
			if (type == Method.GET) {
				urlConnection.setRequestMethod("GET");
			} else if (type == Method.POST) {
				urlConnection.setRequestMethod("POST");

				Log.e("SimiUrlConnection ", "POSTTTTTTTTTTTTTTTT" + url);

				JSONObject postBody = request.getBody();
				if (null != postBody) {
					Log.e("SimiUrlConnection ", "PARAM " + postBody.toString());
					OutputStream os = urlConnection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(os, "UTF-8"));
					writer.write(postBody.toString());
					writer.flush();
					writer.close();
					os.close();
				} else {
					Log.e("SimiUrlConnection ", "Data POST NULL");
				}
			} else if (type == Method.PUT) {
				urlConnection.setRequestMethod("PUT");
				JSONObject putBody = request.getBody();
				if (null != putBody) {
					Log.e("SimiUrlConnection ", "PARAM " + putBody.toString());
					OutputStream os = urlConnection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(os, "UTF-8"));
					writer.write(putBody.toString());
					writer.flush();
					writer.close();
					os.close();
				}

			} else if (type == Method.DELETE) {
				urlConnection.setRequestMethod("DELETE");
				JSONObject deleteBody = request.getBody();
				if (null != deleteBody) {
					Log.e("SimiUrlConnection ",
							"PARAM " + deleteBody.toString());
					OutputStream os = urlConnection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(os, "UTF-8"));
					writer.write(deleteBody.toString());
					writer.flush();
					writer.close();
					os.close();
				}
			}

			// process response
			ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
			int responseCode = urlConnection.getResponseCode();
			if (responseCode != -1) {
				StatusLine responseStatus = new BasicStatusLine(
						protocolVersion, urlConnection.getResponseCode(),
						urlConnection.getResponseMessage());
				BasicHttpResponse response = new BasicHttpResponse(
						responseStatus);
				response.setEntity(entityFromConnection(urlConnection));
				for (Entry<String, List<String>> header : urlConnection
						.getHeaderFields().entrySet()) {
					if (header.getKey() != null) {
						Header h = new BasicHeader(header.getKey(), header
								.getValue().get(0));
						response.addHeader(h);
					}
				}
				// if (url_extended.equals(Constants.SIGN_IN)) {
				// request.onStartRequestQueue();
				// }
				// Log.e("SimiURLConnection Finish ",
				// url_extended + System.currentTimeMillis());
				return response;
			}

		} catch (IOException e) {
			Log.e("SimiUrlStack ", "IOException " + e.getMessage());
		}
		// if (url_extended.equals(Constants.SIGN_IN)) {
		// request.onStartRequestQueue();
		// }
		request.cancel(true);
		return null;
	}

	protected String getParameterURL_GET(HashMap<String, String> dataExtend) {
		// Log.e("SimiUrlConnection", "getParameterURL_GET 001");
		if (null != dataExtend && dataExtend.size() > 0) {
			// Log.e("SimiUrlConnection", "getParameterURL_GET 002");
			Iterator<Entry<String, String>> iterator = dataExtend.entrySet()
					.iterator();
			boolean isFirst = true;
			StringBuilder builder = new StringBuilder();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String parameter = getAParameter(entry);
				// Log.e("SimiUrlConnection", "getParameterURL_GET " +
				// parameter);
				if (Utils.validateString(parameter)) {
					if (isFirst) {
						isFirst = false;
						builder.append(parameter);
					} else {
						builder.append("&");
						builder.append(parameter);
					}
				}
			}
			return builder.toString();
		}

		return "";
	}

	protected String getAParameter(Entry<String, String> entry) {
		String param = "";
		String key = entry.getKey();
		String value = entry.getValue();
		if (Utils.validateString(key) && Utils.validateString(value)) {
			param = key + "=" + value;
		}
		return param;
	}

	// protected String getEntity(JSONObject json)
	// throws UnsupportedEncodingException {
	// StringBuilder result = new StringBuilder();
	// result.append(URLEncoder.encode("data", "UTF-8"));
	// result.append("=");
	// result.append(URLEncoder.encode(json.toString(), "UTF-8"));
	//
	// return result.toString();
	// }

	protected HttpEntity entityFromConnection(HttpURLConnection connection) {
		BasicHttpEntity entity = new BasicHttpEntity();
		InputStream inputStream;
		try {
			inputStream = connection.getInputStream();
		} catch (IOException ioe) {
			inputStream = connection.getErrorStream();
		}
		entity.setContent(inputStream);
		entity.setContentLength(connection.getContentLength());
		entity.setContentEncoding(connection.getContentEncoding());
		entity.setContentType(connection.getContentType());
		return entity;
	}

}

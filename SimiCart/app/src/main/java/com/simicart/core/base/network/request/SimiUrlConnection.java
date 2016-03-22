package com.simicart.core.base.network.request;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.network.request.SimiRequest.Method;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;

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
		// if (url_extended.equals(Constants.SIGN_IN)) {
		// request.onStopRequestQueue();
		// }

		String url = Config.getInstance().getBaseUrl() + url_extended;
		Log.e("SimiUrlStack  ", "URL " + url);
		int type = request.getTypeMethod();

		HttpURLConnection urlConnection = null;
		try {
			URL url_connection = new URL(url);
			if (url.contains("https")) {
				// HttpsURLConnection httpsUrlConnection = (HttpsURLConnection)
				// urlConnection;
				// SSLSocketFactory sslSocketFactory =
				// createTrustAllSslSocketFactory();
				urlConnection = (HttpsURLConnection) url_connection
						.openConnection();
				try {
					((HttpsURLConnection) urlConnection)
							.setSSLSocketFactory(new TLSSocketFactory());
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				urlConnection = (HttpURLConnection) url_connection
						.openConnection();
			}

			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Token", Config.getInstance()
					.getSecretKey());
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			String userAgent = System.getProperty("http.agent");
			if (!DataLocal.isTablet) {
				userAgent = userAgent + " Mobile";
			}
			urlConnection.setRequestProperty(HTTP.USER_AGENT, userAgent);

			// add header
			HashMap<String, String> headerAddtional = request
					.getHeaderAddtional();
			if (null != headerAddtional) {
				for (String key : headerAddtional.keySet()) {
					urlConnection.setRequestProperty(key,
							headerAddtional.get(key));
				}
			}
			if (type == Method.GET) {
				urlConnection.setRequestMethod("GET");
			} else {
				urlConnection.setRequestMethod("POST");
				JSONObject postBody = request.getPostBody();
				if (null != postBody) {
					Log.e("SimiUrlStack ", "PARAM " + postBody.toString());
					OutputStream os = urlConnection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(os, "UTF-8"));
					writer.write(getEntity(postBody));
					writer.flush();
					writer.close();
					os.close();
				}
			}

			Map<String, List<String>> headerFields = urlConnection
					.getHeaderFields();

			List<String> locationHeader = headerFields.get("Location");
			if (null != locationHeader) {
				for (String location : locationHeader) {
					Log.e("SimiUrlConnection ", "LOCATION " + location);
				}
			}

			List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

			if (cookiesHeader != null) {
				for (String cookie : cookiesHeader) {
					if (cookie.contains("frontend")) {
						Config.getInstance().setCookie(cookie);
					}
					HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
					if (null != httpCookie) {
						cookieManager.getCookieStore().add(null, httpCookie);
					}

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

	protected String getEntity(JSONObject json)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		result.append(URLEncoder.encode("data", "UTF-8"));
		result.append("=");
		result.append(URLEncoder.encode(json.toString(), "UTF-8"));

		return result.toString();
	}

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

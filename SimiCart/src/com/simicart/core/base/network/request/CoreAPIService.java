package com.simicart.core.base.network.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;

@SuppressLint("DefaultLocale")
public class CoreAPIService {
	private static CoreAPIService instance;
	public static String BASE_URL_HTTP = Config.getInstance().getBaseUrl();
	public static String BASE_URL_HTTPS = Config.getInstance().getBaseUrl();
	public static int ERROR_CONNECT = -1;
	public static DefaultHttpClient httpClient = null;
	public static HttpContext httpContext;

	ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

		public long getKeepAliveDuration(HttpResponse response,
				HttpContext context) {
			HeaderElementIterator it = new BasicHeaderElementIterator(
					response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return Long.parseLong(value) * 1000;
					} catch (NumberFormatException ignore) {
					}
				}
			}
			return 30 * 1000;
		}

	};

	private CoreAPIService() {
		httpClient = getNewHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
	}

	public static DefaultHttpClient getNewHttpClient() {
		try {
			if (httpClient == null) {

				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);
				httpClient = new DefaultHttpClient(ccm, params);
			}
			return httpClient;

		} catch (Exception e) {
			if (httpClient == null) {
				httpClient = new DefaultHttpClient();
			}
			return httpClient;
		}
	}

	public static CoreAPIService getInstacnce() {
		if (null == instance) {
			instance = new CoreAPIService();
		}
		return instance;
	}

	public String getDataFromServer(JSONObject params, String urlExtension) {
		if (null == params) {
			return null;
		}
		String url = Config.getInstance().getBaseUrl() + urlExtension;
		Log.e("CoreAPIService", "Url : " + url);
		Log.e("CoreAPIService", "params : " + params.toString());

		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity = createEntity(params);
		if (null != entity) {
			httpPost.setEntity(entity);
		}
		return excutePost(httpPost);
	}

	protected String getEntity(JSONObject json)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		result.append(URLEncoder.encode("data", "UTF-8"));
		result.append("=");
		result.append(URLEncoder.encode(json.toString(), "UTF-8"));

		return result.toString();
	}

	public UrlEncodedFormEntity createEntity(JSONObject json) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("data", json.toString()));
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, "UTF-8");
			return entity;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public String excutePost(HttpPost httpPost) {
		InputStream is = null;
		HttpResponse httpResponse;
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 6000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60000);
		httpClient.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		httpClient.setKeepAliveStrategy(myStrategy);
		httpPost.setHeader("Connection", "Keep-Alive");
		httpPost.setHeader("Token", Config.getInstance().getSecretKey());
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// String userAgent = System.getProperty("http.agent");
		// httpPost.setHeader(HTTP.USER_AGENT, userAgent);
		String userAgent = System.getProperty("http.agent");
		if (!DataLocal.isTablet) {
			userAgent = userAgent + " Mobile";
		}
		httpPost.setHeader(HTTP.USER_AGENT, userAgent);

		try {
			httpResponse = httpClient.execute(httpPost, httpContext);
			StatusLine statusLine = httpResponse.getStatusLine();

			// truong 02-06
			Header[] cookies = httpResponse.getHeaders("Set-Cookie");
			// for (Header header : cookies) {
			if (cookies != null && cookies.length > 0) {
				String value = cookies[0].getValue();
				Log.e("CoreAPIService", "Value " + value);
				Config.getInstance().setCookie(value);
			}

			int statusCode = statusLine.getStatusCode();
			HttpEntity httpEntity;
			if (statusCode < 400) {

				httpEntity = httpResponse.getEntity();

				// String result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
				//
				// Log.e("CoreAPIService  ", "RESULT" + result);
				// return result;

				is = httpEntity.getContent();

				Header contentEncoding = httpResponse
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					is = new GZIPInputStream(is);
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				String result = sb.toString();
				return result;
			} else {
				Log.e("CoreAPIService StatusCode ", httpResponse
						.getStatusLine().getReasonPhrase()
						+ "Code "
						+ statusCode);
				httpResponse.getEntity().getContent().close();
				return String.format("%d", statusCode);
			}
		} catch (ClientProtocolException e) {
			Log.e("CoreAPIService ",
					"ClientProtocolException " + e.getMessage());
			return String.format("%d", ERROR_CONNECT);
		} catch (IOException e) {
			Log.e("CoreAPIService ", "IOException " + e.getMessage());
			return String.format("%d", ERROR_CONNECT);
		}

	}
}

package com.simicart.core.notification.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.config.Config;
import com.simicart.core.notification.gcm.GCMRegistrar;
import com.simicart.core.notification.model.RegisterIDModel;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

	// /**
	// * Register this account/device pair within the server.
	// *
	// * @return whether the registration succeeded or not.
	// */
	public static boolean register(final Context context, final String regId,
			String latitude, String longitude) {
		RegisterIDModel model = new RegisterIDModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				Log.e(getClass().getName(), "RegisterIDModel: " + message);
				GCMRegistrar.setRegisteredOnServer(context, true);
			}
		});
		model.addParam("device_token", regId);
		if (!longitude.equals("") && !latitude.equals("")) {
			model.addParam("latitude", latitude);
			model.addParam("longitude", longitude);
		}
		model.request();

		return false;
	}

	public static boolean register(final Context context, final String regId) {
		RegisterIDModel model = new RegisterIDModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				Log.e(getClass().getName(), "RegisterIDModel: " + message);
				GCMRegistrar.setRegisteredOnServer(context, true);
			}
		});
		model.addParam("device_token", regId);
		model.request();

		return false;
	}

	// /**
	// * Register this account/device pair within the server.
	// *
	// * @return whether the registration succeeded or not.
	// */
	// public static boolean register(final Context context, final String regId)
	// {
	// String serverUrl = Config.getInstance().getBaseUrl()
	// + "connector/config/register_device";
	// JSONObject obj = new JSONObject();
	// try {
	// obj.put("device_token", regId);
	// GPSTracker gps = new GPSTracker(context);
	// // check if GPS enabled
	// if (gps.canGetLocation()) {
	// double latitude = gps.getLatitude();
	// double longitude = gps.getLongitude();
	// Log.e("ServerUtilities", "Your Location is - \nLat: "
	// + latitude + "\nLong: " + longitude);
	// obj.put("latitude", latitude);
	// obj.put("longitude", longitude);
	// } else {
	// Log.e("ServerUtilities",
	// "GPS is not enabled. Please enable it to get your current location");
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("data", obj.toString());
	//
	// /*
	// * nhatvm: 26/11/2014
	// */
	// try {
	// post(serverUrl, params);
	// GCMRegistrar.setRegisteredOnServer(context, true);
	// return true;
	// } catch (IOException e) {
	//
	// }
	//
	// return false;
	// }

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.e(CommonUtilities.TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Token", Config.getInstance()
					.getSecretKey());
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static void unregister(Context context, String regId) {
		// TODO Auto-generated method stub
	}

}

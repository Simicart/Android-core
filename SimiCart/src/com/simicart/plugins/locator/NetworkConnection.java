package com.simicart.plugins.locator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

	public static boolean haveInternet(Context ctx) {

		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return false;
		}

		return true;
	}

	public static String checkNetworkStatus(Context ctx) {

		String NetworkStatus = "";
		final ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			NetworkStatus = "wifi";
		} else if (mobile.isAvailable()) {
			NetworkStatus = "mobile";
		} else {
			NetworkStatus = "";
		}
		return NetworkStatus;
	}
}

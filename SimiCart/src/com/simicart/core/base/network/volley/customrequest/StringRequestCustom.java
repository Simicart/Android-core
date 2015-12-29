package com.simicart.core.base.network.volley.customrequest;

import java.util.HashMap;
import java.util.Map;

import com.simicart.core.base.network.volley.AuthFailureError;
import com.simicart.core.base.network.volley.Response.ErrorListener;
import com.simicart.core.base.network.volley.Response.Listener;
import com.simicart.core.base.network.volley.toolbox.StringRequest;
import com.simicart.core.config.Config;

public class StringRequestCustom extends StringRequest {

	private Map<String, String> mHeaders = new HashMap<>();

	public StringRequestCustom(int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	public void setCookies(String cookies) {
		mHeaders.put("Connection", "Keep-Alive");
		mHeaders.put("Token", Config.getInstance().getSecretKey());
		// mHeaders.put("Content-Type", "application/x-www-form-urlencoded");
		mHeaders.put("Content-Type", "application/json");
		if (!cookies.equals("")) {
			mHeaders.put("Cookie", cookies);
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

}

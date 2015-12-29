package com.simicart.core.base.network.volley.customrequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.network.volley.AuthFailureError;
import com.simicart.core.base.network.volley.Response;
import com.simicart.core.base.network.volley.Response.ErrorListener;
import com.simicart.core.base.network.volley.Response.Listener;
import com.simicart.core.base.network.volley.toolbox.JsonObjectRequest;
import com.simicart.core.config.Config;

public class JsonObjectRequestCustom extends JsonObjectRequest {

	private Map<String, String> mHeaders = new HashMap<>();
	private Map<String, String> mParams = new HashMap<String, String>();
	private String cookiesHeader = "";

	public JsonObjectRequestCustom(int method, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public void setCookies(String cookies) {
		mHeaders.put("Connection", "Keep-Alive");
		mHeaders.put("Token", Config.getInstance().getSecretKey());
//		mHeaders.put("Content-Type", "application/x-www-form-urlencoded");
		mHeaders.put("Content-Type", "application/json");
		if (!cookies.equals("")) {
			mHeaders.put("Cookie", cookies);
		}
	}

	public void addParams(HashMap<String, Object> mHashMap) {
		try {
			Iterator<Entry<String, Object>> iter = mHashMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();
				mParams.put((String) mEntry.getKey(),
						(String) mEntry.getValue());
			}
		} catch (Exception e) {
			Log.e("JsonObjectRequest-Add Params:", e.getMessage());
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams != null ? mParams : super.getParams();
	}
}

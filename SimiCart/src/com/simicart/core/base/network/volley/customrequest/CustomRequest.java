package com.simicart.core.base.network.volley.customrequest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.volley.AppController;
import com.simicart.core.base.network.volley.AuthFailureError;
import com.simicart.core.base.network.volley.NetworkResponse;
import com.simicart.core.base.network.volley.Response;
import com.simicart.core.base.network.volley.Response.ErrorListener;
import com.simicart.core.base.network.volley.Response.Listener;
import com.simicart.core.base.network.volley.toolbox.StringRequest;
import com.simicart.core.config.Config;

public class CustomRequest extends StringRequest{

	private Map<String, String> mParam = new HashMap<String, String>();
	private Map<String, String> mHeaders = new HashMap<>();
	private String url = "";
	public CustomRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		this.url = url;
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		mHeaders.put("Token", Config.getInstance().getSecretKey());
		mHeaders.put("Cookie", AppController.getCookie(SimiManager.getIntance().getCurrentContext()));
		return mHeaders;
	}
	
	public void setParams(JSONObject object){
		mParam.put("data", object.toString());
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParam;
	}
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		Map header = response.headers;
		String cookie = (String) header.get("Set-Cookie");
		Config.getInstance().setCookie(cookie);
		AppController.saveCookie(SimiManager.getIntance().getCurrentContext(), cookie, url);
		return super.parseNetworkResponse(response);
	}
}

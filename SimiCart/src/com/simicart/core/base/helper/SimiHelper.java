package com.simicart.core.base.helper;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class SimiHelper {
	
	public static String endCodeJson(List<NameValuePair> pair)
			throws JSONException {
		int total = pair.size();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < total; i++) {
			obj.put(pair.get(i).getName(), pair.get(i).getValue());
		}
		return obj.toString();
	}
}

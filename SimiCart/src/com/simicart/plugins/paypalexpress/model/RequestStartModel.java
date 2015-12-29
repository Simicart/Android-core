package com.simicart.plugins.paypalexpress.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

public class RequestStartModel extends SimiModel {
	String url;
	String review_address;

	public String getUrl() {
		return url;
	}

	public String getReview_address() {
		return review_address;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			JSONObject jsonObject = list.getJSONObject(0);
			url = jsonObject.getString("url");
			review_address = jsonObject.getString("review_address");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "paypalexpress/api/start";
	}
}

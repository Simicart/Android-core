package com.simicart.plugins.mobileanalytics.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

public class GaIDModel extends SimiModel {

	String ga_id;

	public String getGa_id() {
		return ga_id;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			if (null != list && list.length() > 0) {
				JSONObject json = list.getJSONObject(0);
				ga_id = json.getString("ga_id");
			}
		} catch (JSONException e) {

		}
	}

	@Override
	protected void setShowNotifi() {
		this.isShowNotify = false;
	}

	@Override
	protected void setUrlAction() {
		url_action = "manalytics/api/get_ga_id";
	}
}
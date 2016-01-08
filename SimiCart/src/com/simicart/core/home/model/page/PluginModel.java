package com.simicart.core.home.model.page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.simicart.core.config.Constants;
import com.simicart.core.event.base.EventListener;

public class PluginModel {
	private Context mContext;

	public void setData(JSONObject jobs) {
		JSONArray dataOaj;
		try {
			dataOaj = jobs.getJSONArray("plugin");
			for (int i = 0; i < dataOaj.length(); i++) {
				EventListener.setEvent(dataOaj.getJSONObject(i).getString(
						Constants.SKU));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setContext(Context context) {
		this.mContext = context;
	}
}

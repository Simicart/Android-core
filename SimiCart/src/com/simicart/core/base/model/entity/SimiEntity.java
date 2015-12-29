package com.simicart.core.base.model.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class SimiEntity {

	protected JSONObject mJSON;
	protected Bundle bundle = new Bundle();
	
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public Bundle getBundle() {
		return bundle;
	}

	public void setJSONObject(JSONObject json) {
		this.mJSON = json;
	}

	public JSONObject getJSONObject() {
		return mJSON;
	}

	public String getData(String key) {
		if (mJSON != null && mJSON.has(key)) {
			try {
				return this.mJSON.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

package com.simicart.core.catalog.category.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.config.Constants;


public class Sort {
	private int mID;
	private String mTitle;

	public boolean parse(JSONObject json) {
		try {
			if (json.has(Constants.ID)) {
				mID = json.getInt(Constants.ID);
			}
			if (json.has(Constants.TITLE)) {
				mTitle = json.getString(Constants.TITLE);
			}

			return true;
		} catch (JSONException e) {
			return false;
		}
	}

	public int getId() {
		return mID;
	}

	public void setId(int id) {
		this.mID = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

}

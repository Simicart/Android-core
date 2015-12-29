package com.simicart.theme.matrixtheme.home.entity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class OrderProduct extends SimiEntity {

	private ArrayList<String> mUrlImage;
	private String mSpotId;
	private String mSpotKey;
	private String mSpotName;
	private JSONObject mJsonData;

	public JSONObject getJsonData() {
		return mJsonData;
	}

	public OrderProduct(JSONObject json) {
		mJsonData = json;
		mUrlImage = new ArrayList<String>();
		try {
			parse(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parse(JSONObject json) throws JSONException {

		if (json.has(Constants.SPOT_ID)) {
			mSpotId = json.getString(Constants.SPOT_ID);
		}
		if (json.has(Constants.SPOT_KEY)) {
			mSpotKey = json.getString(Constants.SPOT_KEY);
		}
		if (json.has(Constants.SPOT_NAME)) {
			mSpotName = json.getString(Constants.SPOT_NAME);
		}
		if (json.has(Constants.IMAGES)) {
			String data = json.getString(Constants.IMAGES);
			data = data.replace("[", "");
			data = data.replace("]", "");
			String[] arr = data.split(",");
			for (String string : arr) {
				string = string.replace("\"", "");
				string = string.replace("\\/", "/");
				Log.w("==================>", string);
				mUrlImage.add(string);
			}
		}

	}

	public ArrayList<String> getUrlImage() {
		return mUrlImage;
	}

	public void setUrlImage(ArrayList<String> mUrlImage) {
		this.mUrlImage = mUrlImage;
	}

	public String getSpotId() {
		return mSpotId;
	}

	public void setSpotId(String mSpotId) {
		this.mSpotId = mSpotId;
	}

	public String getSpotKey() {
		return mSpotKey;
	}

	public void setSpotKey(String mSpotKey) {
		this.mSpotKey = mSpotKey;
	}

	public String getSpotName() {
		return mSpotName;
	}

	public void setSpotName(String mSpotName) {
		this.mSpotName = mSpotName;
	}

}

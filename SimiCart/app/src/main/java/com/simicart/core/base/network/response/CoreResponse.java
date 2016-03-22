package com.simicart.core.base.network.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;

public class CoreResponse {
	protected String mStatus;
	protected String mMessage;
	protected String mData;
	protected JSONObject mJSON;

	public void setData(String data) {
		mData = data;
	}

	public void setDataJSON(JSONObject json) {
		mJSON = json;
	}

	public JSONObject getDataJSON() {
		return mJSON;
	}

	public boolean parse(String json) {
		mData = json;
		try {
			mJSON = new JSONObject(json);
			if (mJSON.has(Constants.STATUS)) {
				mStatus = mJSON.getString(Constants.STATUS);
			}
			if (mJSON.has(Constants.MESSAGE)) {
				JSONArray mes = mJSON.getJSONArray(Constants.MESSAGE);
				mMessage = mes.getString(0);
			}

			if (mStatus == null
					|| mStatus.equals(Constants.RESPONSE_STATUS_FAIL)) {
				return false;
			}

			return true;
		} catch (JSONException e) {
			return false;
		}

	}

	public boolean parse() {

		if (null == mData) {
			return false;
		}

		try {
			mJSON = null;
			mJSON = new JSONObject(mData);

			if (mJSON.has(Constants.STATUS)) {
				mStatus = mJSON.getString(Constants.STATUS);
			}
			if (mJSON.has(Constants.MESSAGE)) {
				JSONArray mes = mJSON.getJSONArray(Constants.MESSAGE);
				mMessage = mes.getString(0);
			}

			if (mStatus == null
					|| mStatus.equals(Constants.RESPONSE_STATUS_FAIL)) {
				return false;
			}

			return true;
		} catch (JSONException e) {
			return false;
		}
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getMessage() {
		if (mMessage == null) {
			mMessage = Config.getInstance().getText(
					"Some errors occurred. Please try again later");
		}
		return mMessage;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}
}

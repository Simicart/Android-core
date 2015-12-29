package com.simicart.plugins.download.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.download.entity.DownloadEntity;

public class DownloadModel extends SimiModel{
	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			Log.e("JSON Array", "" + list.length());
			collection = new SimiCollection();
			if (null != list && list.length() > 0) {
				for(int i=0; i<list.length(); i++) {
					JSONObject json = list.getJSONObject(i);
					DownloadEntity download = new DownloadEntity();
					download.setJSONObject(json);
					collection.addEntity(download);
				}
			}
		} catch (JSONException e) {

		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "connector/customer/get_download_products";
	}
}

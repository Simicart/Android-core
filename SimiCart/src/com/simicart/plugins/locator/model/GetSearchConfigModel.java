package com.simicart.plugins.locator.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

import android.util.Log;

public class GetSearchConfigModel extends SimiModel {
	
	protected ArrayList<String> listConfigs;
	
	public ArrayList<String> getConfigs() {
		return listConfigs;
	}

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		Log.e("GetSearchConfigModel", mJSON.toString());
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			listConfigs = new ArrayList<>();
			for (int i = 0; i < list.length(); i++) {
				String tag = list.getString(i);
				listConfigs.add(tag);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		// TODO Auto-generated method stub
		url_action = "storelocator/api/get_search_config";
	}

	@Override
	protected void setEnableCache() {
		// TODO Auto-generated method stub
		enableCache = true;
	}

}

package com.simicart.plugins.locator.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.locator.entity.StoreObject;

import android.util.Log;

public class ModelLocator extends SimiModel {

	@Override
	protected void setUrlAction() {
		url_action = "storelocator/api/get_store_list";
	}

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		Log.e("ModelLocator", mJSON.toString());
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			Gson gson = new GsonBuilder().create();
			//list_store_object.clear();
			for (int i = 0; i < list.length(); i++) {
				StoreObject storeObject = gson.fromJson(list
						.getJSONObject(i).toString(),
						StoreObject.class);
				collection.addEntity(storeObject);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

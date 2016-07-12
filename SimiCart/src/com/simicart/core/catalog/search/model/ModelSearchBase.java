package com.simicart.core.catalog.search.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;

public class ModelSearchBase extends SimiModel {

	protected ArrayList<String> mListId = new ArrayList<String>();

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			if (null == collection) {
				collection = new SimiCollection();
			}
			collection.setJSON(mJSON);
			for (int i = 0; i < list.length(); i++) {
				Product product = new Product();
				product.setJSONObject(list.getJSONObject(i));
				collection.addEntity(product);
				mListId.add(product.getId());
			}

		} catch (JSONException e) {
			Log.e("ModelSearchBase ", "paserData Exception " + e.getMessage());

		}
	}

	public void setUrlSearch(String url) {
		url_action = url;
		// this.enableCache = false;
	}
	
	public ArrayList<String> getListId(){
		return mListId;
	}
}

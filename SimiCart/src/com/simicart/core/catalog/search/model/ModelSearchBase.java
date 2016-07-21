package com.simicart.core.catalog.search.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;

public class ModelSearchBase extends SimiModel {

	protected ArrayList<String> mListId;

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
				// mListId.add(product.getId());
			}

			if (mJSON.has("other") && null == mListId) {
				JSONArray array = mJSON.getJSONArray("other");
				if (null != array && array.length() > 0) {
					mListId = new ArrayList<String>();
					parseListID(array);
				}
			}

		} catch (JSONException e) {
			Log.e("ModelSearchBase ", "paserData Exception " + e.getMessage());

		}
	}

	protected void parseListID(JSONArray array) throws JSONException {
		JSONObject json = array.getJSONObject(0);
		if (json.has("product_id_array")) {
			JSONArray arrayID = json.getJSONArray("product_id_array");
			if (null != arrayID && arrayID.length() > 0) {
				for (int i = 0; i < arrayID.length(); i++) {
					String id = arrayID.getString(i);
					mListId.add(id);
				}
			}
		}

	}

	public void setUrlSearch(String url) {
		url_action = url;
	}

	public ArrayList<String> getListId() {
		return mListId;
	}
}

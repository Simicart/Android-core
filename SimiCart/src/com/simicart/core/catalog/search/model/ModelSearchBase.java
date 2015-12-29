package com.simicart.core.catalog.search.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;

public class ModelSearchBase extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			System.out.println(mJSON);
			if (null == collection) {
				collection = new SimiCollection();
			}
			// else{
			// collection.getCollection().clear();
			// }
			collection.setJSON(mJSON);
			for (int i = 0; i < list.length(); i++) {
				Product product = new Product();
				product.setJSONObject(list.getJSONObject(i));
				collection.addEntity(product);
			}

		} catch (JSONException e) {

		}
	}

	public void setUrlSearch(String url) {
		url_action = url;
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

}

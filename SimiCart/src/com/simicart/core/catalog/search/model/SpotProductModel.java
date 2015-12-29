package com.simicart.core.catalog.search.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;

public class SpotProductModel extends SimiModel {

	@Override
	protected void setUrlAction() {
		url_action = "themeone/api/get_spot_products";
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			if (null == collection) {
				collection = new SimiCollection();
			}
			collection.getCollection().clear();
			for (int i = 0; i < list.length(); i++) {
				Product product = new Product();
				product.setJSONObject(list.getJSONObject(i));
				collection.addEntity(product);
			}

		} catch (JSONException e) {

		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

package com.simicart.core.catalog.product.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;

public class RelatedProductModel extends SimiModel {

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_RELATED_PRODUCTS;
	}

	public void paserData() {
		try {
			JSONArray list = this.getDataJSON().getJSONArray("data");
			collection = new SimiCollection();
			if (null != list && list.length() > 0) {
				for (int i = 0; i < list.length(); i++) {
					Product product = new Product();
					product.setJSONObject(list.getJSONObject(i));
					collection.addEntity(product);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

}

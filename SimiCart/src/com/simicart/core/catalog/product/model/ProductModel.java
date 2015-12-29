package com.simicart.core.catalog.product.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;

public class ProductModel extends SimiModel {

	public ProductModel() {
		super();
	}

	@Override
	protected void setUrlAction() {
		this.url_action = Constants.GET_PRODUCT_DETAIL;
	}

	public void paserData() {
		try {
			JSONArray list = this.getDataJSON().getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			if (null != list && list.length() > 0) {
				Product products = new Product();
				products.setJSONObject(list.getJSONObject(0));
				collection.addEntity(products);
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

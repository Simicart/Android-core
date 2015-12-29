package com.simicart.core.catalog.category.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Constants;

public class CategoryModel extends SimiModel {

	protected String mID;

	public void setCategoryID(String id) {
		mID = id;
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_CATEGORY_LIST;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			// if (Config.getInstance().isShow_link_all_product()) {
			// Category shop_category = new Category();
			// shop_category.setCategoryId(mID);
			// shop_category.setCategoryName(Config.getInstance().getText(
			// "All products"));
			// shop_category.setChild(false);
			// collection.addEntity(shop_category);
			// }
			for (int i = 0; i < list.length(); i++) {
				Category category = new Category();
				category.setJSONObject(list.getJSONObject(i));
				collection.addEntity(category);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

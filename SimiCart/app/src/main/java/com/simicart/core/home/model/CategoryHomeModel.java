package com.simicart.core.home.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Constants;

public class CategoryHomeModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
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
	protected void setUrlAction() {
		url_action = Constants.GET_CATEGORY_HOME;
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

	@Override
	protected void setShowNotifi() {
		super.setShowNotifi();
		isShowNotify = false;
	}
}

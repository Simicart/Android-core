package com.simicart.theme.ztheme.home.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.theme.ztheme.home.entity.CategoryZTheme;

public class CategoryTreeZThemeModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray array = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			if (null != array && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					CategoryZTheme categoryZTheme = new CategoryZTheme();
					categoryZTheme.setJSONObject(object);
					collection.addEntity(categoryZTheme);
				}
			}
		} catch (JSONException e) {
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "ztheme/api/get_category_tree";
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

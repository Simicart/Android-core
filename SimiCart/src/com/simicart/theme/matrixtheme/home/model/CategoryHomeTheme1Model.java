package com.simicart.theme.matrixtheme.home.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.theme.matrixtheme.home.entity.Theme1Category;

public class CategoryHomeTheme1Model extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");

			if (null == collection) {
				collection = new SimiCollection();
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject data = list.getJSONObject(i);
				Theme1Category theme1Category = new Theme1Category(data);
				collection.addEntity(theme1Category);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "themeone/api/get_order_categories";
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

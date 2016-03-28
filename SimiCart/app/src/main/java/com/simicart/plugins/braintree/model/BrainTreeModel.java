package com.simicart.plugins.braintree.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

public class BrainTreeModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray js_data = this.mJSON.getJSONArray("data");
			if (null == collection) {
				collection = new SimiCollection();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "simibraintree/index/update_payment";
	}
}

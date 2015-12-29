package com.simicart.plugins.paypalexpress.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.ShippingMethod;

public class RequestUpdateAddressModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray jArray = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jSON = jArray.getJSONObject(i);
				ShippingMethod shippingMethod = new ShippingMethod();
				shippingMethod.setJSONObject(jSON);
				collection.addEntity(shippingMethod);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "paypalexpress/api/update_address";
	}

}

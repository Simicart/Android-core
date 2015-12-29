package com.simicart.core.customer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.MyAddress;

public class NewAddressBookModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();

			if (null != list && list.length() > 0) {
				JSONObject json = list.getJSONObject(0);
				if (null != json) {
					MyAddress address = new MyAddress();
					address.setJSONObject(json);
					collection.addEntity(address);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.SAVE_ADDRESS;
	}
}

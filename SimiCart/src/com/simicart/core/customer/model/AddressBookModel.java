package com.simicart.core.customer.model;

import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.MyAddress;

public class AddressBookModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < list.length(); i++) {
				MyAddress address = new MyAddress();
				address.setJSONObject(list.getJSONObject(i));
				collection.addEntity(address);
			}
			Collections.reverse(collection.getCollection());
			ConfigCheckout.getInstance().setCollectionAddressBook(collection);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_USER_ADDRESS;
	}
}

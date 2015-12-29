package com.simicart.plugins.instantcontact.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

public class ContactUsModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			if (null != list && list.length() > 0) {
				JSONObject json = list.getJSONObject(0);
				ContactUsEntity contact = new ContactUsEntity();
				contact.setJSONObject(json);
				System.out.println(json);
				collection.addEntity(contact);
			}
		} catch (JSONException e) {

		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "simicontact/api/get_contacts";
	}

}

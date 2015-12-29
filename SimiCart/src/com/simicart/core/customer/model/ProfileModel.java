package com.simicart.core.customer.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.ProfileEntity;

public class ProfileModel extends SimiModel {

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < list.length(); i++) {
				ProfileEntity profile = new ProfileEntity();
				profile.setJSONObject(list.getJSONObject(i));
				collection.addEntity(profile);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_PROFILE;
	}

}

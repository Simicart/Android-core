package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class EditProfileModel extends SimiModel {

	@Override
	protected void paserData() {
		// try {
		// JSONArray list = this.mJSON.getJSONArray("data");
		// collection = new SimiCollection();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.CHANGE_USER;
	}

}
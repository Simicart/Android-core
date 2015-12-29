package com.simicart.core.notification.model;

import com.simicart.core.base.model.SimiModel;

public class RegisterIDModel extends SimiModel {

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void paserData() {
		// try {
		// JSONArray list = this.mJSON.getJSONArray("data");
		//
		// if (null == collection) {
		// collection = new SimiCollection();
		// }
		// for (int i = 0; i < list.length(); i++) {
		// Product product = new Product();
		// product.setJSONObject(list.getJSONObject(i));
		// collection.addEntity(product);
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		super.paserData();
	}

	@Override
	protected void setUrlAction() {
		url_action = "connector/config/register_device";
	}
}

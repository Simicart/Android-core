package com.simicart.plugins.two.checkout.model;

import com.simicart.core.base.model.SimiModel;

public class UpdatePaymentModel extends SimiModel {

	public void setUrlAction(String url) {
		url_action = url;
	}

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
	}
}

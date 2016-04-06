package com.simicart.core.notification.model;

import com.simicart.core.base.model.SimiModel;

public class RegisterIDModel extends SimiModel {

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void paserData() {
		super.paserData();
	}

	@Override
	protected void setUrlAction() {
		url_action = "connector/config/register_device";
	}
}

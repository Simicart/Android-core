package com.simicart.plugins.locator.model;

import com.simicart.core.base.model.SimiModel;

public class ModelLocator extends SimiModel {

	@Override
	protected void setUrlAction() {
		// TODO Auto-generated method stub
		super.setUrlAction();
		url_action = "storelocator/api/get_store_list";
	}

}

package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class PluginModel extends SimiModel {

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_LIST_PLUGIN;
	}

}

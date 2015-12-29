package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class CurrencyModel extends SimiModel {
	@Override
	protected void paserData() {
		super.paserData();
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_CURRENCY;
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

package com.simicart.core.splashscreen.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class StoreModel extends SimiModel {

	@Override
	protected void paserData() {
		
		super.paserData();
		
		Log.e("StoreModel ",mJSON.toString());
		
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_STORES;
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

}

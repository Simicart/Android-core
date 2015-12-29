package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class SignOutModel extends SimiModel {

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
		DataLocal.dataJson.clear();
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.SIGN_OUT;
	}

}

package com.simicart.plugins.facebooklogin;

import com.simicart.core.customer.model.SignInModel;

public class FacebookModel extends SignInModel {

	@Override
	protected void setUrlAction() {
		url_action = "simifblogin/index/login";
	}
}

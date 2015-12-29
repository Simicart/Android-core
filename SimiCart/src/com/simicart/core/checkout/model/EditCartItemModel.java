package com.simicart.core.checkout.model;

import com.simicart.core.config.Constants;

public class EditCartItemModel extends CartModel {

	@Override
	protected void setUrlAction() {
		url_action = Constants.EDIT_CART;
	}

}

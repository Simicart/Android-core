package com.simicart.core.catalog.product.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class AddToCartModel extends SimiModel {

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.ADD_TO_CART;
	}

}

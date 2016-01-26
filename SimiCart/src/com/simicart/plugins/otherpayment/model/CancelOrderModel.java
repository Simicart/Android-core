package com.simicart.plugins.otherpayment.model;

import com.simicart.core.base.model.SimiModel;

public class CancelOrderModel extends SimiModel{
	@Override
	protected void setUrlAction() {
		url_action = "connector/checkout/cancel_order";
	}
	
	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
	}
}

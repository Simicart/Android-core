package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

public class OrderHistoryReOrderModel extends SimiModel{
	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
	}
	
	@Override
	protected void setUrlAction() {
		url_action = Constants.ORDER_DETAIL_REORDER;
	}
}

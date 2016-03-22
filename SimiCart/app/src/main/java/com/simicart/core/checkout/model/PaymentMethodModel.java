package com.simicart.core.checkout.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

public class PaymentMethodModel extends SimiModel{
	protected TotalPrice mTotalPrice;
	
	public TotalPrice getTotalPrice() {
		return mTotalPrice;
	}
	
	@Override
	protected void paserData() {
		try {
			JSONArray data = this.mJSON.getJSONArray("data");
			if (null != data && data.length() > 0) {
				JSONObject jsData = data.getJSONObject(0);
				if (jsData.has(Constants.FEE)) {
					JSONObject jsTotal = jsData.getJSONObject(Constants.FEE);
					mTotalPrice = new TotalPrice();
					mTotalPrice.setJSONObject(jsTotal);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	protected void setUrlAction() {
		url_action = Constants.SAVE_PAYMENT_METHOD;
	}
}

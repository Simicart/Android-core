package com.simicart.plugins.paypalexpress.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.customer.entity.MyAddress;

public class RequestAddressModel extends SimiModel {

	private MyAddress shippingAddress = new MyAddress();
	private MyAddress billingAddress = new MyAddress();

	public MyAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(MyAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public MyAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(MyAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray array = this.mJSON.getJSONArray("data");
			if (null != array && array.length() > 0) {
				JSONObject jsonObject = array.getJSONObject(0);
				if (jsonObject.has("shipping_address")) {
					JSONObject shippingAd = jsonObject
							.getJSONObject("shipping_address");
					shippingAddress.setJSONObject(shippingAd);
				}
				if (jsonObject.has("billing_address")) {
					JSONObject billingAd = jsonObject
							.getJSONObject("billing_address");
					billingAddress.setJSONObject(billingAd);
				}
			}

		} catch (JSONException e) {
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "paypalexpress/api/review_address";
	}

}

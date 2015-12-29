package com.simicart.core.checkout.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

public class ShippingMethodModel extends SimiModel {

	protected ArrayList<PaymentMethod> mPaymentMethod;
	protected ArrayList<ShippingMethod> mShippingMethod;
	protected TotalPrice mTotalPrice;

	public ArrayList<PaymentMethod> getListPayment() {
		return mPaymentMethod;
	}

	public ArrayList<ShippingMethod> getShippingMethods() {
		return mShippingMethod;
	}

	public TotalPrice getTotalPrice() {
		return mTotalPrice;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray data = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			if (null != data && data.length() > 0) {
				JSONObject jsData = data.getJSONObject(0);
				if (jsData.has(Constants.FEE)) {
					JSONObject jsTotal = jsData.getJSONObject(Constants.FEE);
					mTotalPrice = new TotalPrice();
					mTotalPrice.setJSONObject(jsTotal);
				}

				if (jsData.has(Constants.PAYMENT_METHOD_LIST)) {
					JSONArray arrayMethod = jsData
							.getJSONArray(Constants.PAYMENT_METHOD_LIST);
					mPaymentMethod = new ArrayList<PaymentMethod>();
					for (int i = 0; i < arrayMethod.length(); i++) {
						JSONObject json = arrayMethod.getJSONObject(i);
						PaymentMethod paymentMethod = new PaymentMethod();
						paymentMethod.setJSONObject(json);
						mPaymentMethod.add(paymentMethod);
					}
				}

				if (jsData.has(Constants.SHIPPING_METHOD_LIST)) {
					JSONArray arrayMethod = jsData
							.getJSONArray(Constants.SHIPPING_METHOD_LIST);
					mShippingMethod = new ArrayList<ShippingMethod>();
					for (int i = 0; i < arrayMethod.length(); i++) {
						JSONObject json = arrayMethod.getJSONObject(i);
						ShippingMethod shippingMethod = new ShippingMethod();
						shippingMethod.setJSONObject(json);
						mShippingMethod.add(shippingMethod);
					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void parsePaymentMethod(JSONArray array) {

	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.SAVE_SHIPPING_METHOD;
	}

}

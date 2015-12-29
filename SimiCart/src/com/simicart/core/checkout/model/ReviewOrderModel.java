package com.simicart.core.checkout.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.Condition;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

public class ReviewOrderModel extends SimiModel {

	protected ArrayList<ShippingMethod> shippingMethods = new ArrayList<>();
	protected ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
	protected ArrayList<Condition> conditions = new ArrayList<>();
	protected TotalPrice totalPrice = new TotalPrice();

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_ORDER_CONFIG;
	}

	@Override
	protected void paserData() {
		try {

			JSONArray js_data = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			JSONObject js_order = js_data.getJSONObject(0);

			// shipping method
			if (js_order.has(Constants.SHIPPING_METHOD_LIST)) {
				JSONArray js_shipping_method = js_order
						.getJSONArray(Constants.SHIPPING_METHOD_LIST);
				for (int i = 0; i < js_shipping_method.length(); i++) {
					ShippingMethod shippingMethod = new ShippingMethod();
					shippingMethod.setJSONObject(js_shipping_method
							.getJSONObject(i));
					shippingMethods.add(shippingMethod);
				}
			}

			// payment method
			if (js_order.has(Constants.PAYMENT_METHOD_LIST)) {
				JSONArray js_payment_method = js_order
						.getJSONArray(Constants.PAYMENT_METHOD_LIST);
				for (int i = 0; i < js_payment_method.length(); i++) {
					PaymentMethod paymentMethod = new PaymentMethod();
					paymentMethod.setJSONObject(js_payment_method
							.getJSONObject(i));
					paymentMethods.add(paymentMethod);
				}
			}

			if (js_order.has(Constants.FEE)) {
				JSONObject js_fee = js_order.getJSONObject(Constants.FEE);
				totalPrice.setJSONObject(js_fee);

				// term & condition
				if (js_fee.has(Constants.CONDITION)) {
					JSONArray js_condition = js_fee
							.getJSONArray(Constants.CONDITION);
					for (int i = 0; i < js_condition.length(); i++) {
						Condition condition = new Condition();
						condition.setJSONObject(js_condition.getJSONObject(i));
						conditions.add(condition);
					}
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	public ArrayList<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(ArrayList<ShippingMethod> shippingMethod) {
		shippingMethods = shippingMethod;
	}

	public ArrayList<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public TotalPrice getTotalPrice() {
		return totalPrice;
	}

	public ArrayList<Condition> getConditions() {
		return conditions;
	}

}

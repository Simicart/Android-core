package com.simicart.plugins.checkout.com;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.checkout.com.view.FragmentCheckoutCom;

public class CheckoutCom {

	String LIVE_URL = "https://secure.checkout.com/hpayment-tokenretry/pay.aspx?";

	Context context;
	String invoice_number;
	String data;

	public CheckoutCom(String method, CheckoutData checkoutData) {
		Log.e(getClass().getName(), "Method: " + method);
		this.context = MainActivity.context;
		this.invoice_number = checkoutData.getInvoice_number();
		JSONObject js_placeOrder = checkoutData.getJsonPlaceOrder();
		if (js_placeOrder.has("params")) {
			try {
				this.data = js_placeOrder.getString("params");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (method.equals("onCheckOut")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("SIMICHECKOUTCOM")) {
				this.onCheckOut(checkoutData.getPaymentMethod());
			}
		}
	}

	private void onCheckOut(PaymentMethod paymentMethod) {
		FragmentCheckoutCom fragment = FragmentCheckoutCom.newInstance(data,
				LIVE_URL);
		fragment.setUrl_action(paymentMethod.getData("url_action"));
		fragment.setUrl_back(paymentMethod.getData("url_back"));
		fragment.setData(data);
		fragment.setInvoice_number(this.invoice_number);
		SimiManager.getIntance().addFragment(fragment);
	}
}

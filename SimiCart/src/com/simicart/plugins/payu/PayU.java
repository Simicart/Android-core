package com.simicart.plugins.payu;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.payu.fragment.PayUFragment;

@SuppressLint("DefaultLocale")
public class PayU {

	public PayU(String method, CheckoutData checkoutData) {
		Log.e("PayU ", "001");
		if (method.equals("onCheckOut")) {
			Log.e("PayU ", "002");
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("SIMIPAYU")) {
				Log.e("PayU ", "003");
				onCheckOut(checkoutData);
			}
		}
	}

	private void onCheckOut(CheckoutData checkoutData) {
		try {
			Log.e("PayU ", "004");
			if (checkoutData.getJsonPlaceOrder().has("params")) {
				Log.e("PayU ", "005");
				String url_payu = checkoutData.getJsonPlaceOrder().getString(
						"params");
				if (Utils.validateString(url_payu)) {
					Log.e("PayU ", "006");
					PayUFragment fragment = PayUFragment.newInstance();
					fragment.setUrl_payu(url_payu);
					fragment.setInvoice_number(checkoutData.getInvoice_number());
					SimiManager.getIntance().addFragment(fragment);
				}
				Log.e("PayU ", "007");
			}
		} catch (JSONException e) {
			Log.e("PayU  onCheckOut Exception ", e.getMessage());
			e.printStackTrace();
		}

	}
}

package com.simicart.plugins.payuindia;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.payuindia.fragment.FragmentPayU;

public class PayU {
	Context context;

	@SuppressLint("DefaultLocale")
	public PayU(String method, CheckoutData checkoutData) {
		this.context = MainActivity.context;
		if (method.equals("onCheckOut")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("SIMIPAYUINDIA")) {
				this.onCheckOut(checkoutData);
			}
		}
	}

	private void onCheckOut(CheckoutData checkoutData) {
		String url = "";
		try {
			if (checkoutData.getJsonPlaceOrder().has("url_action")) {
				url = checkoutData.getJsonPlaceOrder().getString("url_action");
				
				Log.e("PayU URL " , url);
			}
		} catch (JSONException e) {
			Log.e("PayU JSONException ", e.getMessage());
		}

		FragmentPayU fragmentPayU = FragmentPayU.newInstance();
		fragmentPayU.setUrl(url);
		fragmentPayU.setInvoice_number(checkoutData.getInvoice_number());
		SimiManager.getIntance().addPopupFragment(fragmentPayU);
	}
}

package com.simicart.plugins.ccavenue;

import org.json.JSONException;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.ccavenue.fragment.FragmentCCAvenue;

public class CCAvenue {
//	Context context;

	public CCAvenue(String method, CheckoutData checkoutData) {
//		this.context = MainActivity.context;
		if (method.equals("onCheckOut")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("SIMIAVENUE")) {
				this.onCheckOut(checkoutData);
			}
		}
	}

	private void onCheckOut(CheckoutData checkoutData) {
		String data = "";
		try {
			if (checkoutData.getJsonPlaceOrder().has("params")) {
				data = checkoutData.getJsonPlaceOrder().getString("params");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FragmentCCAvenue fragment = FragmentCCAvenue.newInstance();
		fragment.setUrl(data);
		fragment.setInvoice_number(checkoutData.getInvoice_number());
		SimiManager.getIntance().addFragment(fragment);
	}
}

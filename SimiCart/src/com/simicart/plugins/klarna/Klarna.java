package com.simicart.plugins.klarna;

import android.annotation.SuppressLint;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;

public class Klarna {

	@SuppressLint("DefaultLocale")
	public Klarna(String method, final CheckoutData checkoutData) {

		Log.e("Klarna ", "Mehthod " + method);

		if (method.equals("onKlarna")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("SIMIKLARNA")) {
				Log.e("Klarna ", "contructor 001");
				checkoutData.setContructed(true);
				onCheckOut();
			}
		}
	}

	protected void onCheckOut() {
		Log.e("Klarna", "onCheckout");
		KlarnaFragment fragment = new KlarnaFragment();
		SimiManager.getIntance().replacePopupFragment(fragment);
	}

}

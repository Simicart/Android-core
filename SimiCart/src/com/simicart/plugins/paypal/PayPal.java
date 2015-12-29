package com.simicart.plugins.paypal;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.event.checkout.CheckoutData;

public class PayPal {
	Context context;

	public PayPal(Context context, String method,
			ArrayList<PaymentMethod> paymentList) {
		this.context = context;
		if (method.equals("refreshList")) {
			this.refreshList(paymentList);
		}
	}

	public PayPal(String method, CheckoutData checkoutData) {
		this.context = MainActivity.context;
		Log.e("Paypal", "Paypal" + method);
		if (method.equals("callPayPalServer")) {
			this.callPayPalServer(checkoutData.getPaymentMethod(),
					checkoutData.getInvoice_number(),
					checkoutData.getTotal_price());
		}
	}

	public void refreshList(ArrayList<PaymentMethod> paymentList) {
		int i = 0;
		int position = -1;

		for (PaymentMethod payment : paymentList) {
			if (payment.getPayment_method().equals("PAYPAL_MOBILE")) {
				if (payment.getData("client_id").equals("null")
						|| payment.getData("client_id") == null
						|| payment.getData("client_id").equals("")) {
					position = i;
					break;
				}
			}
			i++;
		}
		if (position != -1) {
			paymentList.remove(position);
		}
	}

	public void callPayPalServer(PaymentMethod paymentMethod,
			String invoice_number, String total_price) {
		if (paymentMethod.getPayment_method().equals("PAYPAL_MOBILE")) {
			Intent intent = new Intent(this.context, PaypalSimicart.class);

			String client_id = paymentMethod.getData("client_id");
			intent.putExtra("EXTRA_CLIENT_ID", client_id);
			Log.e("PayPal CLIENT_ID " , client_id);

			String is_sandbox = paymentMethod.getData("is_sandbox");
			intent.putExtra("EXTRA_SANDBOX", is_sandbox);
			Log.e("PayPal IS_SANDBOX ", is_sandbox);
			
			
			intent.putExtra("EXTRA_PRICE", total_price);
			Log.e("PayPal EXTRA_PRICE",total_price);
			
			String bnCode = paymentMethod.getData("bncode");
			intent.putExtra("EXTRA_BNCODE", bnCode);
			Log.e("PayPal BNCODE ", bnCode);
			
			
			intent.putExtra("EXTRA_INVOICE_NUMBER", invoice_number);
			Log.e("PayPal INVOICE NUMBER ", invoice_number);
			this.context.startActivity(intent);
		} else {
			return;
		}

	}
}

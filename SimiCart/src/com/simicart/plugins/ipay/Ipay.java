package com.simicart.plugins.ipay;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;

import com.simicart.MainActivity;
import com.simicart.core.event.checkout.CheckoutData;

public class Ipay {
	Context context;
	
	
	public Ipay(String method, CheckoutData checkoutData){
		this.context = MainActivity.context;
		if (method.equals("callIpayServer")) {
			this.callIpayServer(checkoutData);
		}
	}
	
	public void callIpayServer(CheckoutData checkoutData){
		String merchant_key = "";
		String merchant_code = "";
		String is_sandbox = "";
		String currency_code = "";
		String product_des = "";
		String name = "";
		String email = "";
		String contact = "";
		String country = "";
		String amount = "";
		try {
			merchant_key = checkoutData.getPaymentMethod().getData("merchant_key");
			merchant_code = checkoutData.getPaymentMethod().getData("merchant_code");
			is_sandbox = checkoutData.getPaymentMethod().getData("is_sandbox");
			if(checkoutData.getJsonPlaceOrder().has("currency_code")){
				currency_code = checkoutData.getJsonPlaceOrder().getString("currency_code");
			}
			if(checkoutData.getJsonPlaceOrder().has("product_des")){
				product_des = checkoutData.getJsonPlaceOrder().getString("product_des");
			}
			if(checkoutData.getJsonPlaceOrder().has("name")){
				name = checkoutData.getJsonPlaceOrder().getString("name");
			}
			if(checkoutData.getJsonPlaceOrder().has("email")){
				email = checkoutData.getJsonPlaceOrder().getString("email");
			}
			if(checkoutData.getJsonPlaceOrder().has("contact")){
				contact = checkoutData.getJsonPlaceOrder().getString("contact");
			}
			if(checkoutData.getJsonPlaceOrder().has("country_id")){
				country = checkoutData.getJsonPlaceOrder().getString("country_id");
			}
			if(checkoutData.getJsonPlaceOrder().has("amount")){
				amount = checkoutData.getJsonPlaceOrder().getString("amount");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(checkoutData.getPaymentMethod().getPayment_method().equals("SIMIIPAY88")){
			Intent ipay = new Intent(context, IpaySimiCart.class);
			ipay.putExtra("EXTRA_MECHANT_KEY", merchant_key);
			ipay.putExtra("EXTRA_MECHANT_CODE", merchant_code);
			ipay.putExtra("EXTRA_CUREENTCY", currency_code);
			ipay.putExtra("EXTRA_PRODUCTDES", product_des);
			ipay.putExtra("EXTRA_NAME", name);
			ipay.putExtra("EXTRA_EMAIL", email);
			ipay.putExtra("EXTRA_CONTACT", contact);
			ipay.putExtra("EXTRA_COUNTRY", country);
			ipay.putExtra("EXTRA_INVOICE", checkoutData.getInvoice_number());
			ipay.putExtra("EXTRA_AMOUNT", amount);
			ipay.putExtra("EXTRA_SANDBOX", is_sandbox);
//			DrawableManager.bitMap_Map.clear();
			context.startActivity(ipay);
		}
	}
}

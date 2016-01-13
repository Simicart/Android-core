package com.simicart.plugins.two.checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.event.checkout.CheckoutData;

public class Checkout {
	Context context;
	String LIVE_URL = "https://www.2checkout.com/checkout/purchase?";
	String SANBOX_URL = "https://sandbox.2checkout.com/checkout/purchase?";
	// "merchant_order_id"
	JSONObject post_params;
	String data;
	String total;
	String invoice_number;

	public Checkout(String method, CheckoutData checkoutData) {
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
		if (method.equals("pay")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("TWOUT")) {
				this.pay(checkoutData.getPaymentMethod());
			}
		}
	}

	public void pay(PaymentMethod method) {
		if (method.getData("is_sandbox").equals("1")) {
			FragmentWeb fragment = FragmentWeb.newInstance(data, SANBOX_URL);
			fragment.setUrlAction(method.getData("url_action"));
			fragment.setUrlBack(method.getData("url_back"));
			fragment.setData(data);
			fragment.setInvoiceNumber(this.invoice_number);
			SimiManager.getIntance().addFragment(fragment);
		} else {
			FragmentWeb fragment = FragmentWeb.newInstance(data, LIVE_URL);
			fragment.setUrlAction(method.getData("url_action"));
			// this.parseData();
			fragment.setUrlBack(method.getData("url_back"));
			fragment.setData(data);
			fragment.setInvoiceNumber(this.invoice_number);
			SimiManager.getIntance().addFragment(fragment);
		}
	}

	public void setParamsToPost(String key, String value, int position) {
		if (position != 0) {
			this.data += "&" + key + "=" + value;
		} else {
			this.data = key + "=" + value;
		}
	}

	public void formatData(JSONArray _data, boolean check) {
		if (check) {
			for (int i = 0; i < _data.length(); i++) {
				JSONObject jb;
				String key = "";
				String value = "";
				try {
					jb = _data.getJSONObject(i);
					key = jb.getString("key");
					value = jb.getString("value");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.setParamsToPost(key, value, i);
			}
		} else {
			for (int i = 0; i < _data.length(); i++) {
				JSONObject jb;
				String key = "";
				String value = "";
				try {
					jb = _data.getJSONObject(i);
					key = jb.getString("key");
					value = jb.getString("value");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				this.setParamsToPost(key, value, i);
			}
		}
	}

	public void parseData() {
		if (post_params.has("info")) {
			JSONArray info = new JSONArray();
			try {
				info = post_params.getJSONArray("info");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.formatData(info, true);
		}

		if (post_params.has("product")) {
			JSONArray product = new JSONArray();
			try {
				product = post_params.getJSONArray("product");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < product.length(); i++) {
				try {
					this.formatData(product.getJSONArray(i), false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		if (post_params.has("tax")) {
			JSONArray tax = new JSONArray();
			try {
				tax = post_params.getJSONArray("tax");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < tax.length(); i++) {
				try {
					this.formatData(tax.getJSONArray(i), false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

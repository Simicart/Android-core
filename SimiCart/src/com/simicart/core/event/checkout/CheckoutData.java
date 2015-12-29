package com.simicart.core.event.checkout;

import org.json.JSONObject;

import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.TotalPrice;

public class CheckoutData {
	private String invoice_number;
	private String total_price;
	private TotalPrice total_priceAll;
	// private JSONArray json_Data;
	private PaymentMethod paymentMethod;
	private int showtype;
	private JSONObject JsonPlaceOrder;
	private boolean isContructed = false;

	public boolean isContructed() {
		return isContructed;
	}

	public void setContructed(boolean isContructed) {
		this.isContructed = isContructed;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	// public JSONArray getJson_Data() {
	// return json_Data;
	// }
	//
	// public void setJson_Data(JSONArray json_Data) {
	// this.json_Data = json_Data;
	// }

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setTotal_price(TotalPrice mtotalPrice) {
		this.total_price = "0";
		this.total_priceAll = mtotalPrice;
		if (mtotalPrice.getGrand_total() != null
				&& !mtotalPrice.getGrand_total().equals("null")
				&& !mtotalPrice.getGrand_total().equals("")
				&& !mtotalPrice.getGrand_total().equals("0")) {
			this.total_price = mtotalPrice.getGrand_total();
			return;
		}
		if (mtotalPrice.getGrand_total_incl_tax() != null
				&& !mtotalPrice.getGrand_total_incl_tax().equals("null")
				&& !mtotalPrice.getGrand_total_incl_tax().equals("")
				&& !mtotalPrice.getGrand_total_incl_tax().equals("0")) {
			this.total_price = mtotalPrice.getGrand_total_incl_tax();
			return;
		}
		if (mtotalPrice.getGrand_total_excl_tax() != null
				&& !mtotalPrice.getGrand_total_excl_tax().equals("null")
				&& !mtotalPrice.getGrand_total_excl_tax().equals("")
				&& !mtotalPrice.getGrand_total_excl_tax().equals("0")) {
			this.total_price = mtotalPrice.getGrand_total_excl_tax();
			return;
		}
	}

	public TotalPrice getTotal_priceAll() {
		return total_priceAll;
	}

	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}

	public int getShowtype() {
		return showtype;
	}

	public void setJsonPlaceOrder(JSONObject js_placeOrder) {
		this.JsonPlaceOrder = js_placeOrder;
	}

	public JSONObject getJsonPlaceOrder() {
		return JsonPlaceOrder;
	}
}

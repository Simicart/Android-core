package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

public class OrderHisDetail extends SimiEntity {
	private String mID;
	private String mDate;
	private String mCode;
	private String mTotal;
	private String mSubtotal;
	private String mTax;
	private String mDiscount;
	private String mSfee;
	private String mGiftCode;
	private String mNote;
	private ArrayList<Cart> mItems;
	private String mPaymentMethod;
	private String mCard4digits;
	private String mShippingMethod;
	private ShippingAddress mShippingAddress;
	private BillingAddress mBillingAddress;
	private TotalPrice mTotalPrice;

	public ArrayList<Cart> getOrder_item() {
		if (null == mItems) {
			mItems = new ArrayList<Cart>();
			try {
				JSONArray array = new JSONArray(getData(Constants.ORDER_ITEMS));
				if (null != array && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Cart order = new Cart();
						order.setJSONObject(obj);
						mItems.add(order);
					}
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return mItems;
	}

	public TotalPrice getTotal_price() {
		if (null == mTotalPrice) {
			mTotalPrice = new TotalPrice();
			try {
				mTotalPrice.setJSONObject(new JSONObject(
						getData(Constants.TOTAL_V2)));
			} catch (JSONException e) {
				return null;
			}
		}
		return mTotalPrice;
	}

	public void setTotal_price(TotalPrice total_price) {
		this.mTotalPrice = total_price;
	}

	public void setOrder_item(ArrayList<Cart> order_item) {
		this.mItems = order_item;
	}

	public String getPayment_method() {
		if (null == mPaymentMethod) {
			mPaymentMethod = getData(Constants.PAYMENT_METHOD);
		}
		return mPaymentMethod;
	}

	public void setPayment_method(String payment_method) {
		this.mPaymentMethod = payment_method;
	}

	public String getCard_4digits() {
		if (null == mCard4digits) {
			mCard4digits = getData(Constants.CARD_4DIGITS);
		}
		return mCard4digits;
	}

	public void setCard_4digits(String card_4digits) {
		this.mCard4digits = card_4digits;
	}

	public String getShipping_method() {
		if (null == mShippingMethod) {
			mShippingMethod = getData(Constants.SHIPPING_METHOD);
		}

		return mShippingMethod;
	}

	public void setShipping_method(String shipping_method) {
		this.mShippingMethod = shipping_method;
	}

	public ShippingAddress getShipping_address() {
		if (null == mShippingAddress) {
			mShippingAddress = new ShippingAddress();
			try {

				String value = getData(Constants.SHIPPING_ADDRESS);
				if (null != value && !value.equals("") && !value.equals("null")) {

					mShippingAddress.setJSONObject(new JSONObject(
							getData(Constants.SHIPPING_ADDRESS)));
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return mShippingAddress;
	}

	public void setShipping_address(ShippingAddress shipping_address) {
		this.mShippingAddress = shipping_address;
	}

	public BillingAddress getBilling_address() {
		if (null == mBillingAddress) {
			mBillingAddress = new BillingAddress();
			try {
				mBillingAddress.setJSONObject(new JSONObject(
						getData(Constants.BILLING_ADDRESS)));
			} catch (JSONException e) {
				return null;
			}
		}
		return mBillingAddress;
	}

	public void setBilling_address(BillingAddress billing_address) {
		this.mBillingAddress = billing_address;
	}

	public String getOrder_id() {
		if (null == mID) {
			mID = getData(Constants.ORDER_ID);
		}
		return mID;
	}

	public void setOrder_id(String order_id) {
		this.mID = order_id;
	}

	public String getOrder_date() {
		if (null == mDate) {
			mDate = getData(Constants.ORDER_DATE);
		}

		return mDate;
	}

	public void setOrder_date(String order_date) {
		this.mDate = order_date;
	}

	public String getOrder_code() {
		if (null == mCode) {
			mCode = getData(Constants.ORDER_CODE);
		}
		return mCode;
	}

	public void setOrder_code(String order_code) {
		this.mCode = order_code;
	}

	public String getOrder_total() {
		if (null == mTotal) {
			mTotal = getData(Constants.ORDER_TOTAL);
		}
		return mTotal;
	}

	public void setOrder_total(String order_total) {
		this.mTotal = order_total;
	}

	public String getOrder_subtotal() {
		if (null == mSubtotal) {
			mSubtotal = getData(Constants.ORDER_SUBTOTAL);
		}
		return mSubtotal;
	}

	public void setOrder_subtotal(String order_subtotal) {
		this.mSubtotal = order_subtotal;
	}

	public String getDiscount() {
		if (null == mDiscount) {
			mDiscount = getData(Constants.DISCOUNT);
		}

		return mDiscount;
	}

	public void setDiscount(String discount) {
		this.mDiscount = discount;
	}

	public String getTax() {
		if (null == mTax) {
			mTax = getData(Constants.TAX);
		}
		return mTax;
	}

	public void setTax(String tax) {
		this.mTax = tax;
	}

	public String getS_fee() {
		if (null == mSfee) {
			mSfee = getData(Constants.S_FEE);
		}
		return mSfee;
	}

	public void setS_fee(String s_fee) {
		this.mSfee = s_fee;
	}

	public String getOrder_gift_code() {
		if (null == mGiftCode) {
			mGiftCode = getData(Constants.ORDER_GIFT_CODE);
		}
		return mGiftCode;
	}

	public void setOrder_gift_code(String order_gift_code) {
		this.mGiftCode = order_gift_code;
	}

	public String getOrder_note() {
		return mNote;
	}

	public void setOrder_note(String order_note) {
		this.mNote = order_note;
	}

}

package com.simicart.core.checkout.entity;

import java.io.Serializable;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class ShippingMethod extends SimiEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mID;
	private String mCode;
	private String mTitle;
	private String mFee;
	private String mFeeInclTax;
	private String mName;
	private String isSelected;
	static String code = "";
	static String title = "NONE";
	static String place_shipping_price = "0";

	public boolean isS_method_selected() {

		if (null == isSelected) {
			// isSelected = getData(Constants.METHOD_SELECTED);
            return false;
		}
		if (isSelected.equals("TRUE") || isSelected.equals("true")) {
			return true;
		}
		return false;
	}

	public void setS_method_selected(boolean s_method_selected) {
		this.isSelected = String.valueOf(s_method_selected);
	}

	public String getS_method_id() {
		if (mID == null) {
			mID = (getData(Constants.METHOD_ID));
		}
		return mID;
	}

	public void setS_method_id(String s_method_id) {
		this.mID = s_method_id;
	}

	public String getS_method_code() {
		if (null == mCode) {
			mCode = getData(Constants.METHOD_CODE);
		}
		return mCode;
	}

	public void setS_method_code(String s_method_code) {
		this.mCode = s_method_code;
	}

	public String getS_method_title() {
		if (null == mTitle) {
			mTitle = getData(Constants.METHOD_TITLE);
		}
		return mTitle;
	}

	public void setS_method_title(String s_method_title) {
		this.mTitle = s_method_title;
	}

	public String getS_method_fee_incl_tax() {

		if (null == mFeeInclTax) {
			mFeeInclTax = getData(Constants.METHOD_FEE_INCL_TAX);
		}
		return mFeeInclTax;
	}

	public void setS_method_fee_incl_tax(String s_method_fee_incl_tax) {
		this.mFeeInclTax = s_method_fee_incl_tax;
	}

	public String getS_method_fee() {
		if (null == mFee) {
			mFee = getData(Constants.METHOD_FEE);
		}
		return mFee;
	}

	public void setS_method_fee(String s_method_fee) {
		this.mFee = s_method_fee;
	}

	public String getS_method_name() {

		if (null == mName) {
			mName = getData(Constants.METHOD_NAME);
		}
		return mName;
	}

	public void setS_method_name(String s_method_name) {
		this.mName = s_method_name;
	}

	public static void setCode(String _code) {
		code = _code;
	}

	public static String getCode() {
		return code;
	}

	public static void setTitle(String _title) {
		title = _title;
	}

	public static String getTitle() {
		return title;
	}

	public static void setPlaceShippingPrice(String price) {
		place_shipping_price = price;
	}

	public static String getPlaceShippingPrice() {
		return place_shipping_price;
	}

	public static void refreshShipping() {
		code = "";
		title = "NONE";
		place_shipping_price = "0";
	}

}

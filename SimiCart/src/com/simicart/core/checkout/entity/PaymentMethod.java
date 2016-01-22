package com.simicart.core.checkout.entity;

import java.io.Serializable;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class PaymentMethod extends SimiEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mshowType = -1;
	private String mTitle;
	private String mContent;
	private String mPaymentMethod;

	// Data place order
	private String mCurrentMethod = "";
	private String mPlacePaymentMethod = "";
	private String mPlaceCCType = "";
	private String mPlaceCCNumber = "";
	private String mPlaceCCExpMonth = "";
	private String mPlaceCCExpYear = "";
	private String mPlaceCCId = "";

	private static PaymentMethod instance;
	private String mCheckPaymentMethod;

	public static PaymentMethod getInstance() {
		if (null == instance) {
			instance = new PaymentMethod();
		}
		return instance;
	}

	public int getShow_type() {
		if (mshowType < 0) {
			mshowType = Integer.parseInt(getData(Constants.SHOW_TYPE));
		}
		return mshowType;
	}

	public void setShow_type(int show_type) {
		this.mshowType = show_type;
	}

	public String getTitle() {
		if (null == mTitle) {
			mTitle = getData(Constants.TITLE);
		}

		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public String getPayment_method() {
		if (null == mPaymentMethod) {
			mPaymentMethod = getData(Constants.PAYMENT_METHOD);
		}
		return mPaymentMethod;
	}

	public String getCurrentMethod() {
		return mCurrentMethod;
	}

	public void setCurrentMethod(String currentMethod) {
		this.mCurrentMethod = currentMethod;
	}

	public void setPayment_method(String payment_method) {
		this.mPaymentMethod = payment_method;
	}

	public String getPlace_payment_method() {
		return mPlacePaymentMethod;
	}

	public void setPlace_payment_method(String place_payment_method) {
		this.mPlacePaymentMethod = place_payment_method;
	}

	public String getPlace_cc_type() {
		return mPlaceCCType;
	}

	public void setPlace_cc_type(String place_cc_type) {
		this.mPlaceCCType = place_cc_type;
	}

	public String getPlace_cc_number() {
		return mPlaceCCNumber;
	}

	public void setPlace_cc_number(String place_cc_number) {
		this.mPlaceCCNumber = place_cc_number;
	}

	public String getPlace_cc_exp_month() {
		return mPlaceCCExpMonth;
	}

	public void setPlace_cc_exp_month(String place_cc_exp_month) {
		this.mPlaceCCExpMonth = place_cc_exp_month;
	}

	public String getPlace_cc_exp_year() {
		return mPlaceCCExpYear;
	}

	public void setPlace_cc_exp_year(String place_cc_exp_year) {
		this.mPlaceCCExpYear = place_cc_exp_year;
	}

	public void setPlacePaymentMethod(String method) {
		mPlacePaymentMethod = method;
	}

	public String getPlacePaymentMethod() {
		return mPlacePaymentMethod;
	}

	public void setPlacecc_id(String cc_id) {
		mPlaceCCId = cc_id;
	}

	public String getPlacecc_id() {
		return mPlaceCCId;
	}

	public String getmCheckPaymentMethod() {
		return mCheckPaymentMethod;
	}

	public void setmCheckPaymentMethod(String mCheckPaymentMethod) {
		this.mCheckPaymentMethod = mCheckPaymentMethod;
	}
}

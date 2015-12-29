package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class ShippingAddress extends SimiEntity {
	private String mName;
	private String mStreet;
	private String mCity;
	private String mStateName;
	private String mStateCode;
	private String mZip;
	private String mCountryName;
	private String mCountryCode;
	private String mPhone;
	private String mEmail;

	public String getName() {
		if (null == mName) {
			mName = getData(Constants.NAME);
		}
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getStreet() {
		if (null == mStreet) {
			mStreet = getData(Constants.STREET);
		}
		return mStreet;
	}

	public void setStreet(String street) {
		this.mStreet = street;
	}

	public String getCity() {
		if (null == mCity) {
			mCity = getData(Constants.CITY);
		}
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public String getState_name() {
		if (null == mStateName) {
			mStateName = getData(Constants.STATE_NAME);
		}
		return mStateName;
	}

	public void setState_name(String state_name) {
		this.mStateName = state_name;
	}

	public String getState_code() {
		if (null == mStateCode) {
			mStateCode = getData(Constants.STATE_CODE);
		}
		return mStateCode;
	}

	public void setState_code(String state_code) {
		this.mStateCode = state_code;
	}

	public String getZip() {
		if (null == mZip) {
			mZip = getData(Constants.ZIP);
		}
		return mZip;
	}

	public void setZip(String zip) {
		this.mZip = zip;
	}

	public String getCountry_name() {
		if (null == mCountryName) {
			mCountryName = getData(Constants.COUNTRY_NAME);
		}
		return mCountryName;
	}

	public void setCountry_name(String country_name) {
		this.mCountryName = country_name;
	}

	public String getCountry_code() {
		if (null == mCountryCode) {
			mCountryCode = getData(Constants.COUNTRY_CODE);
		}
		return mCountryCode;
	}

	public void setCountry_code(String country_code) {
		this.mCountryCode = country_code;
	}

	public String getPhone() {
		if (null == mPhone) {
			mPhone = getData(Constants.PHONE);
		}
		return mPhone;
	}

	public void setPhone(String phone) {
		this.mPhone = phone;
	}

	public String getEmail() {
		if (null == mEmail) {

			mEmail = getData(Constants.EMAIL);
		}
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}
}

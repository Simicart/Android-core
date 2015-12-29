package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class NewAddressBook extends SimiEntity{
	private String mAddressID = "-1";
	private String mStateID;
	private String mPrefix;
	private String mName;
	private String mSuffix;
	private String mStreet;
	private String mCity;
	private String mStateName = "";
	private String mStateCode;
	private String mZipCode;
	private String mCountryName = "";
	private String mCountryCode;
	private String mTaxVat;
	private String mPhone;
	private String mEmail;
	private String mFax;
	
	public String getFax() {
		if (null == mFax) {
			mFax = getData(Constants.FAX);
		}
		return mFax;
	}

	public void setFax(String fax) {
		this.mFax = fax;
	}
	
	public String getAddressId() {
		if (mAddressID.equals("-1")) {
			mAddressID = getData(Constants.ADDRESS_ID);
		}
		return mAddressID;
	}

	public void setAddressId(String addressId) {
		this.mAddressID = addressId;
	}
	
	public String getStateId() {
		if (null == mStateID) {
			mStateID = getData(Constants.STATE_ID);
		}
		return mStateID;
	}

	public void setStateId(String stateId) {
		this.mStateID = stateId;
	}

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

	public String getStateName() {
		if (null == mStateName) {
			mStateName = getData(Constants.STATE_NAME);
		}
		return mStateName;
	}

	public void setStateName(String stateName) {
		this.mStateName = stateName;
	}

	public String getStateCode() {
		if (null == mStateCode) {
			mStateCode = getData(Constants.STATE_CODE);
		}

		return mStateCode;
	}

	public void setStateCode(String stateCode) {
		this.mStateCode = stateCode;
	}

	public String getZipCode() {

		if (null == mZipCode) {
			mZipCode = getData(Constants.ZIP);
		}
		return mZipCode;
	}

	public void setZipCode(String zipCode) {
		this.mZipCode = zipCode;
	}

	public String getCountryName() {
		if (null == mCountryName) {
			mCountryName = getData(Constants.COUNTRY_NAME);
		}
		return mCountryName;
	}

	public void setCountryName(String countryName) {
		this.mCountryName = countryName;
	}

	public String getCountryCode() {
		if (null == mCountryCode) {
			mCountryCode = getData(Constants.COUNTRY_CODE);
		}
		return mCountryCode;
	}

	public void setCountryCode(String countryCode) {
		this.mCountryCode = countryCode;
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

	public String getPrefix() {
		if (null == mPrefix) {
			mPrefix = getData(Constants.PREFIX);
		}
		return mPrefix;
	}

	public void setPrefix(String prefix) {
		this.mPrefix = prefix;
	}

	public String getSuffix() {
		if (null == mSuffix) {
			mSuffix = getData(Constants.SUFFIX);
		}
		return mSuffix;
	}

	public void setSuffix(String suffix) {
		this.mSuffix = suffix;
	}

	public String getTaxvat() {
		if (null == mTaxVat) {
			mTaxVat = getData(Constants.TAXVAT);
		}
		return mTaxVat;
	}

	public void setTaxvat(String taxvat) {
		this.mTaxVat = taxvat;
	}
}

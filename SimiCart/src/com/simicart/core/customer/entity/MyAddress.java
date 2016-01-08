package com.simicart.core.customer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

public class MyAddress extends SimiEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mAddressID = "-1";
	private String mStateID;
	private String mPrefix;
	private String mName;
	private String mSuffix;
	private String mStreet;
	private String mCity;
	private String mStateName;
	private String mStateCode;
	private String mZipCode;
	private String mCountryName;
	private String mCountryCode;
	private String mTaxVat;
	private String mGender;
	private String mDay;
	private String mMonth;
	private String mYear;
	private String mPhone;
	private String mEmail;
	private String mFax;
	private String mCompany;
	private String mTaxVatCheckout;

	public String getFax() {
		if (null == mFax) {
			mFax = getData(Constants.FAX);
		}
		return mFax;
	}

	public void setFax(String fax) {
		this.mFax = fax;
	}

	public String getCompany() {
		if (null == mCompany) {
			mCompany = getData(Constants.COMPANY);
		}
		if (null != mCompany && mCompany.equals("null")) {
			mCompany = "";
		}
		return mCompany;
	}

	public void setCompany(String company) {
		this.mCompany = company;
	}

	public String getAddressId() {
		if (!Utils.validateString(mAddressID) || mAddressID.equals("-1")) {
			mAddressID = getData(Constants.ADDRESS_ID);
		}
		if (!Utils.validateString(mAddressID)) {
			mAddressID = "0";
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

		if (null != mStreet && mStreet.equals("null")) {
			mStreet = "";
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
		if (null != mCity && mCity.equals("null")) {
			mCity = "";
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
		if (null != mStateName && mStateName.equals("null")) {
			mStateName = "";
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
		if (null != mZipCode && mZipCode.equals("null")) {
			mZipCode = "";
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
		if (null != mCountryName && mCountryName.equals("null")) {
			mCountryName = "";
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
		if (null != mPhone && mPhone.equals("null")) {
			mPhone = "";
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

	public String getGender() {
		if (null == mGender) {
			mGender = getData(Constants.GENDER);
		}
		return mGender;
	}

	public void setGender(String gender) {
		this.mGender = gender;
	}

	public String getDay() {
		if (null == mDay) {
			mDay = getData(Constants.DAY);
		}
		return mDay;
	}

	public void setDay(String day) {
		this.mDay = day;
	}

	public String getMonth() {
		if (null == mMonth) {
			mMonth = getData(Constants.MONTH);
		}
		return mMonth;
	}

	public void setMonth(String month) {
		this.mMonth = month;
	}

	public String getYear() {
		if (null == mYear) {
			mYear = getData(Constants.YEAR);
		}
		return mYear;
	}

	public void setYear(String year) {
		this.mYear = year;
	}

	public void setTaxvatCheckout(String tax) {
		this.mTaxVatCheckout = tax;
	}

	public String getTaxvatCheckout() {
		if (null == mTaxVatCheckout) {
			mTaxVatCheckout = getData(Constants.TAXVAT_CHECKOUT);
		}
		return this.mTaxVatCheckout;
	}

	public List<NameValuePair> toParamsRequest() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		String addressID = "";
		if (null == getAddressId() || getAddressId().equals("-1")
				|| getAddressId().equals("")) {
			addressID = "0";
		} else {
			addressID = getAddressId();
		}
		params.add(new BasicNameValuePair("address_id", addressID));

		// name
		String name = getName();
		if (null != name && !name.equals("") && !name.equals("null")) {
			params.add(new BasicNameValuePair("name", name));
		}

		// street
		String street = getStreet();
		if (null != street && !street.equals("")) {
			params.add(new BasicNameValuePair("street", street));
		}
		// city
		String city = getCity();
		if (null != city && !city.equals("")) {
			params.add(new BasicNameValuePair("city", city));
		}
		// state name
		String statename = getStateName();
		if (null != statename && !statename.equals("")
				&& !statename.equals("null")) {
			params.add(new BasicNameValuePair("state_code", getStateCode()));
			params.add(new BasicNameValuePair("state_id", getStateId()));
			params.add(new BasicNameValuePair("state_name", statename));
		}
		// country name
		String countryname = getCountryName();

		if (null != countryname && !countryname.equals("")) {
			params.add(new BasicNameValuePair("country_code", getCountryCode()));
			params.add(new BasicNameValuePair("country_name", countryname));
		}
		// ZIP code
		String zipcode = getZipCode();
		if (null != zipcode && !zipcode.equals("")) {
			params.add(new BasicNameValuePair("zip", zipcode));
		}
		if (null != zipcode && !zipcode.equals("")) {
			params.add(new BasicNameValuePair("zip_code", zipcode));
		}
		// phone
		String phone = getPhone();
		if (null != phone && !phone.equals("")) {
			params.add(new BasicNameValuePair("phone", phone));
		}
		// email
		String email = getEmail();
		if (null != email && !email.equals("")) {
			params.add(new BasicNameValuePair("email", email));
		}
		// prefix
		String prefix = getPrefix();
		if (null != prefix && !prefix.equals("")) {
			params.add(new BasicNameValuePair("prefix", prefix));
		}
		// suffix
		String suffix = getSuffix();
		if (null != suffix && !suffix.equals("")) {
			params.add(new BasicNameValuePair("suffix", suffix));
		}
		// tax vat
		String taxvat = getTaxvat();
		if (null != taxvat && !taxvat.equals("")) {
			params.add(new BasicNameValuePair("taxvat", taxvat));
		}
		// tax vat check out
		String taxvatcheckout = getTaxvatCheckout();
		if (null != taxvatcheckout && !taxvatcheckout.equals("null")
				&& !taxvatcheckout.equals("")) {
			params.add(new BasicNameValuePair("vat_id", taxvatcheckout));
		}
		// gender
		String gender = getGender();
		if (null != gender && !gender.equals("")) {
			params.add(new BasicNameValuePair("gender", Utils
					.getValueGender(gender)));
		}
		String day1 = getDay();
		if (null != day1 && !day1.equals("")) {
			String day = "";
			if (getDay().length() == 1) {
				day = "0" + getDay();
			} else {
				day = getDay();
			}
			params.add(new BasicNameValuePair("day", "" + day + ""));
			String month = "";
			if (getMonth().length() == 1) {
				month = "0" + getMonth();
			} else {
				month = getMonth();
			}
			params.add(new BasicNameValuePair("month", "" + month + ""));
			params.add(new BasicNameValuePair("year", "" + getYear() + ""));
			params.add(new BasicNameValuePair("dob", "" + month + "/" + day
					+ "/" + getYear() + ""));
		}
		// Fax
		String fax = getFax();
		if (null != fax && !fax.equals("")) {
			params.add(new BasicNameValuePair("fax", fax));
		}
		// company
		String company = getCompany();
		if (null != company && !company.equals("")) {
			params.add(new BasicNameValuePair("company", company));
		}

		return params;
	}

}

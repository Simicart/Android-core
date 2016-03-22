package com.simicart.core.customer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

public class MyAddress extends SimiEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address_id = "-1";
	private String state_id;
	private String prefix;
	private String name;
	private String suffix;
	private String street;
	private String city;
	private String state_name;
	private String state_code;
	private String zip;
	private String country_name;
	private String country_code;
	private String taxvat;
	private String gender;
	private String day;
	private String month;
	private String year;
	private String phone;
	private String email;
	private String fax;
	private String company;
	private String mTaxVatCheckout;
	private String latlng;

	public MyAddress() {
		// TODO Auto-generated constructor stub
	}

	public String getLatlng() {
		if (null == latlng) {
			latlng = getData(Constants.LATLNG);
		}
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getFax() {
		if (null == fax) {
			fax = getData(Constants.FAX);
		}
		if (checkAddressNA(fax)) {
			fax = "";
		}
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCompany() {
		if (null == company) {
			company = getData(Constants.COMPANY);
		}
		if (checkAddressNA(company)) {
			company = "";
		}
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddressId() {
		if (!Utils.validateString(address_id) || address_id.equals("-1")) {
			address_id = getData(Constants.ADDRESS_ID);
		}
		if (!Utils.validateString(address_id)) {
			address_id = "0";
		}

		return address_id;
	}

	public void setAddressId(String addressId) {
		this.address_id = addressId;
	}

	public String getStateId() {
		if (null == state_id) {
			state_id = getData(Constants.STATE_ID);
		}
		return state_id;
	}

	public void setStateId(String stateId) {
		this.state_id = stateId;
	}

	public String getName() {
		if (null == name) {
			name = getData(Constants.NAME);
		}
		if (checkAddressNA(name)) {
			name = "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		if (null == street) {
			street = getData(Constants.STREET);
		}

		if (checkAddressNA(street)) {
			street = "";
		}
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		if (null == city) {
			city = getData(Constants.CITY);
		}
		if (checkAddressNA(city)) {
			city = "";
		}
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateName() {
		if (null == state_name) {
			state_name = getData(Constants.STATE_NAME);
		}
		if (checkAddressNA(state_name)) {
			state_name = "";
		}
		return state_name;
	}

	public void setStateName(String stateName) {
		this.state_name = stateName;
	}

	public String getStateCode() {
		if (null == state_code) {
			state_code = getData(Constants.STATE_CODE);
		}

		return state_code;
	}

	public void setStateCode(String stateCode) {
		this.state_code = stateCode;
	}

	public String getZipCode() {

		if (null == zip) {
			zip = getData(Constants.ZIP);
		}
		if (checkAddressNA(zip)) {
			zip = "";
		}
		return zip;
	}

	public void setZipCode(String zipCode) {
		this.zip = zipCode;
	}

	public String getCountryName() {
		if (null == country_name) {
			country_name = getData(Constants.COUNTRY_NAME);
		}
		if (checkAddressNA(country_name)) {
			country_name = "";
		}
		return country_name;
	}

	public void setCountryName(String countryName) {
		this.country_name = countryName;
	}

	public String getCountryCode() {
		if (null == country_code) {
			country_code = getData(Constants.COUNTRY_CODE);
		}
		return country_code;
	}

	public void setCountryCode(String countryCode) {
		this.country_code = countryCode;
	}

	public String getPhone() {
		if (null == phone) {
			phone = getData(Constants.PHONE);
		}
		if (checkAddressNA(phone)) {
			phone = "";
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		if (null == email) {
			email = getData(Constants.EMAIL);
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrefix() {
		if (null == prefix) {
			prefix = getData(Constants.PREFIX);
		}
		if (checkAddressNA(prefix)) {
			prefix = "";
		}
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		if (null == suffix) {
			suffix = getData(Constants.SUFFIX);
		}
		if (checkAddressNA(suffix)) {
			suffix = "";
		}
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getTaxvat() {
		if (null == taxvat) {
			taxvat = getData(Constants.TAXVAT);
		}
		if (checkAddressNA(taxvat)) {
			taxvat = "";
		}
		return taxvat;
	}

	public void setTaxvat(String taxvat) {
		this.taxvat = taxvat;
	}

	public String getGender() {
		if (null == gender) {
			gender = getData(Constants.GENDER);
		}
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDay() {
		if (null == day) {
			day = getData(Constants.DAY);
		}
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		if (null == month) {
			month = getData(Constants.MONTH);
		}
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		if (null == year) {
			year = getData(Constants.YEAR);
		}
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setTaxvatCheckout(String tax) {
		this.mTaxVatCheckout = tax;
	}

	public String getTaxvatCheckout() {
		if (null == mTaxVatCheckout) {
			mTaxVatCheckout = getData(Constants.TAXVAT_CHECKOUT);
		}
		if (checkAddressNA(mTaxVatCheckout)) {
			mTaxVatCheckout = "";
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

	@Override
	public String toString() {
		return "MyAddress [address_id=" + address_id + ", state_id=" + state_id
				+ ", prefix=" + prefix + ", mName=" + name + ", suffix="
				+ suffix + ", street=" + street + ", city=" + city
				+ ", state_name=" + state_name + ", state_code=" + state_code
				+ ", zip=" + zip + ", country_name=" + country_name
				+ ", country_code=" + country_code + ", taxvat=" + taxvat
				+ ", gender=" + gender + ", day=" + day + ", month=" + month
				+ ", year=" + year + ", phone=" + phone + ", email=" + email
				+ ", fax=" + fax + ", company=" + company
				+ ", mTaxVatCheckout=" + mTaxVatCheckout + " latlng=" + latlng
				+ "]";
	}

	private boolean checkAddressNA(String text) {
		if (text == null || text.equals("null") || text.equals("N/A")) {
			return true;
		} else {
			return false;
		}
	}
}

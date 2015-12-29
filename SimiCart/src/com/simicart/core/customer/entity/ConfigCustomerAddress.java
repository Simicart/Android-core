package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONException;

import com.simicart.core.base.model.entity.SimiEntity;

public class ConfigCustomerAddress extends SimiEntity {
	public static String OPTION_REQUIRE = "req";
	public static String OPTION_OPTIONAL = "opt";
	public static String OPTION_HIDE = "";

	private String prefix;
	private String suffix;
	private String dob;
	private String vat_id;
	private String taxvat;
	private String gender;
	private ArrayList<GenderConfig> genderConfigs = new ArrayList<>();
	private String company;
	private String street;
	private String country;
	private String state;
	private String city;
	private String zipcode;
	private String telephone;
	private String fax;
	private String name;
	private String email;

	public ConfigCustomerAddress() {
		setPrefix(OPTION_HIDE);
		setName(OPTION_REQUIRE);
		setSuffix(OPTION_HIDE);
		setEmail(OPTION_REQUIRE);
		setCompany(OPTION_HIDE);
		setVat_id(OPTION_HIDE);
		setStreet(OPTION_REQUIRE);
		setCity(OPTION_REQUIRE);
		setState(OPTION_OPTIONAL);
		setCountry(OPTION_REQUIRE);
		setZipcode(OPTION_REQUIRE);
		setTelephone(OPTION_REQUIRE);
		setFax(OPTION_HIDE);
		setDob(OPTION_HIDE);
		setGender(OPTION_HIDE);
		setTaxvat(OPTION_HIDE);
	}

	public void setVat_id(String vat_id) {
		this.vat_id = vat_id;
	}

	public String getVat_id() {
		return vat_id;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getTaxvat() {
		return taxvat;
	}

	public void setTaxvat(String taxvat) {
		this.taxvat = taxvat;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ArrayList<GenderConfig> getGenderConfigs() {
		return genderConfigs;
	}

	public void setGenderConfigs(ArrayList<GenderConfig> genderConfigs) {
		this.genderConfigs = genderConfigs;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getData(String key) {
		if (mJSON != null && mJSON.has(key)) {
			try {
				return this.mJSON.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
				return OPTION_OPTIONAL;
			}
		}
		return OPTION_OPTIONAL;
	}

}
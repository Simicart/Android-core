package com.simicart.core.customer.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.material.ButtonRectangle;

public class AddressBookDetailBlock extends SimiBlock implements
		AddressBookDetailDelegate {
	protected MyAddress mAddressBookDetail;
	protected EditText et_prefix;
	protected EditText et_fullname;
	protected EditText et_suffix;
	protected EditText et_street;
	protected EditText et_city;
	protected EditText et_state;
	protected TextView tv_state;
	protected EditText et_zipcode;
	protected TextView tv_country;
	protected EditText et_company;
	protected EditText et_phone;
	protected EditText et_fax;
	protected TextView tv_date_birth;
	protected RelativeLayout rl_gender;
	protected RelativeLayout rl_state;
	protected RelativeLayout rl_country;
	protected EditText et_taxvat;
	protected EditText et_email;
	protected EditText et_pass;
	protected EditText et_confirm_pass;
	protected EditText et_tax_checkout;
	protected ButtonRectangle bt_save;
	protected ConfigCustomerAddress mAddress;
	protected ArrayList<CountryAllowed> listCountry;
	protected String mCountry;
	private ImageView img_state;
	private ImageView img_country;

	public AddressBookDetailBlock(View view, Context context) {
		super(view, context);
		mAddress = DataLocal.ConfigCustomerAddress;
	}

	public void setSaveClicker(OnClickListener clicker) {
		bt_save.setOnClickListener(clicker);
	}

	public void setChooseCountry(OnClickListener clicker) {
		tv_country.setOnClickListener(clicker);
	}

	public void setChooseStates(OnClickListener clicker) {
		tv_state.setOnClickListener(clicker);
	}

	@Override
	public void initView() {
		et_prefix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_prefix_show"));
		et_fullname = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fullname"));
		et_suffix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_suffix_show"));
		et_street = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_street"));
		et_city = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_city"));
		et_state = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_state"));
		tv_state = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_state"));
		et_zipcode = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_zipcode"));
		tv_country = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_country"));
		et_company = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_company"));
		et_phone = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_phone"));
		et_fax = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fax"));
		tv_date_birth = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_date_birth"));
		rl_gender = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_gender"));
		rl_country = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_country"));
		rl_state = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_state"));
		et_taxvat = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_taxvat_show"));
		et_email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		et_pass = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_pass"));
		et_confirm_pass = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_confirm_pass"));
		et_tax_checkout = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_tax_checkout"));
		bt_save = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("bt_save"));
		img_state = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"imv_extend_st"));
		img_country = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"imv_extend_ct"));

		createCompany();
		createPrefix();
		createFullname();
		createSuffix();
		createStreet();
		createCity();
		createZipCode();
		createCountry();
		createFax();
		createPhone();
		createTaxVat();
		createEmail();
		createDateBirth();
		createGender();
		createPassAndPassConfig();
		createTaxVatCheckOut();
		createButtonSave();
		changeColor();
	}

	private void changeColor() {
		Utils.changeColorEditText(et_prefix);
		Utils.changeColorEditText(et_fullname);
		Utils.changeColorEditText(et_suffix);
		Utils.changeColorEditText(et_street);
		Utils.changeColorEditText(et_city);
		Utils.changeColorEditText(et_state);
		Utils.changeColorEditText(et_zipcode);
		Utils.changeColorEditText(et_company);
		Utils.changeColorEditText(et_phone);
		Utils.changeColorEditText(et_fax);
		Utils.changeColorEditText(et_taxvat);
		Utils.changeColorEditText(et_email);
		Utils.changeColorEditText(et_pass);
		Utils.changeColorEditText(et_confirm_pass);
		Utils.changeColorEditText(et_tax_checkout);

		Utils.changeColorTextView(tv_state);
		Utils.changeColorTextView(tv_country);
		Utils.changeColorTextView(tv_date_birth);

		Utils.changeColorImageview(mContext, img_state, "ic_extend");
		Utils.changeColorImageview(mContext, img_country, "ic_extend");
	}

	private void createPrefix() {
		et_prefix.setHint(Config.getInstance().getText("Prefix") + " (*)");
		if (null != mAddressBookDetail) {
			String prefix = mAddressBookDetail.getPrefix();
			if (null != prefix && !prefix.equals("") && !prefix.equals("null")) {
				et_prefix.setText(prefix);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getPrefix()) {
			case "":
				et_prefix.setVisibility(View.GONE);
				break;
			case "req":
				et_prefix.setHint(Config.getInstance().getText("Prefix")
						+ " (*)");
				break;
			case "opt":
				et_prefix.setHint(Config.getInstance().getText("Prefix"));
				break;
			default:
				break;
			}
		}
	}

	private void createFullname() {
		if (mAddressBookDetail != null) {
			String fullname = mAddressBookDetail.getName();

			if (null != fullname && !fullname.equals("")
					&& !fullname.equals("null")) {
				et_fullname.setText(fullname);
			}
			et_fullname.setHint(Config.getInstance().getText("Full name")
					+ " (*)");
		}
	}

	private void createSuffix() {
		et_suffix.setHint(Config.getInstance().getText("Suffix") + " (*)");
		if (mAddressBookDetail != null) {
			String suffix = mAddressBookDetail.getSuffix();
			if (null != suffix && !suffix.equals("") && !suffix.equals("null")) {
				et_suffix.setText(suffix);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getSuffix()) {
			case "":
				et_suffix.setVisibility(View.GONE);
				break;
			case "req":
				et_suffix.setHint(Config.getInstance().getText("Suffix")
						+ " (*)");
				break;
			case "opt":
				et_suffix.setHint(Config.getInstance().getText("Suffix"));
				break;
			default:
				break;
			}
		}
	}

	private void createStreet() {
		et_street.setHint(Config.getInstance().getText("Street") + " (*)");
		if (mAddressBookDetail != null) {
			String street = mAddressBookDetail.getStreet();
			if (null != street && !street.equals("") && !street.equals("null")) {
				et_street.setText(street);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getStreet()) {
			case "":
				et_street.setVisibility(View.GONE);
				break;
			case "req":
				et_street.setHint(Config.getInstance().getText("Street")
						+ " (*)");
				break;
			case "opt":
				et_street.setHint(Config.getInstance().getText("Street"));
				break;
			default:
				break;
			}
		}
	}

	private void createCity() {

		et_city.setHint(Config.getInstance().getText("City") + " (*)");
		if (mAddressBookDetail != null) {
			String city = mAddressBookDetail.getCity();
			if (null != city && !city.equals("") && !city.equals("null")) {
				et_city.setText(city);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getCity()) {
			case "":
				et_city.setVisibility(View.GONE);
				break;
			case "req":
				et_city.setHint(Config.getInstance().getText("Prefix") + " (*)");
				break;
			case "opt":
				et_city.setHint(Config.getInstance().getText("Prefix"));
				break;
			default:
				break;
			}
		}
	}

	private void createZipCode() {

		et_zipcode.setHint(Config.getInstance().getText("Post/Zip Code")
				+ " (*)");
		if (mAddressBookDetail != null) {
			String zipcode = mAddressBookDetail.getZipCode();

			if (null != zipcode && !zipcode.equals("")
					&& !zipcode.equals("null")) {
				et_zipcode.setText(zipcode);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getZipcode()) {
			case "":
				et_zipcode.setVisibility(View.GONE);
				break;
			case "req":
				et_zipcode.setHint(Config.getInstance()
						.getText("Post/Zip Code") + " (*)");
				break;
			case "opt":
				et_zipcode.setHint(Config.getInstance()
						.getText("Post/Zip Code"));
				break;
			default:
				break;
			}
		}
	}

	private void createPhone() {
		et_phone.setHint(Config.getInstance().getText("Phone") + " (*)");
		if (mAddressBookDetail != null) {
			String phone = mAddressBookDetail.getPhone();
			if (null != phone && !phone.equals("") && !phone.equals("null")) {
				et_phone.setText(mAddressBookDetail.getPhone());
			}
		}
		if (mAddress != null) {
			switch (mAddress.getTelephone()) {
			case "":
				et_phone.setVisibility(View.GONE);
				break;
			case "req":
				et_phone.setHint(Config.getInstance().getText("Phone") + " (*)");
				break;
			case "opt":
				et_phone.setHint(Config.getInstance().getText("Phone"));
				break;
			default:
				break;
			}
		}
	}

	private void createEmail() {
		if (mAddressBookDetail != null) {
			String email = mAddressBookDetail.getEmail();

			if (null != email && !email.equals("") && !email.equals("null")) {
				et_email.setText(mAddressBookDetail.getEmail());
				et_email.setKeyListener(null);
			}
		}
	}

	private void createDateBirth() {
		tv_date_birth.setVisibility(View.GONE);
	}

	private void createCompany() {
		et_company.setHint(Config.getInstance().getText("Company") + " (*)");
		if (mAddressBookDetail != null) {
			String company = mAddressBookDetail.getCompany();

			if (null != company && !company.equals("")
					&& !company.equals("null")) {
				et_company.setText(company);
			}
			if (mAddress != null) {
				if (mAddress != null) {
					String check = mAddress.getCompany().toLowerCase();
					switch (check) {
					case "":
						et_company.setVisibility(View.GONE);
						break;
					case "req":
						et_company.setHint(Config.getInstance().getText(
								"Company")
								+ " (*)");
						break;
					case "opt":
						et_company.setHint(Config.getInstance().getText(
								"Company"));
						break;
					default:
						break;
					}
				}
			}
		}
	}

	private void createCountry() {
		if (mAddressBookDetail != null) {
			String countryname = mAddressBookDetail.getCountryName();
			if (null != countryname && !countryname.equals("")
					&& !countryname.equals("null")) {
				tv_country.setText(countryname);
			}
		}
		if (mAddress != null) {
			switch (mAddress.getCountry()) {
			case "":
				rl_country.setVisibility(View.GONE);
				break;
			case "req":
				break;
			case "opt":
				break;
			default:
				break;
			}
		}
	}

	private void createState(ArrayList<CountryAllowed> listCountry) {
		if (mAddressBookDetail != null && listCountry != null) {
			ArrayList<String> states = getStateFromCountry(
					mAddressBookDetail.getCountryName(), listCountry);
			et_state.setVisibility(View.VISIBLE);
			rl_state.setVisibility(View.VISIBLE);
			Log.e("AddressBookDetailBlock createState ", "001");
			if (states.size() <= 0) {
				et_state.setVisibility(View.VISIBLE);
				rl_state.setVisibility(View.GONE);
				et_state.setHint(Config.getInstance().getText("State"));
			} else {
				rl_state.setVisibility(View.VISIBLE);
				et_state.setVisibility(View.GONE);
			}

			String state = mAddressBookDetail.getStateName();
			if (null != state && !state.equals("") && !state.equals("null")) {
				tv_state.setText(mAddressBookDetail.getStateName());
				et_state.setText(mAddressBookDetail.getStateName());
			}
		}
		if (mAddressBookDetail != null) {
			String state = mAddressBookDetail.getStateName();
			if (null != state && !state.equals("") && !state.equals("null")) {
				tv_state.setText(mAddressBookDetail.getStateName());
			}
		}
		if (mAddress != null) {
			switch (mAddress.getState()) {
			case "":
				rl_state.setVisibility(View.GONE);
				et_state.setVisibility(View.GONE);
				break;
			case "req":
				et_state.setHint(Config.getInstance().getText("State") + "(*)");
				break;
			case "opt":
				et_state.setHint(Config.getInstance().getText("State"));
				break;
			default:
				break;
			}
		}

	}

	private void createButtonSave() {
		bt_save.setTextColor(Color.WHITE);
		bt_save.setText(Config.getInstance().getText("Save"));
		bt_save.setBackgroundColor(Config.getInstance().getColorMain());
		bt_save.setTextSize(Constants.SIZE_TEXT_BUTTON);
	}

	private void createGender() {
		rl_gender.setVisibility(View.GONE);
	}

	private void createFax() {
		et_fax.setHint(Config.getInstance().getText("Fax") + " (*)");
		if (mAddressBookDetail != null) {
			String fax = mAddressBookDetail.getFax();
			if (null != fax && !fax.equals("") && !fax.equals("null")) {
				et_fax.setText(fax);
			}
		}
		if (mAddress != null) {
			String check = mAddress.getFax().toLowerCase();
			switch (check) {
			case "":
				et_fax.setVisibility(View.GONE);
				break;
			case "req":
				et_fax.setHint(Config.getInstance().getText("Fax") + " (*)");
				break;
			case "opt":
				et_fax.setHint(Config.getInstance().getText("Fax"));
				break;
			default:
				break;
			}
		}
	}

	private void createTaxVat() {

		et_taxvat.setVisibility(View.GONE);
		// et_taxvat.setHint(Config.getInstance().getText("Tax/VAT number")
		// + " (*)");
		// Log.e("AddressBookDetailBlock ", "001");
		// String taxvat = mAddressBookDetail.getTaxvat();
		// if (null != taxvat && !taxvat.equals("") && !taxvat.equals("null")) {
		// Log.e("AddressBookDetailBlock ", "createTaxVat : " + taxvat);
		// et_taxvat.setText(taxvat);
		// }
		// Log.e("AddressBookDetailBlock ", "002");
		//
		// switch (mAddress.getTaxvat_show()) {
		// case "":
		// Log.e("AddressBookDetailBlock ", "003");
		// et_taxvat.setVisibility(View.GONE);
		// break;
		// case "req":
		// Log.e("AddressBookDetailBlock ", "004");
		// et_taxvat.setHint(Config.getInstance().getText("Tax/VAT number")
		// + " (*)");
		// break;
		// case "opt":
		// Log.e("AddressBookDetailBlock ", "005");
		// et_taxvat.setHint(Config.getInstance().getText("Tax/VAT number"));
		// break;
		// default:
		// break;
		// }
	}

	private void createTaxVatCheckOut() {
		et_tax_checkout.setVisibility(View.VISIBLE);

		et_tax_checkout.setHint(Config.getInstance().getText("Tax/VAT number")
				+ " (*)");
		if (mAddressBookDetail != null) {
			String vatNumber = mAddressBookDetail.getTaxvatCheckout();
			if (Utils.validateString(vatNumber)) {
				Log.e("AddressBookDetailBlock : ", "TaxVATNumber : "
						+ vatNumber);
				et_tax_checkout.setText(vatNumber);
			}
		}
		if (mAddress != null) {
			String check = mAddress.getVat_id().toLowerCase();
			switch (check) {
			case "":
				et_tax_checkout.setVisibility(View.GONE);
				return;
			case "req":
				et_tax_checkout.setHint(Config.getInstance().getText(
						"VAT number")
						+ " (*)");
				break;
			case "opt":
				et_tax_checkout.setHint(Config.getInstance().getText(
						"VAT number"));
				break;
			default:
				break;
			}
		}

	}

	private void createPassAndPassConfig() {
		et_pass.setVisibility(View.GONE);
		et_confirm_pass.setVisibility(View.GONE);
	}

	public void setAddressBookDetail(MyAddress addressBookDetail) {
		this.mAddressBookDetail = addressBookDetail;
	}

	@Override
	public MyAddress getAddressBookDetail() {
		MyAddress addressBookDetail = new MyAddress();
		String email = et_email.getText().toString();
		if (null != email) {
			addressBookDetail.setEmail(email);
		}
		String prefix = et_prefix.getText().toString();
		if (null != prefix) {
			addressBookDetail.setPrefix(prefix);
		}
		String fullname = et_fullname.getText().toString();
		if (null != fullname) {
			addressBookDetail.setName(fullname);
		}
		String suffix = et_suffix.getText().toString();
		if (null != suffix) {
			addressBookDetail.setSuffix(suffix);
		}
		String taxvat = et_tax_checkout.getText().toString();
		if (null != taxvat) {
			addressBookDetail.setTaxvatCheckout(taxvat);
		}
		String city = et_city.getText().toString();
		if (null != city) {
			addressBookDetail.setCity(city);
		}
		String zipcode = et_zipcode.getText().toString();
		if (null != zipcode) {
			addressBookDetail.setZipCode(zipcode);
		}
		String phone = et_phone.getText().toString();
		if (null != phone) {
			addressBookDetail.setPhone(phone);
		}
		String street = et_street.getText().toString();
		if (null != street) {
			addressBookDetail.setStreet(street);
		}
		String fax = et_fax.getText().toString();
		if (null != fax) {
			addressBookDetail.setFax(fax);
		}
		String company = et_company.getText().toString();
		if (null != company) {
			addressBookDetail.setCompany(company);
		}

		if (mAddressBookDetail != null) {
			String id = mAddressBookDetail.getAddressId();
			if (null != id) {
				Log.e("AddressBookDetailBlock ID ", id);
				addressBookDetail.setAddressId(id);
			}
			// state
			String state = tv_state.getText().toString();
			if (null != state && !state.equals("") && !state.equals("null")) {
				addressBookDetail.setStateName(state);
			} else {
				state = et_state.getText().toString();
				if (null != state && !state.equals("") && !state.equals("null")) {
					addressBookDetail.setStateName(state);
				}
			}
			String countryname = mAddressBookDetail.getCountryName();
			if (null != countryname) {
				addressBookDetail.setCountryName(countryname);
				String countrycode = mAddressBookDetail.getCountryCode();
				if (null != countrycode) {
					addressBookDetail.setCountryCode(countrycode);
				}
			}
		}
		return addressBookDetail;
	}

	@Override
	public void setCountry(int type, String mCountry,
			ArrayList<CountryAllowed> listCountry) {
		ArrayList<String> states = getStateFromCountry(mCountry, listCountry);
		if (type == 0) {
			mAddressBookDetail.setCountryName(mCountry);
			mAddressBookDetail.setStateName("");
			if (states.size() <= 0) {
				mAddressBookDetail.setStateName("");
			} else {
				mAddressBookDetail.setStateName(states.get(0).toString());
			}
			mAddressBookDetail.setCountryCode(getCountryCode(mCountry,
					listCountry));
		}
		if (type == 1) {
			mAddressBookDetail.setStateName(mCountry);
			mAddressBookDetail.setStateCode(getStateCode(
					mCountry,
					getListStateFromCountry(
							mAddressBookDetail.getCountryName(), listCountry)));
			mAddressBookDetail.setStateId(getStateId(
					mCountry,
					getListStateFromCountry(
							mAddressBookDetail.getCountryName(), listCountry)));
		}
		createState(listCountry);
	}

	public ArrayList<String> getStateFromCountry(String country,
			ArrayList<CountryAllowed> listCountry) {
		ArrayList<String> states = new ArrayList<String>();
		for (CountryAllowed countryAllowed : listCountry) {
			if (countryAllowed.getCountry_name().equals(country)
					&& countryAllowed.getStateList() != null) {
				for (StateOfCountry state : countryAllowed.getStateList()) {
					states.add(state.getState_name());
				}
				return states;
			}
		}
		return states;
	}

	@Override
	public void setListCountry(ArrayList<CountryAllowed> listCountry) {
		createState(listCountry);
	}

	public String getCountryCode(String country,
			ArrayList<CountryAllowed> countryAlloweds) {
		String country_code = "";
		for (CountryAllowed countryAllowed : countryAlloweds) {
			if (countryAllowed.getCountry_name().equals(country)) {
				country_code = countryAllowed.getCountry_code();
				return country_code;
			}
		}
		return country_code;
	}

	public String getStateCode(String statename,
			ArrayList<StateOfCountry> stateOfCountries) {
		String state_code = "";
		for (StateOfCountry stateOfCountry : stateOfCountries) {
			if (stateOfCountry.getState_name().equals(statename)) {
				state_code = stateOfCountry.getState_code();
				return state_code;
			}
		}
		return state_code;
	}

	public ArrayList<StateOfCountry> getListStateFromCountry(String country,
			ArrayList<CountryAllowed> countryAlloweds) {
		for (CountryAllowed countryAllowed : countryAlloweds) {
			if (countryAllowed.getCountry_name().equals(country)) {
				return countryAllowed.getStateList();
			}
		}
		return null;
	}

	public String getStateId(String statename,
			ArrayList<StateOfCountry> stateOfCountries) {
		String state_Id = "";
		for (StateOfCountry stateOfCountry : stateOfCountries) {
			if (stateOfCountry.getState_name().equals(statename)) {
				state_Id = stateOfCountry.getState_id();
				return state_Id;
			}
		}
		return state_Id;
	}
}

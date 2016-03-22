package com.simicart.core.customer.block;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.GenderAdapter;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.delegate.NewAddressBookDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.ProfileEntity;
import com.simicart.core.material.ButtonRectangle;

@SuppressLint("DefaultLocale")
public class NewAddressBookBlock extends SimiBlock implements
		NewAddressBookDelegate {
	protected EditText edt_prefix;
	protected EditText edt_fullname;
	protected EditText edt_suffix;
	protected EditText edt_street;
	protected EditText edt_city;
	protected EditText edt_state;
	protected TextView tv_state;
	protected EditText edt_zipcode;
	protected TextView tv_country;
	protected EditText edt_company;
	protected EditText edt_phone;
	protected EditText edt_fax;
	protected TextView tv_date_birth;
	protected RelativeLayout rl_gender;
	protected RelativeLayout rl_state;
	protected RelativeLayout rl_country;
	protected EditText edt_taxvat;
	protected EditText edt_email;
	protected EditText edt_pass;
	protected EditText edt_confirmPass;
	protected EditText edt_tax_checkout;
	protected ButtonRectangle btn_save;
	protected TextView tv_gender;
	private ImageView img_gender;
	protected String mSelectedDate;
	protected String mGender;
	private Spinner sp_gender;
	protected int mAfterController;
	ChooseCountryDelegate mController;
	private ImageView img_state;
	private ImageView img_country;
	protected ConfigCustomerAddress mAddress;

	public void setAfterController(int afterController) {
		mAfterController = afterController;
	}

	public NewAddressBookBlock(View view, Context context) {
		super(view, context);
		mAddress = DataLocal.ConfigCustomerAddress;
	}

	public void setSaveAddress(OnClickListener click) {
		btn_save.setOnClickListener(click);
	}

	public void setChooseCountry(OnClickListener click) {
		tv_country.setOnClickListener(click);
	}

	public void setChooseStates(OnClickListener click) {
		tv_state.setOnClickListener(click);
	}

	public void setOnclickTextviewGender(OnClickListener click) {
		tv_gender.setOnClickListener(click);
	}

	public void setOnclickImageGender(OnClickListener click) {
		img_gender.setOnClickListener(click);
	}

	@Override
	public void initView() {
		// TextView tv_editAddress = (TextView) mView.findViewById(Rconfig
		// .getInstance().id("lable_NewAddress"));
		// tv_editAddress.setText(Config.getInstance().getText("New Address"));

		edt_prefix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_prefix_show"));
		edt_fullname = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fullname"));
		edt_suffix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_suffix_show"));
		edt_street = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_street"));
		edt_city = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_city"));
		edt_state = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_state"));
		tv_state = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_state"));
		edt_zipcode = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_zipcode"));
		tv_country = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_country"));
		edt_company = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_company"));
		edt_phone = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_phone"));
		edt_fax = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fax"));
		tv_date_birth = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_date_birth"));
		rl_gender = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_gender"));
		rl_country = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_country"));
		rl_state = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_state"));
		edt_taxvat = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_taxvat_show"));
		edt_email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_pass = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_pass"));
		edt_confirmPass = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_confirm_pass"));
		edt_tax_checkout = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_tax_checkout"));
		btn_save = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("bt_save"));
		tv_gender = (TextView) rl_gender.findViewById(Rconfig.getInstance().id(
				"tv_gender"));
		img_gender = (ImageView) rl_gender.findViewById(Rconfig.getInstance()
				.id("im_extend"));
		img_state = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"imv_extend_st"));
		img_country = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"imv_extend_ct"));
	}

	@Override
	public void createView(int control) {
		createPrefix(control);
		createFullname();
		createSuffix(control);
		createEmail(control);
		createStreet();
		createVatCheckOut();
		createCity();
		createState(control);
		createCountry(control);
		createZipCode();
		createPhone();
		createDateBirth();
		createGender(control);
		createTaxVat();
		createFax(control);
		createPassAndPassConfirm(control);
		createButtonSave();
		createCompany();
		changeColorComponent();
	}

	private void changeColorComponent() {
		Utils.changeColorTextView(tv_state);
		Utils.changeColorTextView(tv_country);
		Utils.changeColorTextView(tv_date_birth);
		Utils.changeColorTextView(tv_gender);

		Utils.changeColorEditText(edt_prefix);
		Utils.changeColorEditText(edt_fullname);
		Utils.changeColorEditText(edt_suffix);
		Utils.changeColorEditText(edt_street);
		Utils.changeColorEditText(edt_city);
		Utils.changeColorEditText(edt_state);
		Utils.changeColorEditText(edt_zipcode);
		Utils.changeColorEditText(edt_company);
		Utils.changeColorEditText(edt_phone);
		Utils.changeColorEditText(edt_fax);
		Utils.changeColorEditText(edt_taxvat);
		Utils.changeColorEditText(edt_email);
		Utils.changeColorEditText(edt_pass);
		Utils.changeColorEditText(edt_confirmPass);
		Utils.changeColorEditText(edt_tax_checkout);

		Utils.changeColorImageview(mContext, img_gender, "ic_extend");
		Utils.changeColorImageview(mContext, img_state, "ic_extend");
		Utils.changeColorImageview(mContext, img_country, "ic_extend");
	}

	private void setPropertyHidden(EditText editText, String check, String input) {
		switch (check) {
		case "":
			editText.setVisibility(View.GONE);
			break;
		case "req":
			editText.setHint(Config.getInstance().getText(input) + " (*)");
			break;
		case "opt":
			editText.setHint(Config.getInstance().getText(input));
			break;
		default:
			break;
		}
	}

	protected void createPrefix(int control) {
		edt_prefix.setHint(Config.getInstance().getText("Prefix") + " (*)");
		if (mAddress != null) {
			String check = mAddress.getPrefix().toLowerCase();
			setPropertyHidden(edt_prefix, check, "Prefix");
		}
	}

	protected void createFullname() {
		if (mAddress != null) {
			String check = mAddress.getName();
			setPropertyHidden(edt_fullname, check, "Full Name");
		}
	}

	protected void createSuffix(int control) {
		edt_suffix.setHint(Config.getInstance().getText("Suffix") + " (*)");
		if (mAddress != null) {
			String check = mAddress.getSuffix().toLowerCase();
			setPropertyHidden(edt_suffix, check, "Suffix");
		}
	}

	protected void createStreet() {
		if (mAddress != null) {
			String check = mAddress.getStreet().toLowerCase();
			setPropertyHidden(edt_street, check, "Street");
		}
	}

	protected void createCity() {
		if (mAddress != null) {
			String check = mAddress.getCity().toLowerCase();
			setPropertyHidden(edt_city, check, "City");
		}
	}

	protected void createZipCode() {
		if (mAddress != null) {
			String check = mAddress.getZipcode().toLowerCase();
			setPropertyHidden(edt_zipcode, check, "Post/Zip Code");
		}
	}

	protected void createPhone() {
		if (mAddress != null) {
			String check = mAddress.getTelephone().toLowerCase();
			setPropertyHidden(edt_phone, check, "Phone");
		}
	}

	protected void createEmail(int control) {
		if (control != Constants.NEW_ADDRESS
				&& control != Constants.NEW_ADDRESS_CHECKOUT) {
			String email = DataLocal.getEmail();
			if (null != email && !email.equals("")
					&& DataLocal.isSignInComplete()) {
				edt_email.setText(email);
				if (mAfterController != Constants.NEW_CUSTOMER
						&& mAfterController != Constants.NEW_AS_GUEST) {
					edt_email.setKeyListener(null);
				}
			} else {
				edt_email.setHint(Config.getInstance().getText("Email")
						+ " (*)");
			}
		} else {
			if (control == Constants.NEW_ADDRESS_CHECKOUT
					&& !DataLocal.isSignInComplete()) {
				edt_email.setHint(Config.getInstance().getText("Email")
						+ " (*)");
			} else {
				String email = DataLocal.getEmail();
				edt_email.setText(email);
				edt_email.setVisibility(View.GONE);
			}
		}
	}

	protected void createDateBirth() {
		if (DataLocal.isSignInComplete()) {
			tv_date_birth.setVisibility(View.GONE);
			return;
		}
		Calendar cDate = Calendar.getInstance();
		final int cDay = cDate.get(Calendar.DAY_OF_MONTH);
		final int cMonth = cDate.get(Calendar.MONTH);
		final int cYear = cDate.get(Calendar.YEAR);
		if (mAddress != null) {
			String check = mAddress.getDob().toLowerCase();

			switch (check) {
			case "":
				tv_date_birth.setVisibility(View.GONE);
				return;
			case "req":
				tv_date_birth.setHint(Config.getInstance().getText(
						"Date of Birth")
						+ " (*):");
				break;
			case "opt":
				tv_date_birth.setHint(Config.getInstance().getText(
						"Date of Birth")
						+ ":");
				break;
			default:
				break;
			}
		}

		final OnDateSetListener onDateSet = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				int sYear = year;
				int sMonth = monthOfYear + 1;
				int sDay = dayOfMonth;
				String selectedDate = new StringBuilder().append(sDay)
						.append("/").append(sMonth).append("/").append(sYear)
						.append(" ").toString();
				String check = mAddress.getDob().toLowerCase();

				switch (check) {
				case "":
					tv_date_birth.setVisibility(View.GONE);
					break;
				case "req":
					tv_date_birth.setText(Config.getInstance().getText(
							"Date of Birth")
							+ " (*): " + selectedDate);
					mSelectedDate = selectedDate;
					break;
				case "opt":
					tv_date_birth.setText(Config.getInstance().getText(
							"Date of Birth")
							+ ": " + selectedDate);
					mSelectedDate = selectedDate;
					break;
				default:
					break;
				}

			}
		};

		tv_date_birth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						mContext, onDateSet, cYear, cMonth, cDay);
				datePickerDialog.show();
			}
		});

	}

	protected void createTaxVat() {
		if (mAddress != null) {
			String check = mAddress.getTaxvat().toLowerCase().trim();
			setPropertyHidden(edt_taxvat, check, "Tax/VAT number");
		}
	}

	protected void createCompany() {
		if (mAddress != null) {
			String check = mAddress.getCompany().toLowerCase();
			setPropertyHidden(edt_company, check, "Company");
		}
	}

	protected void createCountry(int control) {
		if (mAddress != null) {
			String check = mAddress.getCountry().toLowerCase();
			switch (check) {
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

	protected void createState(int control) {
		if (mAddress != null) {
			String check = mAddress.getState().toLowerCase();
			switch (check) {
			case "":
				rl_state.setVisibility(View.GONE);
				edt_state.setVisibility(View.GONE);
				break;
			case "req":
				edt_state
						.setHint(Config.getInstance().getText("State") + "(*)");
				break;
			case "opt":
				edt_state.setHint(Config.getInstance().getText("State"));
				break;
			default:
				break;
			}
		}
	}

	protected void createButtonSave() {
		btn_save.setTextColor(Color.WHITE);
		btn_save.setText(Config.getInstance().getText("Save"));
		btn_save.setTextSize(Constants.SIZE_TEXT_BUTTON);
		btn_save.setBackgroundColor(Config.getInstance().getColorMain());
	}

	protected void createGender(int control) {
		if (DataLocal.isSignInComplete()) {
			rl_gender.setVisibility(View.GONE);
			return;
		}
		sp_gender = (Spinner) rl_gender.findViewById(Rconfig.getInstance().id(
				"sp_gender"));
		tv_gender = (TextView) rl_gender.findViewById(Rconfig.getInstance().id(
				"tv_gender"));
		tv_gender.setText(Config.getInstance().getText("Gender") + " (*):");
		tv_gender.setVisibility(View.VISIBLE);
		GenderAdapter adapter = new GenderAdapter(mContext);
		sp_gender.setAdapter(adapter);
		String check = mAddress.getGender().toLowerCase();
		switch (check) {
		case "":
			rl_gender.setVisibility(View.GONE);
			return;
		case "req":
			tv_gender.setText(Config.getInstance().getText("Gender") + " (*):");
			break;
		case "opt":
			tv_gender.setText(Config.getInstance().getText("Gender") + ":");
			break;
		default:
			break;
		}

		sp_gender.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String gender = "";
				if (position != 0) {
					gender = mAddress.getGenderConfigs().get(position - 1)
							.getLabel();
				}
				mGender = gender;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void createFax(int control) {
		if (mAddress != null) {
			String check = mAddress.getFax().toLowerCase();
			setPropertyHidden(edt_fax, check, "Fax");
		}
	}

	protected void createVatCheckOut() {
		if (mAddress != null) {
			String check = mAddress.getVat_id().toLowerCase();
			setPropertyHidden(edt_tax_checkout, check, "VAT number");
		}
	}

	protected void createPassAndPassConfirm(int control) {
		if (control == Constants.NEW_CUSTOMER) {
			edt_pass.setHint(Config.getInstance().getText("Password") + " (*)");
			edt_confirmPass.setHint(Config.getInstance().getText(
					"Confirm Password")
					+ " (*)");
		} else {
			edt_pass.setVisibility(View.GONE);
			edt_confirmPass.setVisibility(View.GONE);
		}
	}

	@Override
	public MyAddress getNewAddressBook() {
		MyAddress address = new MyAddress();
		// prefix
		String prefix = edt_prefix.getText().toString();
		if (null != prefix && !prefix.equals("")) {
			address.setPrefix(prefix);
		}
		// full name
		String fullname = edt_fullname.getText().toString();
		if (null != fullname && !fullname.equals("")) {
			address.setName(fullname);
		}
		// suffix
		String suffix = edt_suffix.getText().toString();
		if (null != suffix && !suffix.equals("")) {
			address.setSuffix(suffix);
		}
		// street
		String street = edt_street.getText().toString();
		if (null != street && !street.equals("")) {
			address.setStreet(street);
		}
		// city
		String city = edt_city.getText().toString();
		if (null != city && !city.equals("")) {
			address.setCity(city);
		}
		// post ZIP code
		String zipcode = edt_zipcode.getText().toString();
		if (null != zipcode && !zipcode.equals("")) {
			address.setZipCode(zipcode);
		}
		// phone
		String phone = edt_phone.getText().toString();
		if (null != phone && !phone.equals("")) {
			address.setPhone(phone);
		}
		// tax vat
		String taxVat = edt_taxvat.getText().toString();
		if (null != taxVat && !taxVat.equals("")) {
			address.setTaxvat(taxVat);
		}

		// fax
		String fax = edt_fax.getText().toString();
		if (null != fax && !fax.equals("")) {
			address.setFax(fax);
		}

		// tax vat checkout
		String tax_vat_check_out = edt_tax_checkout.getText().toString();
		if (null != tax_vat_check_out && !tax_vat_check_out.equals("")) {
			address.setTaxvatCheckout(tax_vat_check_out);
		}

		// email
		String email = edt_email.getText().toString();
		if (null != email && !email.equals("")) {
			address.setEmail(email);
		}
		// company
		String company = edt_company.getText().toString();
		if (null != company && !company.equals("")) {
			address.setCompany(company);
		}
		// date birth
		String datebirth = mSelectedDate;
		if (null != datebirth && !datebirth.equals("")) {
			String[] arr = datebirth.trim().split("/");
			if (null != arr && arr.length > 0) {
				String day = arr[0];
				if (null != day) {
					address.setDay(day);
				}
				String month = arr[1];
				if (null != month) {
					address.setMonth(month);
				}
				String year = arr[2];
				if (null != year) {
					address.setYear(year);
				}
			}
		}
		// gender
		if (null != mGender && !mGender.equals("") && !mGender.equals("null")) {
			address.setGender(mGender);
		}

		// state
		String state = tv_state.getText().toString();
		if (null != state && !state.equals("") && !state.equals("null")) {
			address.setStateName(state);
		}
		// country
		String country = tv_country.getText().toString();
		if (null != country && !country.equals("") && !country.equals("null")) {
			address.setCountryName(country);
		}

		return address;
	}

	@Override
	public ProfileEntity getProfileEntity() {
		ProfileEntity profile = new ProfileEntity();

		// Name
		String name = edt_fullname.getText().toString();
		if (null != name) {
			profile.setName(name);
		} else {
			return null;
		}

		// email
		String email = edt_email.getText().toString();
		if (null != email) {
			profile.setEmail(email);
		} else {
			return null;
		}

		// password
		String password = edt_pass.getText().toString();
		if (null != password) {
			if (password.length() < 6) {
				SimiManager.getIntance().showNotify(
						Config.getInstance().getText(
								"Please enter 6 or more characters")
								+ ".");
				return null;
			}
			profile.setCurrentPass(password);
		} else {
			return null;
		}

		// confirm password
		String confirmpassword = edt_confirmPass.getText().toString();
		if (null != confirmpassword) {
			profile.setConfirmPass(confirmpassword);
		} else {
			return null;
		}

		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			SimiManager.getIntance().showNotify(null,
					Config.getInstance().getText("Invalid email address"),
					Config.getInstance().getText("OK"));
			return null;

		}
		if (!password.equals(confirmpassword)) {
			SimiManager.getIntance().showNotify(
					null,
					Config.getInstance().getText(
							"Password and Confirm password don't match" + "."),
					Config.getInstance().getText("OK"));
			return null;

		}

		return profile;
	}

	@Override
	public void updateCountry(String country) {
		tv_country.setVisibility(View.VISIBLE);
		if (rl_country.getVisibility() == View.VISIBLE) {
			tv_country.setText(country);
		}
		if (mController != null) {
			mController.setCurrentCountry(country);
		}
	}

	@Override
	public void updateState(String state) {
		if (null != state && !state.equals("")) {

			edt_state.setVisibility(View.GONE);
			if (rl_state.getVisibility() == View.VISIBLE) {
				tv_state.setText(state);
			}
		} else {
			edt_state.setVisibility(View.VISIBLE);
			edt_state.setHint(Config.getInstance().getText("State"));
			rl_state.setVisibility(View.GONE);
		}
		if (mController != null) {
			mController.setCurrentState(state);
		}
	}

	public void setControllerDelegate(ChooseCountryDelegate mController) {
		this.mController = mController;
	}

	@Override
	public Spinner getGender() {
		return sp_gender;
	}

}

package com.simicart.core.customer.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.CountryFragment;
import com.simicart.core.customer.model.AddressBookDetailModel;
import com.simicart.core.customer.model.GetCountryModel;

@SuppressLint("DefaultLocale")
public class AddressBookDetailController extends SimiController implements
		ChooseCountryDelegate {
	protected OnClickListener mClickSave;
	protected AddressBookDetailDelegate mDelegate;
	protected ArrayList<CountryAllowed> country;
	protected OnClickListener mChooseCountry;
	protected OnClickListener mChooseStates;
	protected String mCountry;
	protected ArrayList<String> list_country_adapter;

	public OnClickListener getClickSave() {
		return mClickSave;
	}

	public OnClickListener getChooseCountry() {
		return mChooseCountry;
	}

	public void setDelegate(AddressBookDetailDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	public OnClickListener getChooseStates() {
		return mChooseStates;
	}

	@Override
	public void onStart() {
		onRequestCountryAllowed();

		mClickSave = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hideKeyboard(v);
				MyAddress addressBookDetail = mDelegate.getAddressBookDetail();
				if (isCompleteRequired(addressBookDetail)) {
					OnRequestChangeAddress(addressBookDetail);
				} else {
					SimiManager.getIntance().showNotify(null,
							"Please select all (*) fields", "OK");
				}
			}
		};

		mChooseCountry = new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeFragmentCountry(0, list_country_adapter);
			}
		};

		mChooseStates = new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyAddress addressBookDetail = mDelegate.getAddressBookDetail();
				changeFragmentCountry(
						1,
						getStateFromCountry(addressBookDetail.getCountryName(),
								country));
			}
		};
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
		mDelegate.setListCountry(country);
	}

	protected void onRequestCountryAllowed() {
		mDelegate.showLoading();
		mModel = new GetCountryModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					if (null != entity && entity.size() > 0) {
						country = new ArrayList<CountryAllowed>();
						for (SimiEntity simiEntity : entity) {
							CountryAllowed country_add = (CountryAllowed) simiEntity;
							country.add(country_add);
						}
						list_country_adapter = new ArrayList<String>();
						for (int i = 0; i < country.size(); i++) {
							list_country_adapter.add(country.get(i)
									.getCountry_name());
						}
						mDelegate.setListCountry(country);
						mDelegate.updateView(mModel.getCollection());
					}
				} else {
					SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});

		mModel.request();
	}

	protected void OnRequestChangeAddress(MyAddress address) {
		mDelegate.showLoading();
		mModel = new AddressBookDetailModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					// SimiManager.getIntance().showNotify("SUCCESS", message,
					// "OK");
					AddressBookFragment fragment = AddressBookFragment
							.newInstance();
					SimiManager.getIntance().replacePopupFragment(fragment);

				} else {
					SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});
		// String id = address.getAddressId();
		// if (null != id) {
		// Log.e("AddressBookDetailController ID", id);
		// mModel.addParam("address_id", id);
		// }
		// String name = address.getName();
		// if (null != name) {
		// mModel.addParam("name", name);
		// }
		// String email = address.getEmail();
		// if (null != email) {
		// mModel.addParam("user_email", email);
		// }
		// String street = address.getStreet();
		// if (null != street) {
		// mModel.addParam("street", street);
		// }
		// String city = address.getCity();
		// if (null != city) {
		// mModel.addParam("city", city);
		// }
		// String prefix = address.getPrefix();
		// if (null != prefix) {
		// mModel.addParam("prefix", prefix);
		// }
		// String suffix = address.getSuffix();
		// if (null != suffix) {
		// mModel.addParam("suffix", suffix);
		// }
		// String taxvat = address.getTaxvat();
		// if (null != taxvat) {
		// mModel.addParam("taxvat", taxvat);
		// }
		// String phone = address.getPhone();
		// if (null != phone) {
		// mModel.addParam("phone", phone);
		// }
		// String fax = address.getFax();
		// if (null != phone) {
		// mModel.addParam("fax", fax);
		// }
		// String company = address.getCompany();
		// if (null != company) {
		// mModel.addParam("company", company);
		// }
		// String zipcode = address.getZipCode();
		// if (null != zipcode) {
		// mModel.addParam("zip", zipcode);
		// }
		// String statename = address.getStateName();
		// if (null != statename && !statename.equals("null")
		// && !statename.equals("")) {
		// mModel.addParam("state_name", statename);
		// String statecode = address.getStateCode();
		// if (null != statecode) {
		// mModel.addParam("state_code", statecode);
		//
		// }
		// String stateid = address.getStateId();
		// if (null != stateid) {
		// mModel.addParam("state_id", stateid);
		// }
		// }
		//
		// String countryname = address.getCountryName();
		// if (null != countryname && !countryname.equals("null")
		// && !countryname.equals("")) {
		// mModel.addParam("country_name", countryname);
		// String countrycode = address.getCountryCode();
		// if (null != countrycode) {
		// mModel.addParam("country_code", countrycode);
		// }
		// }

		List<NameValuePair> params = address.toParamsRequest();
		for (NameValuePair nameValuePair : params) {
			String key = nameValuePair.getName();
			String value = nameValuePair.getValue();
			mModel.addParam(key, value);
		}

		mModel.request();
	}

	protected boolean isCompleteRequired(MyAddress addressBookDetail) {
		ConfigCustomerAddress _configCustomer = DataLocal.ConfigCustomerAddress;
		if (addressBookDetail.getName().equals("")
				|| addressBookDetail.getEmail().equals("")) {
			return true;
		} else {

			String prefix = addressBookDetail.getPrefix();
			if (_configCustomer.getPrefix().toLowerCase().equals("req")
					&& (null == prefix || prefix.equals(""))) {
				return false;
			}
			String suffix = addressBookDetail.getSuffix();
			if (_configCustomer.getSuffix().toLowerCase().equals("req")
					&& (null == suffix || suffix.equals(""))) {
				return false;
			}

			String street = addressBookDetail.getStreet();
			if (_configCustomer.getStreet().toLowerCase().equals("req")
					&& (null == street || street.equals(""))) {
				return false;
			}
			String city = addressBookDetail.getCity();
			if (_configCustomer.getCity().toLowerCase().equals("req")
					&& (null == city || city.equals(""))) {
				return false;
			}
			String zipcode = addressBookDetail.getZipCode();
			if (_configCustomer.getZipcode().toLowerCase().equals("req")
					&& (null == zipcode || zipcode.equals(""))) {
				return false;
			}
			String phone = addressBookDetail.getPhone();
			if (_configCustomer.getTelephone().toLowerCase().equals("req")
					&& (null == phone || phone.equals(""))) {
				return false;
			}
			if (_configCustomer.getFax().toLowerCase().equals("req")
					&& (addressBookDetail.getFax() == null || addressBookDetail
							.getFax().equals(""))) {
				return false;
			}
			if (_configCustomer.getVat_id().toLowerCase().equals("req")
					&& (addressBookDetail.getTaxvatCheckout() == null || addressBookDetail
							.getTaxvatCheckout().equals(""))) {
				return false;
			}
			if (_configCustomer.getCompany().toLowerCase().equals("req")
					&& (addressBookDetail.getCompany() == null || addressBookDetail
							.getCompany().equals(""))) {
				return false;
			}
			return true;
		}
	}

	protected void changeFragmentCountry(int type,
			ArrayList<String> list_country) {
		CountryFragment fragment_country = CountryFragment.newInstance(type, list_country);
		fragment_country.setChooseDelegate(this);
//		fragment_country.setList_country(list_country);
//		fragment_country.setType(type);
		SimiManager.getIntance().replacePopupFragment(fragment_country);
	}

	public ArrayList<String> getStateFromCountry(String country,
			ArrayList<CountryAllowed> listCountry) {
		ArrayList<String> states = new ArrayList<String>();
		for (CountryAllowed countryAllowed : listCountry) {
			if (countryAllowed.getCountry_name().equals(country)) {
				for (StateOfCountry state : countryAllowed.getStateList()) {
					states.add(state.getState_name());
				}
				return states;
			}
		}
		return states;
	}

	@Override
	public void chooseCountry(int type, String mCountry) {
		mDelegate.setCountry(type, mCountry, this.country);
	}

	@Override
	public void setCurrentCountry(String country) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setCurrentState(String state) {
		// TODO Auto-generated method stub
	}

}

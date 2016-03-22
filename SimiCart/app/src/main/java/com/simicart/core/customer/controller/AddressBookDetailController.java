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
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
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
	protected String mCurrentCountry = "";
	protected String mCurrentState = "";

	protected int editAddressFor = Constants.KeyAddress.ALL_ADDRESS;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;

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

	protected CountryAllowed getCurrentCountry(String name) {
		if (null != name && null != country)
			for (CountryAllowed ele : country) {
				if (ele.getCountry_name().equals(name)) {
					return ele;
				}
			}

		return null;
	}

	protected StateOfCountry getCurrentState(String name, CountryAllowed country) {
		if (null != name && null != country) {
			ArrayList<StateOfCountry> states = country.getStateList();
			if (null != states && states.size() > 0) {
				for (StateOfCountry state : states) {
					if (state.getState_name().equals(name)) {
						return state;
					}
				}
			}
		}

		return null;
	}

	@Override
	public void onStart() {
		onRequestCountryAllowed();

		mClickSave = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hideKeyboard(v);
				MyAddress address = mDelegate.getAddressBookDetail();
				String state = address.getStateName();
				if (null != state) {
					mCurrentState = state;
				}
				String country = address.getCountryName();
				if (null != country) {
					mCurrentCountry = country;
				}
				CountryAllowed countryAllow = getCurrentCountry(mCurrentCountry);
				if (null != countryAllow) {
					address.setCountryName(mCurrentCountry);
					address.setCountryCode(countryAllow.getCountry_code());
					if (!mCurrentState.equals("")) {
						StateOfCountry stateOfCountry = getCurrentState(
								mCurrentState, countryAllow);
						if (null != stateOfCountry) {
							address.setStateName(mCurrentState);
							address.setStateCode(stateOfCountry.getState_code());
							address.setStateId(stateOfCountry.getState_id());
						}

					}
				}
				if (isCompleteRequired(address)) {
					switch (editAddressFor) {
					case Constants.KeyAddress.BILLING_ADDRESS:
						onEditBillingAddress(address);
						break;
					case Constants.KeyAddress.SHIPPING_ADDRESS:
						onEditShippingAddress(address);
						break;
					default:
						OnRequestChangeAddress(address);
						break;
					}
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

	protected void onEditShippingAddress(MyAddress address) {
		mShippingAddress = address;
		if (mBillingAddress == null) {
			mBillingAddress = address;
		}
		SimiManager.getIntance().replaceFragment(
				ReviewOrderFragment.newInstance(editAddressFor,
						mShippingAddress, mBillingAddress));

	}

	protected void onEditBillingAddress(MyAddress address) {
		mBillingAddress = address;
		if (mShippingAddress == null) {
			mShippingAddress = address;
		}
		SimiManager.getIntance().replaceFragment(
				ReviewOrderFragment.newInstance(editAddressFor,
						mShippingAddress, mBillingAddress));

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
					AddressBookFragment fragment = AddressBookFragment
							.newInstance();
					SimiManager.getIntance().replacePopupFragment(fragment);

				} else {
					SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});

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
		CountryFragment fragment_country = CountryFragment.newInstance(type,
				list_country);
		fragment_country.setChooseDelegate(this);
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

	public void setBillingAddress(MyAddress _BillingAddress) {
		this.mBillingAddress = _BillingAddress;
	}

	public void setShippingAddress(MyAddress _ShippingAddress) {
		this.mShippingAddress = _ShippingAddress;
	}

	public void setEditAddressFor(int _editAddressFor) {
		this.editAddressFor = _editAddressFor;
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

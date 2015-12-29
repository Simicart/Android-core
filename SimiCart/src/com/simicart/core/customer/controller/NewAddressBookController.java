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
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.fragment.AddressBookCheckoutFragment;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.delegate.NewAddressBookDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.ProfileEntity;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.CountryFragment;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.customer.model.GetCountryModel;
import com.simicart.core.customer.model.NewAddressBookModel;

@SuppressLint("DefaultLocale")
public class NewAddressBookController extends SimiController implements
		ChooseCountryDelegate {

	public static int TYPE_SELECT_STATE = 0;
	public static int TYPE_SELECT_COUNTRY = 1;

	protected ArrayList<CountryAllowed> mListCountryAllowed;
	protected OnClickListener mChooseCountry;
	protected OnClickListener mChooseStates, mClickShowGender;
	protected NewAddressBookDelegate mDelegate;
	protected OnClickListener mClickSave;
	protected ArrayList<String> mListCountry;
	protected String mCurrentCountry = "";
	protected String mCurrentState = "";
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;

	public void setBillingAddress(MyAddress mBillingAddress) {
		this.mBillingAddress = mBillingAddress;
	}

	public void setShippingAddress(MyAddress mShippingAddress) {
		this.mShippingAddress = mShippingAddress;
	}

	protected int mAfterController;
	protected int addressFor = -1;

	public void setAddressFor(int addresFor) {
		this.addressFor = addresFor;
	}

	public int getAddressFor() {
		return this.addressFor;
	}

	public void setAfterController(int controll) {
		mAfterController = controll;
	}

	public OnClickListener getClickSave() {
		return mClickSave;
	}

	public OnClickListener getChooseCountry() {
		return mChooseCountry;
	}

	public OnClickListener getChooseStates() {
		return mChooseStates;
	}

	public OnClickListener showGender() {
		return mClickShowGender;
	}

	public void setDelegate(NewAddressBookDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	@Override
	public void onStart() {
		onRequestCountryAllowed();
		mClickShowGender = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDelegate.getGender().performClick();
			}
		};

		mClickSave = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyAddress address = mDelegate.getNewAddressBook();
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
				ReviewOrderFragment fragment = ReviewOrderFragment
						.newInstance();
				Utils.hideKeyboard(arg0);
				if (isCompleteRequired(address)) {
					if (android.util.Patterns.EMAIL_ADDRESS.matcher(
							address.getEmail()).matches()) {
						if (mAfterController == NewAddressBookFragment.NEW_AS_GUEST
								|| mAfterController == NewAddressBookFragment.NEW_ADDRESS_CHECKOUT) {
							fragment.setBilingAddress(address);
							fragment.setShippingAddress(address);
							fragment.setAfterControll(mAfterController);
							SimiManager.getIntance().replacePopupFragment(
									fragment);
						} else if (mAfterController == NewAddressBookFragment.NEW_CUSTOMER) {
							ProfileEntity profile = mDelegate
									.getProfileEntity();
							if (null != profile) {
								String name = profile.getName();
								String email = profile.getEmail();
								String password = profile.getCurrentPass();
								DataLocal.saveData(email, password);
								DataLocal.saveData(name, email, password);
								fragment.setBilingAddress(address);
								fragment.setAfterControll(mAfterController);
								fragment.setShippingAddress(address);
								fragment.setAfterControll(mAfterController);

								if (addressFor == AddressBookCheckoutFragment.BILLING_ADDRESS) {
									fragment.setBilingAddress(address);
									fragment.setShippingAddress(mShippingAddress);
								} else if (addressFor == AddressBookCheckoutFragment.SHIPPING_ADDRESS) {
									fragment.setShippingAddress(address);
									fragment.setBilingAddress(mBillingAddress);
								} else {
									fragment.setShippingAddress(address);
									fragment.setBilingAddress(address);
								}
								SimiManager.getIntance().removeDialog();
								SimiManager.getIntance().replaceFragment(
										fragment);
							}
						} else {
							OnRequestChangeAddress(address);
						}
						ConfigCheckout.getInstance().setStatusAddressBook(true);
					} else {
						SimiManager.getIntance().showNotify(
								null,
								Config.getInstance().getText(
										"Invalid email address"),
								Config.getInstance().getText("OK"));
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
				changeFragmentCountry(TYPE_SELECT_COUNTRY, mListCountry);
			}
		};

		mChooseStates = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> states = getStateFromCountry(mCurrentCountry,
						mListCountryAllowed);
				if (null != states && states.size() > 0) {
					changeFragmentCountry(
							TYPE_SELECT_STATE,
							getStateFromCountry(mCurrentCountry,
									mListCountryAllowed));
				}
			}
		};

		mDelegate.createView(mAfterController);
	}

	@Override
	public void onResume() {
		mDelegate.createView(mAfterController);
		mDelegate.updateView(mModel.getCollection());
		mDelegate.updateCountry(mCurrentCountry);
		mDelegate.updateState(mCurrentState);
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
						mListCountryAllowed = new ArrayList<CountryAllowed>();
						for (SimiEntity simiEntity : entity) {
							CountryAllowed country_add = (CountryAllowed) simiEntity;
							mListCountryAllowed.add(country_add);
						}
						mListCountry = new ArrayList<String>();
						for (int i = 0; i < mListCountryAllowed.size(); i++) {
							mListCountry.add(mListCountryAllowed.get(i)
									.getCountry_name());
						}
						String name = mListCountryAllowed.get(0)
								.getCountry_name();

						if (null != name) {
							mDelegate.updateCountry(name);
							mCurrentCountry = name;
							ArrayList<String> states = getStateFromCountry(
									name, mListCountryAllowed);
							if (null != states && states.size() > 0) {
								mCurrentState = states.get(0);
							} else {
								mCurrentState = "";
							}
							mDelegate.updateState(mCurrentState);
							mDelegate.updateView(mModel.getCollection());
						}
					}
				} else {
					SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});

		mModel.request();
	}

	protected void OnRequestChangeAddress(final MyAddress address) {
		mDelegate.showLoading();
		mModel = new NewAddressBookModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {

					if (mAfterController == NewAddressBookFragment.NEW_ADDRESS) {
						AddressBookFragment fragment = AddressBookFragment
								.newInstance();
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {

						MyAddress newAddress = (MyAddress) mModel
								.getCollection().getCollection().get(0);

						CountryAllowed country = getCurrentCountry(mCurrentCountry);
						if (null != country) {
							newAddress.setCountryName(mCurrentCountry);
							newAddress.setCountryCode(country.getCountry_code());
							if (!mCurrentState.equals("")) {
								StateOfCountry state = getCurrentState(
										mCurrentState, country);
								if (null != state) {
									newAddress.setStateName(mCurrentState);
									newAddress.setStateCode(state
											.getState_code());
									newAddress.setStateId(state.getState_id());
								}
							}
						}

						if (null != newAddress) {
							if (mAfterController == NewAddressBookFragment.NEW_ADDRESS_CHECKOUT) {
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance();
								switch (addressFor) {
								case AddressBookCheckoutFragment.ALL_ADDRESS:
									fragment.setBilingAddress(newAddress);
									fragment.setShippingAddress(newAddress);
									break;
								case AddressBookCheckoutFragment.BILLING_ADDRESS:
									fragment.setBilingAddress(newAddress);
									fragment.setShippingAddress(mShippingAddress);
									break;
								case AddressBookCheckoutFragment.SHIPPING_ADDRESS:
									fragment.setBilingAddress(mBillingAddress);
									fragment.setShippingAddress(newAddress);
									break;
								default:
									break;
								}
								SimiManager.getIntance().replacePopupFragment(
										fragment);
							} else {
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance();
								fragment.setBilingAddress(newAddress);
								fragment.setShippingAddress(newAddress);
								SimiManager.getIntance().replacePopupFragment(
										fragment);
							}
						}
					}
				} else {
					SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});

		CountryAllowed country = getCurrentCountry(mCurrentCountry);
		if (null != country) {
			address.setCountryName(mCurrentCountry);
			address.setCountryCode(country.getCountry_code());
			if (!mCurrentState.equals("")) {
				StateOfCountry state = getCurrentState(mCurrentState, country);
				if (null != state) {
					address.setStateName(mCurrentState);
					address.setStateCode(state.getState_code());
					address.setStateId(state.getState_id());
				}
			}
		}

		List<NameValuePair> params = address.toParamsRequest();
		for (NameValuePair nameValuePair : params) {
			String key = nameValuePair.getName();
			String value = nameValuePair.getValue();
			mModel.addParam(key, value);
		}

		mModel.request();
	}

	protected CountryAllowed getCurrentCountry(String name) {
		if (null != name && null != mListCountryAllowed)
			for (CountryAllowed ele : mListCountryAllowed) {
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

	private boolean isCompleteRequired(MyAddress add_address) {
		// ConfigCustomerAddress _configCustomer =
		// DataLocal.ConfigCustomerAddress;
		// if (mAfterController == NewAddressBookFragment.NEW_ADDRESS_CHECKOUT
		// || mAfterController == NewAddressBookFragment.NEW_AS_GUEST
		// || mAfterController == NewAddressBookFragment.NEW_CUSTOMER) {
		ConfigCustomerAddress _configCustomer = DataLocal.ConfigCustomerAddress;
		// }
		String name = add_address.getName();
		String email = add_address.getEmail();

		if (null == name || name.equals("") || null == email
				|| email.equals("")) {
			return false;
		} else {
			if (_configCustomer.getPrefix().toLowerCase().equals("req")
					&& (add_address.getPrefix() == null || add_address
							.getPrefix().equals(""))) {
				return false;
			}
			if (_configCustomer.getSuffix().toLowerCase().equals("req")
					&& (add_address.getSuffix() == null || add_address
							.getSuffix().equals(""))) {
				return false;
			}

			if (mAfterController == NewAddressBookFragment.NEW_CUSTOMER
					|| mAfterController == NewAddressBookFragment.NEW_AS_GUEST) {
				if (_configCustomer.getTaxvat().toLowerCase().equals("req")
						&& (add_address.getTaxvat() == null || add_address
								.getTaxvat().equals(""))) {
					return false;
				}

				if (_configCustomer.getGender().toLowerCase().equals("req")
						&& (null == add_address.getGender() || add_address
								.getGender().equals(""))) {
					return false;
				}

				if (_configCustomer.getDob().toLowerCase().equals("req")
						&& (null == add_address.getDay() || add_address
								.getDay().equals(""))) {
					return false;
				}

			}

			if (_configCustomer.getStreet().toLowerCase().equals("req")
					&& (add_address.getStreet() == null || add_address
							.getStreet().equals(""))) {
				return false;
			}
			if (_configCustomer.getCity().toLowerCase().equals("req")
					&& (add_address.getCity() == null || add_address.getCity()
							.equals(""))) {
				return false;
			}
			if (_configCustomer.getZipcode().toLowerCase().equals("req")
					&& (add_address.getZipCode() == null || add_address
							.getZipCode().equals(""))) {
				return false;
			}
			if (_configCustomer.getTelephone().toLowerCase().equals("req")
					&& (add_address.getPhone() == null || add_address
							.getPhone().equals(""))) {
				return false;
			}
			if (_configCustomer.getFax().toLowerCase().equals("req")
					&& (add_address.getFax() == null || add_address.getFax()
							.equals(""))) {
				return false;
			}
			if (_configCustomer.getVat_id().toLowerCase().equals("req")
					&& (add_address.getTaxvatCheckout() == null || add_address
							.getTaxvatCheckout().equals(""))) {
				return false;
			}
			if (_configCustomer.getCompany().toLowerCase().equals("req")
					&& (add_address.getCompany() == null || add_address
							.getCompany().equals(""))) {
				return false;
			}
			return true;
		}
	}

	protected void changeFragmentCountry(int type,
			ArrayList<String> list_country) {
		CountryFragment fragment = CountryFragment.newInstance();
		fragment.setChooseDelegate(this);
		fragment.setList_country(list_country);
		fragment.setType(type);
		SimiManager.getIntance().replacePopupFragment(fragment);
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
	public void chooseCountry(int type, String country) {
		if (type == TYPE_SELECT_COUNTRY) {
			mCurrentCountry = country;
			mDelegate.updateCountry(country);
			ArrayList<String> states = getStateFromCountry(country,
					mListCountryAllowed);
			if (null != states && states.size() > 0) {
				mCurrentState = states.get(0);
			} else {
				mCurrentState = "";
			}
			mDelegate.updateState(mCurrentState);

		} else if (type == TYPE_SELECT_STATE) {
			mCurrentState = country;
			mDelegate.updateState(mCurrentState);
		}

	}

	@Override
	public void setCurrentCountry(String country) {
		mCurrentCountry = country;
	}

	@Override
	public void setCurrentState(String state) {
		mCurrentState = state;

	}

}

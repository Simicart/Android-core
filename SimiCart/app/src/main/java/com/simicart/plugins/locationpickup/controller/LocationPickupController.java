package com.simicart.plugins.locationpickup.controller;

import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.controller.NewAddressBookController;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.model.NewAddressBookModel;

public class LocationPickupController extends NewAddressBookController{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void OnRequestChangeAddress(MyAddress address) {
		mDelegate.showLoading();
		mModel = new NewAddressBookModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				
				if (isSuccess) {
					Log.d("duyquang", "==1==");
					if (mAfterController == Constants.NEW_ADDRESS) {
						Log.d("duyquang", "==2==");
						AddressBookFragment fragment = AddressBookFragment
								.newInstance();
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						Log.d("duyquang", "==22==");
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
							MyAddress shippingAdd = null, billingAdd = null;
							if (mAfterController == Constants.NEW_ADDRESS_CHECKOUT) {
								switch (addressFor) {
								case Constants.KeyAddress.ALL_ADDRESS:
									billingAdd = newAddress;
									shippingAdd = newAddress;
									break;
								case Constants.KeyAddress.BILLING_ADDRESS:
									billingAdd =newAddress;
									shippingAdd = mShippingAddress;
									break;
								case Constants.KeyAddress.SHIPPING_ADDRESS:
									billingAdd =mBillingAddress;
									shippingAdd = newAddress;
									break;
								default:
									break;
								}
								Log.d("duyquang", "==3==");
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance(mAfterController, shippingAdd, billingAdd);
//								switch (addressFor) {
//								case AddressBookCheckoutFragment.ALL_ADDRESS:
//									fragment.setBilingAddress(newAddress);
//									fragment.setShippingAddress(newAddress);
//									break;
//								case AddressBookCheckoutFragment.BILLING_ADDRESS:
//									fragment.setBilingAddress(newAddress);
//									fragment.setShippingAddress(mShippingAddress);
//									break;
//								case AddressBookCheckoutFragment.SHIPPING_ADDRESS:
//									fragment.setBilingAddress(mBillingAddress);
//									fragment.setShippingAddress(newAddress);
//									break;
//								default:
//									break;
//								}
								SimiManager.getIntance().replacePopupFragment(
										fragment);
							} else {
								billingAdd = newAddress;
								shippingAdd = newAddress;
								Log.d("duyquang", "==4==");
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance(-1, shippingAdd, billingAdd);
//								fragment.setBilingAddress(newAddress);
//								fragment.setShippingAddress(newAddress);
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

		String lat = address.getBundle().getString("lat");
		String lng = address.getBundle().getString("long");
		if(!lat.equals("") && !lng.equals("")){
			mModel.addParam("latlng", lat + ", " + lng);
		}
		mModel.request();
	}
}

package com.simicart.plugins.locationpickup.controller;

import java.util.List;

import org.apache.http.NameValuePair;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.model.AddressBookDetailModel;

public class LocationPickupEditController extends AddressBookDetailController {
	@Override
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

		String lat = address.getBundle().getString("lat");
		String lng = address.getBundle().getString("long");
		if (!lat.equals("") && !lng.equals("")) {
			mModel.addParam("latlng", lat + "," + lng);
		}

		mModel.request();
	}
}

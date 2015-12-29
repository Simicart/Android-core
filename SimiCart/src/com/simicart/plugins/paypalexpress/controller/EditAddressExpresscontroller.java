package com.simicart.plugins.paypalexpress.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.plugins.paypalexpress.fragment.FragmentAddress;
import com.simicart.plugins.paypalexpress.fragment.FragmentEditAddressExpress;

public class EditAddressExpresscontroller extends AddressBookDetailController {

	int addressType;
	MyAddress addressbookTemp;

	public void setAddressbookTemp(MyAddress addressbookTemp) {
		this.addressbookTemp = addressbookTemp;
	}

	public void setAddressType(int addressType) {
		this.addressType = addressType;
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
					if (addressType == FragmentEditAddressExpress.BILLING) {
						FragmentAddress fragmentAddress = new FragmentAddress(
								addressbookTemp, addressBookDetail);
						SimiManager.getIntance().replaceFragment(
								fragmentAddress);
					} else {
						FragmentAddress fragmentAddress = new FragmentAddress(
								addressBookDetail, addressbookTemp);
						SimiManager.getIntance().replaceFragment(
								fragmentAddress);
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
}

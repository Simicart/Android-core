package com.simicart.plugins.paypalexpress.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.plugins.paypalexpress.controller.EditAddressExpresscontroller;

public class FragmentEditAddressExpress extends AddressBookDetailFragment {

	public static int SHIPPING = 1;
	public static int BILLING = 2;
	int addressType;
	MyAddress addressbookTemp;
	EditAddressExpresscontroller mController;

	public void setBillingAddressbook(MyAddress addressbook) {
		this.addressbook = addressbook;
		this.addressType = BILLING;
	}

	public void setAddressbookTemp(MyAddress addressbookTemp) {
		this.addressbookTemp = addressbookTemp;
	}

	public void setShippingAddressbook(MyAddress addressbook) {
		this.addressbook = addressbook;
		this.addressType = SHIPPING;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_new_address_layout"),
				container, false);
		Context context = getActivity();

		mBlock = new AddressBookDetailBlock(view, context);
		mBlock.setAddressBookDetail(addressbook);
		Log.e("FragmentEditAddressExpress : ",
				"ID + " + addressbook.getAddressId());
		// Log.e("FragmentEditAddressExpress : ", "JSON "
		// + addressbook.getJSONObject().toString());
		mBlock.initView();

		if (null == mController) {
			mController = new EditAddressExpresscontroller();
			mController.setDelegate(mBlock);
			mController.setAddressbookTemp(addressbookTemp);
			mController.setAddressType(addressType);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.setAddressbookTemp(addressbookTemp);
			mController.setAddressType(addressType);
			mController.onResume();
		}

		mBlock.setSaveClicker(mController.getClickSave());
		mBlock.setChooseCountry(mController.getChooseCountry());
		mBlock.setChooseStates(mController.getChooseStates());
		return view;
	}
}

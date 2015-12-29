package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.entity.MyAddress;

public class AddressBookDetailFragment extends SimiFragment {
	protected MyAddress addressbook;
	protected AddressBookDetailBlock mBlock;
	protected AddressBookDetailController mController;

	public void setAddressbook(MyAddress addressbook) {
		this.addressbook = addressbook;
	}
	
	public MyAddress getAddressbook() {
		return addressbook;
	}

	public static AddressBookDetailFragment newInstance() {
		AddressBookDetailFragment fragment = new AddressBookDetailFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_new_address_layout"),
				container, false);
		if (DataLocal.isLanguageRTL) {
			view = inflater
					.inflate(
							Rconfig.getInstance().layout(
									"rtl_core_new_address_layout"), container,
							false);
		}
		Context context = getActivity();

		mBlock = new AddressBookDetailBlock(view, context);
		mBlock.setAddressBookDetail(addressbook);
		mBlock.initView();

		if (null == mController) {
			mController = new AddressBookDetailController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setSaveClicker(mController.getClickSave());
		mBlock.setChooseCountry(mController.getChooseCountry());
		mBlock.setChooseStates(mController.getChooseStates());
		return view;
	}
}

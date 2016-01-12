package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.NewAddressBookBlock;
import com.simicart.core.customer.controller.NewAddressBookController;
import com.simicart.core.customer.entity.MyAddress;

public class NewAddressBookFragment extends SimiFragment {
	// click add new address from my account
	public static final int NEW_ADDRESS = 0;
	// click as new customer
	public static final int NEW_CUSTOMER = 1;
	// click check out as guest
	public static final int NEW_AS_GUEST = 2;
	// click new address from check out
	public static final int NEW_ADDRESS_CHECKOUT = 3;

	public static NewAddressBookFragment newInstance() {
		NewAddressBookFragment fragment = new NewAddressBookFragment();
		return fragment;
	}

	protected NewAddressBookBlock mBlock;
	protected NewAddressBookController mController;
	protected int addressFor = -1;

	public int afterControl = NEW_ADDRESS;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;

	public void setBillingAddress(MyAddress mBillingAddress) {
		this.mBillingAddress = mBillingAddress;
	}

	public void setShippingAddress(MyAddress mShippingAddress) {
		this.mShippingAddress = mShippingAddress;
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
		mBlock = new NewAddressBookBlock(view, context);
		mBlock.setAfterController(afterControl);
		mBlock.initView();
		if (null == mController) {
			mController = new NewAddressBookController();
			mController.setDelegate(mBlock);
			mController.setAfterController(afterControl);
			mController.setAddressFor(this.addressFor);
			mController.setBillingAddress(mBillingAddress);
			mController.setShippingAddress(mShippingAddress);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setControllerDelegate(mController);
		mBlock.setSaveAddress(mController.getClickSave());
		mBlock.setChooseCountry(mController.getChooseCountry());
		mBlock.setChooseStates(mController.getChooseStates());
		mBlock.setOnclickTextviewGender(mController.showGender());
		mBlock.setOnclickImageGender(mController.showGender());
		return view;                                                                               
	}

	public void setAfterControler(int afterControl) {
		this.afterControl = afterControl;
	}
	
	public int getAfterControl() {
		return afterControl;
	}

	public void setAddressFor(int addressFor) {
		this.addressFor = addressFor;
	}
}

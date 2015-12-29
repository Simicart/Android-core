package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.checkout.controller.AddressBookCheckoutController;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookBlock;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookFragment;

public class AddressBookCheckoutFragment extends AddressBookFragment {
	public final static int ALL_ADDRESS = 0;
	public final static int BILLING_ADDRESS = 1;
	public final static int SHIPPING_ADDRESS = 2;
	protected int mAfterController;


	public static AddressBookCheckoutFragment newInstance() {
		AddressBookCheckoutFragment fragment = new AddressBookCheckoutFragment();
		return fragment;
	}

	
	protected AddressBookBlock mBlock;
	protected AddressBookCheckoutController mController;

	protected int addressFor = ALL_ADDRESS;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;
	
	public void setAfterController(int afterController) {
		mAfterController = afterController;
	}

	public void setAddressFor(int addressFor) {
		this.addressFor = addressFor;
	}

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
				Rconfig.getInstance().layout("core_address_book_layout"),
				container, false);
		Context context = getActivity();

		mBlock = new AddressBookBlock(view, context);
		mBlock.setIsCheckout(true);
		mBlock.initView();

		if (null == mController) {
			mController = new AddressBookCheckoutController();
			mController.setDelegate(mBlock);
			mController.setAddressFor(addressFor);
			mController.setBillingAddress(mBillingAddress);
			mController.setShippingAddress(mShippingAddress);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setOnItemClicker(mController.getItemClicker());
		mBlock.setonTouchListener(mController.getListener());

		return view;

	}
}

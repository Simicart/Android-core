package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.checkout.controller.AddressBookCheckoutController;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookBlock;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookFragment;

public class AddressBookCheckoutFragment extends AddressBookFragment {
	public final static int ALL_ADDRESS = 0;
	public final static int BILLING_ADDRESS = 1;
	public final static int SHIPPING_ADDRESS = 2;
	protected int mAfterController;


	public static AddressBookCheckoutFragment newInstance(int afterControl, int addressFor, MyAddress billingAddress, MyAddress shippingAddress) {
		AddressBookCheckoutFragment fragment = new AddressBookCheckoutFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.AFTER_CONTROL, afterControl, Constants.KeyData.TYPE_INT, bundle);
		setData(Constants.KeyData.ADDRESS_FOR, addressFor, Constants.KeyData.TYPE_INT, bundle);
//		setData(Constants.KeyData.BILLING_ADDRESS, billingAddress, Constants.KeyData.TYPE_LIST_STRING, bundle);
		bundle.putSerializable(Constants.KeyData.BILLING_ADDRESS, billingAddress);
//		setData(Constants.KeyData.SHIPPING_ADDRESS, shippingAddress, Constants.KeyData.TYPE_LIST_STRING, bundle);
		bundle.putSerializable(Constants.KeyData.SHIPPING_ADDRESS, shippingAddress);
		fragment.setArguments(bundle);
		return fragment;
	}
	public static AddressBookCheckoutFragment newInstance() {
		AddressBookCheckoutFragment fragment = new AddressBookCheckoutFragment();
	
		return fragment;
	}


	
	protected AddressBookBlock mBlock;
	protected AddressBookCheckoutController mController;

	protected int addressFor = ALL_ADDRESS;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;
	
//	public void setAfterController(int afterController) {
//		mAfterController = afterController;
//	}
//
//	public void setAddressFor(int addressFor) {
//		this.addressFor = addressFor;
//	}
//
//	public void setBillingAddress(MyAddress mBillingAddress) {
//		this.mBillingAddress = mBillingAddress;
//	}
//
//	public void setShippingAddress(MyAddress mShippingAddress) {
//		this.mShippingAddress = mShippingAddress;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_address_book_layout"),
				container, false);
		Context context = getActivity();

		//getdata
		if(getArguments() != null){
			mAfterController = (int) getData(Constants.KeyData.AFTER_CONTROL, Constants.KeyData.TYPE_INT, getArguments());
			addressFor = (int) getData(Constants.KeyData.ADDRESS_FOR, Constants.KeyData.TYPE_INT, getArguments());
			mBillingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.BILLING_ADDRESS);
			mShippingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.SHIPPING_ADDRESS);
		}
				
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

package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.NewAddressBookBlock;
import com.simicart.core.customer.controller.NewAddressBookController;
import com.simicart.core.customer.entity.MyAddress;

public class NewAddressBookFragment extends SimiFragment {
	
	protected NewAddressBookBlock mBlock;
	protected NewAddressBookController mController;
	protected int addressFor = -1;

	public int afterControl;// = NEW_ADDRESS;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;

	public static NewAddressBookFragment newInstance(int afterControl, int addressFor, MyAddress billingAddress, MyAddress shippingAddress) {
		NewAddressBookFragment fragment = new NewAddressBookFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.AFTER_CONTROL, afterControl, Constants.KeyData.TYPE_INT, bundle);
		setData(Constants.KeyData.ADDRESS_FOR, addressFor, Constants.KeyData.TYPE_INT, bundle);
		bundle.putSerializable(Constants.KeyData.BILLING_ADDRESS, billingAddress);
		bundle.putSerializable(Constants.KeyData.SHIPPING_ADDRESS, shippingAddress);
		fragment.setArguments(bundle);
		Log.d("quang123", "NewAddressBookFragment=newInstance=afterControl==" +afterControl);
		return fragment;
	}
	
	public static NewAddressBookFragment newInstance() {
		NewAddressBookFragment fragment = new NewAddressBookFragment();
		
		return fragment;
	}
	
	public int getAfterControl() {
		return afterControl;
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
		Log.d("quang123", "NewAddressBookFragment==getArguments==afterControl==");
		//getdata
		if(getArguments() != null){
		afterControl = (int) getData(Constants.KeyData.AFTER_CONTROL, Constants.KeyData.TYPE_INT, getArguments());
		addressFor = (int) getData(Constants.KeyData.ADDRESS_FOR, Constants.KeyData.TYPE_INT, getArguments());
		mBillingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.BILLING_ADDRESS);
		mShippingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.SHIPPING_ADDRESS);
		}
		Log.d("quang123", "NewAddressBookFragment==getArguments==afterControl==" +afterControl);
		
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

}

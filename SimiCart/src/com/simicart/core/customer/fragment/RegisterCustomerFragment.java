package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.RegisterCustomerBlock;
import com.simicart.core.customer.controller.RegisterCustomerController;

public class RegisterCustomerFragment extends SimiFragment {
	protected RegisterCustomerBlock mBlock;
	protected RegisterCustomerController mController;
	protected boolean isCheckout = false;

	public static RegisterCustomerFragment newInstance() {
		RegisterCustomerFragment fragment = new RegisterCustomerFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_register_customer"),
				container, false);
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_core_register_customer"),
					container, false);
		}
		Context context = getActivity();
		mBlock = new RegisterCustomerBlock(view, context);
		mBlock.initView();
		if (null == mController) {
			mController = new RegisterCustomerController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setOnClickRelativeLayout(mController.getOnClickRelative());
		mBlock.setRegisterClick(mController.getOnclickRegister());
		mBlock.setOnClickTextViewGender(mController.getOnClickRelative());
		return view;
	}
}

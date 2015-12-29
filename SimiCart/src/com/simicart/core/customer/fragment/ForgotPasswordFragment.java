package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.ForgotPasswordBlock;
import com.simicart.core.customer.controller.ForgotPasswordController;

public class ForgotPasswordFragment extends SimiFragment {

	protected ForgotPasswordController mController;
	protected ForgotPasswordBlock mBlock;

	public static ForgotPasswordFragment newInstance() {
		ForgotPasswordFragment fragment = new ForgotPasswordFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_forgotpassword_layout"),
				container, false);
		Context context = getActivity();
		mBlock = new ForgotPasswordBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new ForgotPasswordController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setOnClicker(mController.getClicker());
		return view;
	}

}

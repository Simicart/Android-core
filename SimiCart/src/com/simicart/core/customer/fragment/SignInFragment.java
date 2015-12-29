package com.simicart.core.customer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.SignInBlock;
import com.simicart.core.customer.controller.SignInController;

public class SignInFragment extends SimiFragment {

	protected SignInBlock mBlock;
	protected SignInController mController;
	protected boolean isCheckout = false;// sign in into checkout
	protected String mEmail = "";
	protected String mPassword = "";
	protected int requestCode;
	protected int resultCode;
	protected Intent data;

	public static SignInFragment newInstance() {
		SignInFragment fragment = new SignInFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_sign_in_layout"), container,
				false);
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_core_sign_in_layout"),
					container, false);
		}
		Context context = getActivity();

		mBlock = new SignInBlock(view, context);
		mBlock.setEmail(mEmail);
		mBlock.setPassword(mPassword);
		mBlock.initView();

		if (null == mController) {
			mController = new SignInController();
			mController.setDelegate(mBlock);
			mController.setCheckout(isCheckout);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setSingInClicker(mController.getSignInClicker());
		mBlock.setForgotPassClicker(mController.getForgotPassClicker());
		mBlock.setCreateAccClicker(mController.getCreateAccClicker());
		mBlock.setOutSideClicker(mController.getOutSideClicker());
		mBlock.setEmailWatcher(mController.getEmailWatcher());
		mBlock.setPasswordWatcher(mController.getPassWatcher());
		mBlock.setOnCheckBox(mController.getOnCheckBox());
		return view;
	}

	public void setCheckout(boolean isCheckout) {
		this.isCheckout = isCheckout;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public SignInController getController() {
		return mController;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public Intent getData() {
		return data;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		this.requestCode = requestCode;
		this.resultCode = resultCode;
		this.data = data;

		SimiManager.getIntance().eventFragment(this);
	}
}

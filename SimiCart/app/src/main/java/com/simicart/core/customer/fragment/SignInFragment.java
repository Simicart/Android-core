package com.simicart.core.customer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Constants;
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

	public static SignInFragment newInstance(String email, String pass, boolean checkout) {
		SignInFragment fragment = new SignInFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.EMAIL, email, Constants.KeyData.TYPE_STRING, bundle);
		setData(Constants.KeyData.PASSWORD, pass, Constants.KeyData.TYPE_STRING, bundle);
		setData(Constants.KeyData.CHECK_BOO, checkout, Constants.KeyData.TYPE_BOOLEAN, bundle);
		fragment.setArguments(bundle);
		return fragment;
	}
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
		View view = null;
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_core_sign_in_layout"),
					container, false);
		}else {
			view = inflater.inflate(
					Rconfig.getInstance().layout("core_sign_in_layout"), container,
					false);
		}
		Context context = getActivity();
		
		//getdata
		if(getArguments() != null){
		mEmail = (String) getData(Constants.KeyData.EMAIL, Constants.KeyData.TYPE_STRING, getArguments());
		mPassword = (String) getData(Constants.KeyData.PASSWORD, Constants.KeyData.TYPE_STRING, getArguments());
		isCheckout = (boolean) getData(Constants.KeyData.CHECK_BOO, Constants.KeyData.TYPE_BOOLEAN, getArguments());
		}
		mBlock = new SignInBlock(view, context);
		if( mEmail != null){
		mBlock.setEmail(mEmail);
		}
		if(mPassword != null){
		mBlock.setPassword(mPassword);
		}
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
		mBlock.setVisibleSignIn(mController.isVisibleSignIn());
		return view;
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

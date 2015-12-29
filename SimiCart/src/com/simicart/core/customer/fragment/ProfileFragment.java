package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.ProfileBlock;
import com.simicart.core.customer.controller.ProfileController;

public class ProfileFragment extends SimiFragment {

	protected ProfileBlock mBlock;
	protected ProfileController mController;

	public static ProfileFragment newInstance() {
		ProfileFragment fragment = new ProfileFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_profile_layout"), container,
				false);
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_profile_layout"),
					container, false);
		}
		Context context = getActivity();
		mBlock = new ProfileBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new ProfileController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setSaveClicker(mController.getSaveClicker());
		mBlock.setShowCurrentPass(mController.getOnTouchCurrentPass());
		mBlock.setShowNewPass(mController.getOnTouchNewPass());
		mBlock.setShowConfirmPass(mController.getOnTouchConfirmPass());
		mBlock.setClickImageGender(mController.getOnclickGenderImage());
		return view;
	}

}

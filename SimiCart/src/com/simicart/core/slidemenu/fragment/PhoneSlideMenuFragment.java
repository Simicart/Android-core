package com.simicart.core.slidemenu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.block.PhoneSlideMenuBlock;
import com.simicart.core.slidemenu.controller.PhoneSlideMenuController;
import com.simicart.core.slidemenu.delegate.CloseSlideMenuDelegate;

public class PhoneSlideMenuFragment extends SimiFragment {

	protected PhoneSlideMenuController mController;
	protected PhoneSlideMenuBlock mBlock;
	protected CloseSlideMenuDelegate mCloseDelegate;

	public void setCloseDelegate(CloseSlideMenuDelegate delegate) {
		mCloseDelegate = delegate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_phone_slide_menu"), null,
				false);
		view.setBackgroundColor(Config.getInstance().getMenu_background());
		Context context = getActivity();
		mBlock = new PhoneSlideMenuBlock(view, context);
		mBlock.initView();
		mController = new PhoneSlideMenuController(mBlock, context);
		mController.setCloseDelegate(mCloseDelegate);
		mController.create();
		mBlock.setListener(mController.getListener());
		mBlock.setClickerPersonal(mController.getOnClickPersonal());

		SimiManager.getIntance().setSlideMenuController(mController);

		return view;
	}
}

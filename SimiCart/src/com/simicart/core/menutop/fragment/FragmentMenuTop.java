package com.simicart.core.menutop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.menutop.block.MenuTopBlock;
import com.simicart.core.menutop.controller.MenuTopController;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

public class FragmentMenuTop extends SimiFragment {

	protected View rootView;
	protected MenuTopBlock mBlock;
	protected MenuTopController mController;
	protected SlideMenuFragment mNavigationDrawerFragment;

	public static FragmentMenuTop newInstance(
			SlideMenuFragment mNavigationDrawerFragment) {
		FragmentMenuTop fragment = new FragmentMenuTop();
		fragment.mNavigationDrawerFragment = mNavigationDrawerFragment;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(Rconfig.getInstance()
				.layout("core_menutop"), container, false);
		rootView.setBackgroundColor(Config.getInstance().getKey_color());

		Context mContext = getActivity();

		mBlock = new MenuTopBlock(rootView, mContext);
		mBlock.initView();

		if (null == mController) {
			mController = new MenuTopController();
			mController.setDelegate(mBlock);
			mController.setSlideMenu(mNavigationDrawerFragment);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.setSlideMenu(mNavigationDrawerFragment);
			mController.onResume();
		}

		mBlock.setOnTouchCart(mController.getTouchCart());
		mBlock.setOnTouchMenu(mController.getTouchMenu());
		SimiManager.getIntance().setMenuTopController(mController);

		return rootView;
	}
}

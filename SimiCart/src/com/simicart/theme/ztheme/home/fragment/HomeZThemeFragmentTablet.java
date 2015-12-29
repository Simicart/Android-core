package com.simicart.theme.ztheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.ztheme.home.block.HomeZThemeBlockTablet;
import com.simicart.theme.ztheme.home.controller.HomeZThemeControllerTablet;

public class HomeZThemeFragmentTablet extends HomeZThemeFragment {

	protected HomeZThemeControllerTablet mController;
	protected HomeZThemeBlockTablet mBlock;

	public static HomeZThemeFragmentTablet newInstance() {
		HomeZThemeFragmentTablet fragment = new HomeZThemeFragmentTablet();
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("ztheme_home_layout"), null);

		Context context = getActivity();

		SimiManager.getIntance().setChildFragment(getChildFragmentManager());

		mBlock = new HomeZThemeBlockTablet(rootView, context);
		mBlock.initView();
		if (null == mController) {
			mController = new HomeZThemeControllerTablet();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setCategoryItemClickListener(mController.getOnClickItemCat());

		return rootView;
	}

	// public void setSlideMenu(SlideMenuFragment mNavigationDrawerFragment) {
	// this.mNavigationDrawerFragment = mNavigationDrawerFragment;
	// }
}

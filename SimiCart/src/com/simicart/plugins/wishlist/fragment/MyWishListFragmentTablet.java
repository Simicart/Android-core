package com.simicart.plugins.wishlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.block.MyWishListBlockTablet;
import com.simicart.plugins.wishlist.controller.MyWishListController;

public class MyWishListFragmentTablet extends SimiFragment {

	protected MyWishListBlockTablet mBlock;
	protected MyWishListController mController;

	public static MyWishListFragmentTablet newInstance() {
		MyWishListFragmentTablet fragment = new MyWishListFragmentTablet();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_wishlist_mywishlist"),
				container, false);
		Context context = getActivity();

		mBlock = new MyWishListBlockTablet(view, context);

		mBlock.initView();
		if (null == mController) {
			mController = new MyWishListController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		// mBlock.setShareListener(mController.getTabletShareListener());
		return view;
	}
}

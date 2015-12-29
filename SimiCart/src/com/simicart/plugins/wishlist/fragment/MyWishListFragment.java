package com.simicart.plugins.wishlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.block.MyWistListBlock;
import com.simicart.plugins.wishlist.controller.MyWishListController;

public class MyWishListFragment extends SimiFragment {
	protected MyWistListBlock mBlock;
	protected MyWishListController mController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_wishlist_fragment_mywishlist"),
				container, false);
		Context context = getActivity();
		mBlock = new MyWistListBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new MyWishListController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		
		mBlock.setShareListener(mController.getShareListener());

		return view;
	}
}

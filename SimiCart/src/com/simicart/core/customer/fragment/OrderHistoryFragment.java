package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.OrderHistoryBlock;
import com.simicart.core.customer.controller.OrderHistoryController;

public class OrderHistoryFragment extends SimiFragment {

	protected OrderHistoryBlock mBlock;
	protected OrderHistoryController mController;

	public static OrderHistoryFragment newInstance() {
		OrderHistoryFragment fragment = new OrderHistoryFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("Order History Screen");
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_order_history_layout"),
				container, false);

		Context context = getActivity();
		SimiManager.getIntance().setChildFragment(getChildFragmentManager());
		mBlock = new OrderHistoryBlock(rootView, context);
		mBlock.initView();

		if (null == mController) {
			mController = new OrderHistoryController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setItemClicker(mController.getItemClicker());
		mBlock.setScrollListener(mController.getScrollListener());

		return rootView;
	}

}

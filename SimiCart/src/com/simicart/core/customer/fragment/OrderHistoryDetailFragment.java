package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.OrderHistoryDetailBlock;
import com.simicart.core.customer.controller.OrderHistoryDetailController;

public class OrderHistoryDetailFragment extends SimiFragment {

	protected String mID;
	protected OrderHistoryDetailBlock mBlock;
	protected OrderHistoryDetailController mController;
	

	public void setID(String id) {
		mID = id;
	}

	public static OrderHistoryDetailFragment newInstance() {
		OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
		return fragment;
	}
	public static OrderHistoryDetailFragment newInstance(int targer) {
		OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
		fragment.setTargetFragment(fragment, targer);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("Order History Detail Screen");
		if (DataLocal.isLanguageRTL) {
			rootView = inflater
					.inflate(
							Rconfig.getInstance().layout(
									"rtl_order_his_detail_layout"), container,
							false);
		} else {
			rootView = inflater.inflate(
					Rconfig.getInstance()
							.layout("core_order_his_detail_layout"), container,
					false);
		}
		Context context = getActivity();
		mBlock = new OrderHistoryDetailBlock(rootView, context);
		mBlock.initView();
		if (null == mController) {
			mController = new OrderHistoryDetailController();
			mController.setID(mID);
			mController.setDelegate(mBlock);
			mController.setReOrderDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.setReOrderDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setOnTouchReOrder(mController.getReOrderClicker());
		return rootView;
	}

}

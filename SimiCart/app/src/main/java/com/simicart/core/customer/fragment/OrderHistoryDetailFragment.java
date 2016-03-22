package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.OrderHistoryDetailBlock;
import com.simicart.core.customer.controller.OrderHistoryDetailController;

public class OrderHistoryDetailFragment extends SimiFragment {

	protected String mID;
	protected OrderHistoryDetailBlock mBlock;
	protected OrderHistoryDetailController mController;

	public static OrderHistoryDetailFragment newInstance(int targer, String id) {
		OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
		if (targer != 0) {
			fragment.setTargetFragment(fragment, targer);
		}
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.ID, id, Constants.KeyData.TYPE_STRING, bundle);
		fragment.setArguments(bundle);
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
		if(getArguments() != null){
		mID = (String) getData(Constants.KeyData.ID,
				Constants.KeyData.TYPE_STRING, getArguments());
		}

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

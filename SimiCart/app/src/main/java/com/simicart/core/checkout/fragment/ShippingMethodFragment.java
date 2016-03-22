package com.simicart.core.checkout.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.block.ShippingMethodBlock;
import com.simicart.core.checkout.controller.ShippingMethodController;
import com.simicart.core.checkout.delegate.SelectedShippingMethodDelegate;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.config.Rconfig;

public class ShippingMethodFragment extends SimiFragment {

	protected ArrayList<ShippingMethod> mListShipping;
	protected ShippingMethodBlock mBlock;
	protected ShippingMethodController mController;
	protected SelectedShippingMethodDelegate mSeletecdDelegate;

	public static ShippingMethodFragment newInstance() {
		ShippingMethodFragment fragment = new ShippingMethodFragment();
		return fragment;
	}

	public void setSelectedShippingMethodDelegate(
			SelectedShippingMethodDelegate delegate) {
		mSeletecdDelegate = delegate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_shipping_method_layout"),
				container, false);
		Context context = getActivity();
		mBlock = new ShippingMethodBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new ShippingMethodController();
			mController.setDelegate(mBlock);
			mController.setSelectedShippingMethodDelegate(mSeletecdDelegate);
			mController.setListShipping(mListShipping);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setItemClicker(mController.getItemClicker());

		return view;
	}

}

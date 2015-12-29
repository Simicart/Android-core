package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.block.CartBlock;
import com.simicart.core.checkout.controller.CartController;
import com.simicart.core.config.Rconfig;

public class CartFragment extends SimiFragment {

	protected CartController mController;
	protected CartBlock mBlock;

	public static CartFragment newInstance() {
		CartFragment fragment = new CartFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimiManager.getIntance().showCartLayout(false);
	}

	@Override
	public void onStart() {
		SimiManager.getIntance().showCartLayout(false);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("Cart Screen");
		SimiManager.getIntance().showCartLayout(false);
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_cart_layout"), container,
				false);
		Context context = getActivity();
		mBlock = new CartBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new CartController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		return view;
	}

	@Override
	public void onDestroy() {
		SimiManager.getIntance().showCartLayout(true);
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		SimiManager.getIntance().showCartLayout(true);
	}

	@Override
	public void onResume() {
		SimiManager.getIntance().showCartLayout(false);
		super.onResume();
	}
}

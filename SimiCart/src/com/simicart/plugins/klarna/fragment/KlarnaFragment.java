package com.simicart.plugins.klarna.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.klarna.block.KlarnaBlock;
import com.simicart.plugins.klarna.controller.KlarnaController;

public class KlarnaFragment extends SimiFragment {

	public static String URL_CHECKOUT_KLARNA = "simiklarna/api/checkout/data/";
	public static String URL_GETPARAMS_KLARNA = "simiklarna/api/get_params";
	public static String URL_PUSH_KLARNA = "simiklarna/api/push/";

	protected KlarnaBlock mBlock;
	protected KlarnaController mController;

	public static KlarnaFragment newInstance() {
		KlarnaFragment fragment = new KlarnaFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_webview_layout"), null,
				false);
		Context context = getActivity();
		mBlock = new KlarnaBlock(view, context);
		mBlock.initView();
		if (null == mController) {
			mController = new KlarnaController();
			mController.setKlarnaDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setKlarnaDelegate(mBlock);
			mController.onResume();
		}
		return view;
	}

}

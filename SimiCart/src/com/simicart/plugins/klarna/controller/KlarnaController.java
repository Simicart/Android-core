package com.simicart.plugins.klarna.controller;

import org.json.JSONArray;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.plugins.klarna.delegate.KlarnaDelegate;
import com.simicart.plugins.klarna.model.KlarnaModel;

public class KlarnaController extends SimiController {

	protected KlarnaDelegate mDelegate;
	protected JSONArray json;

	public void setKlarnaDelegate(KlarnaDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {
		requestGetParam();
	}

	protected void requestGetParam() {
		final KlarnaModel model = new KlarnaModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					json = model.getDataKlarna();
					onLoadWebView();
				} else {

				}

			}
		});
		model.request();
	}

	protected void onLoadWebView() {
		mDelegate.onLoadWebView(json);
	}

	@Override
	public void onResume() {
		onLoadWebView();

	}

}

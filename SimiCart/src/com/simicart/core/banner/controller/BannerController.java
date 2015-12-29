package com.simicart.core.banner.controller;

import com.simicart.core.banner.delegate.BannerDelegate;
import com.simicart.core.banner.model.BannerModel;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;

public class BannerController extends SimiController {
	protected BannerDelegate mDelegate;

	public BannerController(BannerDelegate delegate) {
		mDelegate = delegate;
	}

	public void setDelegate(BannerDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {
		// mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				// mDelegate.dismissLoading();
				mDelegate.updateView(mModel.getCollection());

			}
		};
		mModel = new BannerModel();
		mModel.setDelegate(delegate);
		mModel.addParam("limit", "10");
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

}

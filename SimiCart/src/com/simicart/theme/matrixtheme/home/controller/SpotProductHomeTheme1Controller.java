package com.simicart.theme.matrixtheme.home.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.theme.matrixtheme.home.model.SpotProductHomeTheme1Model;

public class SpotProductHomeTheme1Controller extends SimiController {

	protected SimiDelegate mDelegate;

	@Override
	public void onStart() {
		// mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				// mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}
			}
		};

		mModel = new SpotProductHomeTheme1Model();
		if (DataLocal.isTablet) {
			mModel.addParam(Constants.PHONE_TYPE, "tablet");
		}
		mModel.setDelegate(delegate);
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

	public void setDelegate(SimiDelegate delegate) {
		this.mDelegate = delegate;
	}
}
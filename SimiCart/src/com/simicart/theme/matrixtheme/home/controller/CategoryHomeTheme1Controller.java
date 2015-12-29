package com.simicart.theme.matrixtheme.home.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.theme.matrixtheme.home.delegate.CategoryHomeTheme1Delegate;
import com.simicart.theme.matrixtheme.home.model.CategoryHomeTheme1Model;

public class CategoryHomeTheme1Controller extends SimiController {

	protected CategoryHomeTheme1Delegate mDelegate;

	@Override
	public void onStart() {
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}
			}
		};

		mModel = new CategoryHomeTheme1Model();
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

	public void setDelegate(CategoryHomeTheme1Delegate delegate) {
		this.mDelegate = delegate;
	}
}

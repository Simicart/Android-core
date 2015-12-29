package com.simicart.core.catalog.product.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.product.model.RelatedProductModel;

public class RelatedProductController extends SimiController {

	protected SimiDelegate mDelegate;
	protected String mID;

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public void setProductId(String id) {
		mID = id;
	}

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
		mModel = new RelatedProductModel();
		mModel.addParam("product_id", mID);
		mModel.addParam("limit", "15");
		mModel.addParam("width", "300");
		mModel.addParam("height", "300");
		mModel.setDelegate(delegate);
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

}

package com.simicart.core.home.controller;

import java.util.ArrayList;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.home.delegate.CategoryHomeDelegate;
import com.simicart.core.home.model.CategoryHomeModel;

public class CategoryHomeController extends SimiController {

	protected CategoryHomeDelegate mDelegate;
	protected ArrayList<Category> listCategory = new ArrayList<Category>();

	public CategoryHomeController() {
	}

	public void setDelegate(CategoryHomeDelegate delegate) {
		this.mDelegate = delegate;
	}

	public CategoryHomeDelegate getDelegate() {
		return mDelegate;
	}

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
		mModel = new CategoryHomeModel();
		mModel.setDelegate(delegate);
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

}

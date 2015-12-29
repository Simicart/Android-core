package com.simicart.core.home.controller;

import java.util.ArrayList;

import android.util.Log;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.home.delegate.ProductListDelegate;
import com.simicart.core.home.model.ProductListModel;

public class ProductListController extends SimiController {
	protected ProductListDelegate mDelegate;
	protected ArrayList<ProductList> mProductList;

	public ProductListController() {
		this.mProductList = new ArrayList<ProductList>();
	}

	public void setDelegate(ProductListDelegate delegate) {
		this.mDelegate = delegate;
	}

	@Override
	public void onStart() {
		Log.e("Time start ", "");
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					for (SimiEntity simiEntity : entity) {
						ProductList product = new ProductList();
						product.setJSONObject(simiEntity.getJSONObject());
						mProductList.add(product);
					}
					mDelegate.onUpdate(mProductList);
				}
			}
		};
		mModel = new ProductListModel();
		mModel.setDelegate(delegate);
		mModel.addParam("limit", "15");
		mModel.addParam("width", "200");
		mModel.addParam("height", "200");
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.onUpdate(mProductList);

	}
}

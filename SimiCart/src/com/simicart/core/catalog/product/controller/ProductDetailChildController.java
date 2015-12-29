package com.simicart.core.catalog.product.controller;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailChildDelegate;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.model.ProductModel;

public class ProductDetailChildController extends SimiController {

	protected ProductDetailChildDelegate mDelegate;
	protected ProductDetailAdapterDelegate mAdapterDelegate;
	protected ProductDetailParentController mController;
	private ProductDelegate productDelegate;

	public void setProductDelegate(ProductDelegate productDelegate) {
		this.productDelegate = productDelegate;
	}

	public void setParentController(ProductDetailParentController controller) {
		mController = controller;
	}

	public void setAdapterDelegate(ProductDetailAdapterDelegate delegate) {
		mAdapterDelegate = delegate;
	}

	protected String mID;

	public void setProductID(String id) {
		mID = id;
	}

	public void setDelegate(ProductDetailChildDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {
		requestData(mID);
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
		String current_id = mAdapterDelegate.getCurrentID();
		if (getProductFromCollection() != null) {
			String id = getProductFromCollection().getId();
			if (current_id.equals(id)) {
				mDelegate.updateIndicator();
			}
		}
	}

	protected void requestData(final String id) {
		mDelegate.showLoading();
		if (productDelegate != null) {
			productDelegate.getLayoutMore().setVisibility(View.GONE);
		}
		mModel = new ProductModel();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (productDelegate != null) {
					productDelegate.getLayoutMore().setVisibility(View.VISIBLE);
				}
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());

					if (null != mAdapterDelegate) {
						String current_id = mAdapterDelegate.getCurrentID();
						String id = getProductFromCollection().getId();
						if (current_id.equals(id)) {
							mController
									.onUpdateTopBottom((ProductModel) mModel);
							Log.e("ProductDetailChildController ",
									"requestData " + id);
							mDelegate.updateIndicator();
						}
					}

				}
			}
		};
		mModel.addParam("product_id", id);
		mModel.setDelegate(delegate);
		mModel.addParam("width", "600");
		mModel.addParam("height", "600");
		mModel.request();
	}

	public void onUpdateTopBottom() {
		if (null != mModel && null != mController) {
			mController.onUpdateTopBottom((ProductModel) mModel);
		}
	}

	protected Product getProductFromCollection() {
		Product product = null;
		ArrayList<SimiEntity> entity = mModel.getCollection().getCollection();
		if (null != entity && entity.size() > 0) {
			product = (Product) entity.get(0);
		}
		return product;
	}

}

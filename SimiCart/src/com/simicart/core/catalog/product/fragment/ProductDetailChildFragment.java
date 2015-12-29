package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.block.ProductDetailChildBlock;
import com.simicart.core.catalog.product.controller.ProductDetailChildController;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.config.Rconfig;

public class ProductDetailChildFragment extends SimiFragment {
	protected ProductDetailChildBlock mBlock;
	protected ProductDetailChildController mController;
	protected String mID;
	protected ProductDetailAdapterDelegate mAdapterDelegate;
	protected ProductDetailParentController mParentController;

	private ProductDelegate productDelegate;

	public void setProductDelegate(ProductDelegate productDelegate) {
		this.productDelegate = productDelegate;
	}

	// variable for tablet
	float mScale = 1.0f;
	boolean isBlured = false;

	public void setScalse(float scale) {
		mScale = scale;
	}

	public void setBlured(boolean blured) {
		isBlured = blured;
	}

	public static ProductDetailChildFragment newInstance() {
		ProductDetailChildFragment fragment = new ProductDetailChildFragment();
		return fragment;
	}

	public void setAdapterDelegate(ProductDetailAdapterDelegate delegate) {
		mAdapterDelegate = delegate;
	}

	public void setController(ProductDetailParentController controller) {
		mParentController = controller;
	}

	public void setProductID(String id) {
		mID = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_product_detail_child"),
				null, false);
		SimiManager.getIntance().setChildFragment(getChildFragmentManager());

		mBlock = new ProductDetailChildBlock(view, getActivity(),
				getChildFragmentManager());

		mBlock.initView();
		mBlock.setDelegate(mParentController);

		if (null == mController) {
			mController = new ProductDetailChildController();
			mController.setAdapterDelegate(mAdapterDelegate);
			mController.setProductID(mID);
			mController.setDelegate(mBlock);
			mController.setProductDelegate(productDelegate);
			mController.setParentController(mParentController);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		return view;
	}

	public void onUpdateTopBottom() {
		if (null != mController) {
			mController.onUpdateTopBottom();
		}
		if (null != mBlock) {
			mBlock.updateIndicator();
		}
	}

}

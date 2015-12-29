package com.simicart.core.catalog.product.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.fragment.ProductDetailImageFragment;

public class ProductDetailChildeAdapter extends FragmentPagerAdapter {
	protected String[] images = new String[] {};

	private int mCount;
	protected ProductDetailParentController mParentController;

	public void setDelegate(ProductDetailParentController delegate) {
		mParentController = delegate;
	}

	public ProductDetailChildeAdapter(FragmentManager fmChild, String[] Images) {
		super(fmChild);
		this.images = Images;
		this.mCount = images.length;
	}

	@Override
	public Fragment getItem(int position) {
		if (images.length > 0 && images[position] != null) {
			String url = images[position];
			ProductDetailImageFragment fragment = ProductDetailImageFragment.newInstance(url);
			fragment.setDelegate(mParentController);
			return fragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return mCount;
	}

}

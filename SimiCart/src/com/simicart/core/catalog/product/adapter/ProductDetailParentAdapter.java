package com.simicart.core.catalog.product.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.fragment.ProductDetailChildFragment;

public class ProductDetailParentAdapter extends FragmentPagerAdapter implements
		ProductDetailAdapterDelegate {
	protected ArrayList<String> mListID;
	protected String mCurrentID = "";
	protected ProductDetailParentController mController;

	public void setController(ProductDetailParentController controller) {
		mController = controller;
	}

	public void setListID(ArrayList<String> id) {
		mListID = id;
	}

	public ProductDetailParentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		String id = mListID.get(position);
		if (mCurrentID.equals("")) {
			mCurrentID = id;
		} else if (!mCurrentID.equals(id)) {
			mCurrentID = id;
			((ProductDetailChildFragment) object).onUpdateTopBottom();
		}
	}

	@Override
	public Fragment getItem(int position) {
		
		String id = mListID.get(position);
		ProductDetailChildFragment fragment = ProductDetailChildFragment
				.newInstance();
		fragment.setAdapterDelegate(this);
		fragment.setController(mController);
		fragment.setProductID(id);
		return fragment;
	}

	@Override
	public int getCount() {
		return mListID.size();
	}

	@Override
	public String getCurrentID() {
		return mCurrentID;
	}
}

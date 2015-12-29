package com.simicart.core.catalog.product.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.fragment.ProductDetailChildFragment;

public class ProductDetailParentAdapterTablet extends
		SmartFragmentStatePagerAdapter implements ProductDetailAdapterDelegate {

	private ProductDelegate productDelegate;
	
	public void setProductDelegate(ProductDelegate productDelegate) {
		this.productDelegate = productDelegate;
	}
	
	
	public ProductDetailParentAdapterTablet(FragmentManager fragmentManager) {
		super(fragmentManager);
	}
	

	protected ArrayList<String> mListID;
	protected String mCurrentID = "";
	protected ProductDetailParentController mController;

	public void setCurrentID(String id) {
		mCurrentID = id;
	}

	public void setController(ProductDetailParentController controller) {
		mController = controller;
	}

	public void setListID(ArrayList<String> id) {
		mListID = id;
	}

	@Override
	public float getPageWidth(int position) {
		return 0.8f;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
//		super.setPrimaryItem(container, position, object);
		String id = mListID.get(position);
		if (mCurrentID.equals("")) {
			mCurrentID = id;
		}

		else

		if (!mCurrentID.equals(id)) {

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
		fragment.setProductDelegate(productDelegate);
		fragment.setController(mController);
		fragment.setProductID(id);
		return fragment;
	}

	@Override
	public int getCount() {
		return mListID.size();
	}

	// ProductDetailAdapterDelegate
	@Override
	public String getCurrentID() {
		return mCurrentID;
	}

}

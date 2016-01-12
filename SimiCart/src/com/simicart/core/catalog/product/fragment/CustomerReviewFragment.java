package com.simicart.core.catalog.product.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.internal.ar;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.block.CustomerReviewBlock;
import com.simicart.core.catalog.product.controller.CustomerReviewController;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;

public class CustomerReviewFragment extends SimiFragment {

	protected ArrayList<Integer> mRatingStar;
	protected String mID;
	protected CustomerReviewBlock mBlock = null;
	protected CustomerReviewController mController = null;
	protected Product mProduct;

	public static CustomerReviewFragment newInstance(String id, Product mProduct, ArrayList<Integer> stars) {
		CustomerReviewFragment fragment = new CustomerReviewFragment();
		
		Bundle args = new Bundle();
		setData(Constants.KeyData.ID, id, Constants.KeyData.TYPE_STRING, args);
//		setData(Constants.KeyData.PRODUCT, mProduct, Constants.KeyData.TYPE_MODEL, args);
		args.putParcelable(Constants.KeyData.PRODUCT, mProduct);
		setData(Constants.KeyData.LIST_RATING_STAR, stars, Constants.KeyData.TYPE_LIST_INT, args);
	    fragment.setArguments(args);
		return fragment;
	}

//	public void setRatingStar(ArrayList<Integer> stars) {
//		mRatingStar = stars;
//	}
//
//	public void setProductID(String id) {
//		mID = id;
//	}
//	
//	public void setProduct(Product mProduct) {
//		this.mProduct = mProduct;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_information_customer_review_layout"),
				container, false);
		Context context = getActivity();
		
		//data
		if(getArguments() != null){
		mID = (String) getData(Constants.KeyData.ID, Constants.KeyData.TYPE_STRING, getArguments());
		mProduct = getArguments().getParcelable(Constants.KeyData.PRODUCT);
		mRatingStar = (ArrayList<Integer>) getData(Constants.KeyData.LIST_RATING_STAR, Constants.KeyData.TYPE_LIST_INT, getArguments());
		}
		
		mBlock = new CustomerReviewBlock(view, context);
		mBlock.setProduct(mProduct);
		
		// event
		CacheBlock cacheBlock = new CacheBlock();
		cacheBlock.setBlock(mBlock);
		EventBlock event = new EventBlock();
		event.dispatchEvent(
				"com.simicart.core.catalog.product.block.CustomerReviewBlock",
				view, context, cacheBlock);
		mBlock = (CustomerReviewBlock) cacheBlock.getBlock();
		
		mBlock.initView();
		if (mController == null) {
			mController = new CustomerReviewController();
			mController.setProductId(mID);
			mController.setDelegate(mBlock);
			mController.setRatingStar(mRatingStar);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setonScroll(mController.getScroller());

		return view;

	}

}

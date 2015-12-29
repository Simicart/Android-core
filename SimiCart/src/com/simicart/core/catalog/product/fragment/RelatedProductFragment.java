package com.simicart.core.catalog.product.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.block.RelatedProductBlock;
import com.simicart.core.catalog.product.controller.RelatedProductController;
import com.simicart.core.config.Rconfig;

public class RelatedProductFragment extends SimiFragment {
	protected String mID;
	protected RelatedProductBlock mBlock;
	protected RelatedProductController mController;
	
	public static RelatedProductFragment newInstance() {
		RelatedProductFragment fragment = new RelatedProductFragment();
		return fragment;
	}
	
	public void setID(String mID) {
		this.mID = mID;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_information_related_product_layout"), container,
				false);
		Context context = getActivity();
		mBlock = new RelatedProductBlock(view, context);
		mBlock.initView();
		if(mController == null){
			mController = new RelatedProductController();
			mController.setProductId(mID);
			mController.setDelegate(mBlock);
			mController.onStart();
		}else{
			mController.setProductId(mID);
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		return view;
	}
}

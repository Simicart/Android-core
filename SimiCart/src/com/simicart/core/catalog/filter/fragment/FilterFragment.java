package com.simicart.core.catalog.filter.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.filter.block.FilterBlock;
import com.simicart.core.catalog.filter.controller.FilterController;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.config.Rconfig;

public class FilterFragment extends SimiFragment {

	protected FilterController mController;
	protected FilterBlock mBlock;
	protected ArrayList<FilterEntity> mFilterEntity;
	protected FilterRequestDelegate mDelegate;
	protected ArrayList<FilterState> mStates;
	protected String tag_search ;
	private String catName = "";
	
	
	public void setCatName(String catName) {
		this.catName = catName;
	}

	public static FilterFragment newInstance() {
		FilterFragment fragment = new FilterFragment();
		return fragment;
	}
	
	public void setTag_search(String tag_search) {
		this.tag_search = tag_search;
	}

	public void setState(ArrayList<FilterState> states) {
		mStates = states;
	}

	public void setDelegate(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public void setFilterEntity(ArrayList<FilterEntity> filterEntity) {
		mFilterEntity = filterEntity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
					Rconfig.getInstance().layout("plugins_filter_layout"), null,
					false);
		
		Context context = getActivity();
		mBlock = new FilterBlock(view, context);
		mBlock.setName_category(catName);
		mBlock.initView();
		if (null != mDelegate) {
			mBlock.setDelegate(mDelegate);
		}
		if (null == mController) {
			mController = new FilterController();
			mController.setFilters(mFilterEntity);
			mController.setState(mStates);
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setonItemClickFilterList(mController.getItemClicker());
		return view;
	}

}

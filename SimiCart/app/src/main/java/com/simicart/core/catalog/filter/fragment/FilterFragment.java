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
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class FilterFragment extends SimiFragment {

	protected FilterController mController;
	protected FilterBlock mBlock;
	protected ArrayList<FilterEntity> mFilterEntity;
	protected FilterRequestDelegate mDelegate;
	protected ArrayList<FilterState> mStates;
	protected String tag_search;
	private String catName = "";

	public static FilterFragment newInstance(String tag, String name, ArrayList<FilterState> states, ArrayList<FilterEntity> filterEntity) {
		FilterFragment fragment = new FilterFragment();
		
		 Bundle bundle = new Bundle();
		 setData(Constants.KeyData.TAG, tag, Constants.KeyData.TYPE_STRING, bundle);
		 setData(Constants.KeyData.NAME, name, Constants.KeyData.TYPE_STRING, bundle);
		 bundle.putSerializable(Constants.KeyData.LIST_FILTER_STATE, states);
		 bundle.putSerializable(Constants.KeyData.LIST_FILTER_ENTITY, filterEntity);
		    fragment.setArguments(bundle);
		return fragment;
	}

	public void setDelegate(FilterRequestDelegate delegate) {
		if(delegate != null)
		mDelegate = delegate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_filter_layout"), null,
				false);

		Context context = getActivity();
		//data 
		if(getArguments() != null){
		tag_search = (String) getData(Constants.KeyData.TAG, Constants.KeyData.TYPE_STRING, getArguments());
		catName = (String) getData(Constants.KeyData.NAME, Constants.KeyData.TYPE_STRING, getArguments());
		mStates =  (ArrayList<FilterState>) getArguments().getSerializable(Constants.KeyData.LIST_FILTER_STATE);
		mFilterEntity =  (ArrayList<FilterEntity>) getArguments().getSerializable(Constants.KeyData.LIST_FILTER_ENTITY);
		}
		
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

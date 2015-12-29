package com.simicart.core.catalog.filter.controller;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.catalog.filter.delegate.FilterDelegate;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;

public class FilterController extends SimiController {

	protected OnItemClickListener mItemClicker;
	protected FilterDelegate mDelegate;
	protected ArrayList<FilterEntity> mFilters;
	protected ArrayList<FilterState> mStates;

	public void setState(ArrayList<FilterState> states) {
		mStates = states;
	}

	public void setFilters(ArrayList<FilterEntity> filters) {
		mFilters = filters;
	}

	public void setDelegate(FilterDelegate delegate) {
		mDelegate = delegate;
	}

	public OnItemClickListener getItemClicker() {
		return mItemClicker;
	}

	@Override
	public void onStart() {
		mItemClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onSelectClick(position);
			}
		};
		mDelegate.onShowListFilter(mFilters);
		mDelegate.onShowListSelectedFilter(mStates);
	}
	protected void onSelectClick(int position) {
		FilterEntity entity = mFilters.get(position);
		mDelegate.onShowDetailFilter(entity);
	}

	@Override
	public void onResume() {
		mDelegate.onShowListFilter(mFilters);
		mDelegate.onShowListSelectedFilter(mStates);
	}

}

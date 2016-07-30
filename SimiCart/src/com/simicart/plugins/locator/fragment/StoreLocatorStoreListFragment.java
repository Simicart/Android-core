package com.simicart.plugins.locator.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.block.StoreLocatorStoreListBlock;
import com.simicart.plugins.locator.controller.StoreLocatorStoreListController;
import com.simicart.plugins.locator.entity.SearchObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreLocatorStoreListFragment extends SimiFragment {
	
	protected StoreLocatorStoreListBlock mBlock;
	protected StoreLocatorStoreListController mController;
	protected SearchObject searchObject;
	
	public static StoreLocatorStoreListFragment newInstance() {
		return new StoreLocatorStoreListFragment();
	}
	
	public void setSearchObject(SearchObject search) {
		searchObject = search;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_store_list"), container, false);
		
		mBlock = new StoreLocatorStoreListBlock(rootView, getActivity());
		mBlock.initView();
		if(mController == null) {
			mController = new StoreLocatorStoreListController();
			mController.setDelegate(mBlock);
			mController.setSearchObject(searchObject);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.onListStoreScroll(mController.getOnListScroll());
		mBlock.onSearchClick(mController.getOnSearchClick());
		mBlock.onItemListStoreClick(mController.getOnListItemClick());
		
		return rootView;
	}

}

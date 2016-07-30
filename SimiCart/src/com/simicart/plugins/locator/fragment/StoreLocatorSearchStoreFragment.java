package com.simicart.plugins.locator.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.block.StoreLocatorSearchStoreBlock;
import com.simicart.plugins.locator.controller.StoreLocatorSearchStoreController;
import com.simicart.plugins.locator.entity.SearchObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreLocatorSearchStoreFragment extends SimiFragment {

	protected StoreLocatorSearchStoreBlock mBlock;
	protected StoreLocatorSearchStoreController mController;
	protected SearchObject search_object;

	public static StoreLocatorSearchStoreFragment newInstance(SearchObject searchObject) {
		StoreLocatorSearchStoreFragment fragment = new StoreLocatorSearchStoreFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KeyData.SEARCH_OBJECT, searchObject);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_search_store"), container,
				false);

		// data
		if (getArguments() != null) {
			search_object = (SearchObject) getArguments().getSerializable(Constants.KeyData.SEARCH_OBJECT);
		}

		mBlock = new StoreLocatorSearchStoreBlock(rootView, getActivity());
		mBlock.setSearchObject(search_object);
		mBlock.initView();
		if (mController == null) {
			mController = new StoreLocatorSearchStoreController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.onClearAllClick(mController.getOnClearSearchClick());
		mBlock.onSearchByTag(mController.getOnSearchBytag());
		mBlock.onSearchClick(mController.getOnSearchClick());

		return rootView;
	}

}

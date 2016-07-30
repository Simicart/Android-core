package com.simicart.plugins.locator.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.entity.SearchObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreLocatorMainPageTabletFragment extends SimiFragment {

	protected SearchObject searchObject;

	public static StoreLocatorMainPageTabletFragment newInstance() {
		return new StoreLocatorMainPageTabletFragment();
	}

	public void setSearchObject(SearchObject search) {
		searchObject = search;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_main"), container, false);

		StoreLocatorStoreListFragment storeListFragment = StoreLocatorStoreListFragment.newInstance();
		storeListFragment.setSearchObject(searchObject);
		SimiManager.getIntance().getManager().beginTransaction()
				.replace(Rconfig.getInstance().id("list_store_container"), storeListFragment).commit();
		SimiManager.getIntance().getManager().beginTransaction()
				.replace(Rconfig.getInstance().id("map_container"), StoreLocatorMapFragment.newInstance()).commit();

		return rootView;
	}

}

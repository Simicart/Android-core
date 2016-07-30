package com.simicart.plugins.locator.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.adapter.TabAdapter;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.style.SlidingTabLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreLocatorMainPageFragment extends SimiFragment {
	
	protected ViewPager mPager;
	protected SearchObject searchObject;
	
	public static StoreLocatorMainPageFragment newInstance() {
		return new StoreLocatorMainPageFragment();
	}
	
	public void setSearchObject(SearchObject search) {
		searchObject = search;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_mainpage"), container, false);
		
		mPager = (ViewPager) rootView.findViewById(Rconfig
				.getInstance().id("pager_store_locator"));
		setupViewPager();

		SlidingTabLayout title_tab = (SlidingTabLayout) rootView
				.findViewById(Rconfig.getInstance().id("tab_store_locator"));
		title_tab.setDistributeEvenly(true);
		title_tab.setBackgroundColor(Color.parseColor(Config.getInstance()
				.getSection_color()));
		title_tab.setViewPager(mPager);
		
		return rootView;
	}
	
	protected void setupViewPager() {
		TabAdapter adapter = new TabAdapter(getChildFragmentManager());
		StoreLocatorStoreListFragment storeListFragment = StoreLocatorStoreListFragment.newInstance();
		storeListFragment.setSearchObject(searchObject);
        adapter.addFragment(storeListFragment
        		, Config.getInstance().getText("Store List"));
        adapter.addFragment(StoreLocatorMapFragment.newInstance()
        		, Config.getInstance().getText("Map"));
        mPager.setAdapter(adapter);
	}

}

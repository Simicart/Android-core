package com.simicart.plugins.locator.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.block.StoreLocatorMapBlock;
import com.simicart.plugins.locator.common.StoreLocatorConfig;
import com.simicart.plugins.locator.controller.StoreLocatorMapController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StoreLocatorMapFragment extends SimiFragment {
	
	protected StoreLocatorMapBlock mBlock;
	protected StoreLocatorMapController mController;
	
	public static StoreLocatorMapFragment newInstance() {
		return new StoreLocatorMapFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_map"), container, false);
		
		mBlock = new StoreLocatorMapBlock(rootView, getActivity());
		mBlock.initView();
		if(mController == null) {
			mController = new StoreLocatorMapController();
			mController.setDelegate(mBlock);
			//mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			//mController.onResume();
		}
		StoreLocatorConfig.mapController = mController;
		
		return rootView;
	}

}

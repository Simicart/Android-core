package com.simicart.plugins.download.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.PagerSlidingTabStrip;
import com.simicart.plugins.download.adapter.TabDownloadAdapter;
import com.simicart.plugins.download.common.DownloadConstant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DownloadFragment extends SimiFragment {

	View v;
	public static DownloadFragment instance;
	ViewPager mPager;
	public TabDownloadAdapter adapter;
	boolean isUpdated = false;
	int currentTab = 0;
	protected BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			currentTab = mPager.getCurrentItem();
			mPager.setAdapter(adapter);
			mPager.setCurrentItem(currentTab);
		}
	};
	
	public static DownloadFragment newInstance() {
		if(instance == null)
			instance = new DownloadFragment();
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("DownloadFragment", "onCreateView");
		v = inflater.inflate(
				Rconfig.getInstance().layout("plugins_download_layout"),
				container, false);
		initView();
		return v;
	}

	public void initView() {
		adapter = new TabDownloadAdapter(getChildFragmentManager());
		mPager = (ViewPager) v.findViewById(Rconfig
				.getInstance().id("pager_downloaded"));
		mPager.setAdapter(adapter);

		PagerSlidingTabStrip title_tab = (PagerSlidingTabStrip) v
				.findViewById(Rconfig.getInstance().id("pager_title_strip_downloaded"));
		title_tab.setTextColor(Color.parseColor("#000000"));
		title_tab.setBackgroundColor(Color.parseColor("#eeeeee"));
		title_tab.setDividerColor(Color.parseColor("#eeeeee"));
		title_tab.setIndicatorColor(Color.parseColor("#c3c3c3"));
		title_tab.setIndicatorHeight(5);
		title_tab.setAllCaps(false);
		title_tab.setViewPager(mPager);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("DownloadFragment", "onResume");
		IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(DownloadConstant.DOWNLOAD_ACTION);
	    intentFilter.addAction(DownloadConstant.DOWNLOADING_ACTION);
	    getActivity().registerReceiver(receiver, intentFilter);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}
	
}

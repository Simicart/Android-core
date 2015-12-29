package com.simicart.plugins.download.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;
import com.simicart.plugins.download.fragment.DownloadProductFragment;
import com.simicart.plugins.download.fragment.DownloadProductFragmentTablet;
import com.simicart.plugins.download.fragment.DownloadedListProductFragment;
import com.simicart.plugins.download.fragment.DownloadedListProductFragmentTablet;
import com.simicart.plugins.download.fragment.DownloadingListProductFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabDownloadAdapter extends FragmentStatePagerAdapter {

	protected ArrayList<SimiFragment> mListFragment;
	protected ArrayList<String> mListTitle;
	protected Map<Integer, String> mFragmentTags;
	protected FragmentManager mFragmentManager;

	public TabDownloadAdapter(FragmentManager fm) {
		super(fm);
		mListFragment = new ArrayList<SimiFragment>();
		mListTitle = new ArrayList<String>();
		mFragmentTags = new HashMap<Integer, String>();
		mFragmentManager = fm;
		addFragment();
		addTitle();
		EventTabFragment();
	}

	@Override
	public Fragment getItem(int position) {
		return mListFragment.get(position);
	}

	@Override
	public int getCount() {
		return mListFragment.size();
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return mListFragment.indexOf(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mListTitle.get(position);
	}

	private void addFragment() {
		if (DataLocal.isTablet) {
			DownloadProductFragmentTablet fragment_download = DownloadProductFragmentTablet
					.newInstance();

			mListFragment.add(fragment_download);
			
			DownloadingListProductFragment fragment_downloading = DownloadingListProductFragment.newInstance();
			
			mListFragment.add(fragment_downloading);

			DownloadedListProductFragmentTablet fragment_list = DownloadedListProductFragmentTablet
					.newInstance();

			mListFragment.add(fragment_list);
		} else {
			DownloadProductFragment fragment_download = DownloadProductFragment
					.newInstance();

			mListFragment.add(fragment_download);
			
			DownloadingListProductFragment fragment_downloading = DownloadingListProductFragment.newInstance();
			
			mListFragment.add(fragment_downloading);

			DownloadedListProductFragment fragment_list = DownloadedListProductFragment
					.newInstance();

			mListFragment.add(fragment_list);
		}
	}

	private void addTitle() {
		mListTitle.add(Config.getInstance().getText(
				Config.getInstance().getText("Download Availability")));
		mListTitle.add(Config.getInstance().getText(
				Config.getInstance().getText("Downloading")));
		mListTitle.add(Config.getInstance().getText(
				Config.getInstance().getText("Downloaded")));
	}

	public void EventTabFragment() {
		EventBlock event = new EventBlock();
		CacheBlock cacheBlock = new CacheBlock();
		cacheBlock.setListFragment(mListFragment);
		cacheBlock.setListName(mListTitle);
		event.dispatchEvent(
				"com.simicart.plugins.download.adapter.TabDownloadAdapter",
				cacheBlock);
	}
}

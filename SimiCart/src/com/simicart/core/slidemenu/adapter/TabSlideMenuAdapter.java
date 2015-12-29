package com.simicart.core.slidemenu.adapter;

import java.util.ArrayList;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;

public class TabSlideMenuAdapter extends FragmentStatePagerAdapter {
	protected ArrayList<SimiFragment> mListFragment;
	protected ArrayList<String> mListTitle;

	public TabSlideMenuAdapter(FragmentManager fm,
			ArrayList<SimiFragment> simiFragments) {
		super(fm);
		mListFragment = simiFragments;
		mListTitle = new ArrayList<String>();
		addTitle();
	}

	@Override
	public SimiFragment getItem(int position) {
		return mListFragment.get(position);
	}

	@Override
	public int getCount() {
		return mListFragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mListTitle.get(position);
	}

	private void addTitle() {
		mListTitle.add(Config.getInstance().getText("Menu"));
		mListTitle.add(Config.getInstance().getText("Category"));
	}

	// @Override
	// public int getPageIconResId(int position) {
	// switch (position) {
	// case 0:
	// return R.drawable.ic_cart;
	// case 1:
	// return R.drawable.ic_extend;
	// default:
	// return 0;
	// }
	// }

}

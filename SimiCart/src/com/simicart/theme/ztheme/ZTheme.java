package com.simicart.theme.ztheme;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.theme.ztheme.home.fragment.HomeZThemeFragment;
import com.simicart.theme.ztheme.home.fragment.HomeZThemeFragmentTablet;

public class ZTheme {
	CacheFragment mCacheFragment;
	public ZTheme(String method, CacheFragment caheFragment) {
		this.mCacheFragment = caheFragment;
		if(method.equals("changeHome")){
			SimiFragment fragment;
			if (DataLocal.isTablet) {
				fragment = HomeZThemeFragmentTablet.newInstance();
			} else {
				fragment = HomeZThemeFragment.newInstance();
			}
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

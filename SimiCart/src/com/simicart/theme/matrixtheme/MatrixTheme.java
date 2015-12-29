package com.simicart.theme.matrixtheme;

import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.theme.matrixtheme.home.fragment.HomeTheme1Fragment;

public class MatrixTheme {
	CacheFragment mCacheFragment;

	public MatrixTheme(String method, CacheFragment caheFragment) {
		this.mCacheFragment = caheFragment;
		if (method.equals("changeHome")) {
			HomeTheme1Fragment fragment = HomeTheme1Fragment.newInstance();
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

package com.simicart.core.event.fragment;

import com.simicart.core.base.fragment.SimiFragment;

public class CacheFragment {
	private SimiFragment fragment;

	private static CacheFragment instance;

	public static CacheFragment getInstance() {
		if (null == instance) {
			instance = new CacheFragment();
		}
		return instance;
	}

	public void setFragment(SimiFragment fragment) {
		this.fragment = fragment;
	}

	public SimiFragment getFragment() {
		return this.fragment;
	}
}

package com.simicart.plugins.europackage;

import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.event.fragment.CacheFragment;

public class EuroPackageClass {
	CacheFragment mCacheFragment;
	public EuroPackageClass(String method, CacheFragment mCacheFragment) {
		this.mCacheFragment = mCacheFragment;
		if (method.equals("revieworder_europackage")) {
			ReviewOrderEuroPackageFragment fragment = new ReviewOrderEuroPackageFragment();
			ReviewOrderFragment reviewOrderFragment = (ReviewOrderFragment)mCacheFragment.getFragment();
					
			fragment.setShippingAddress(reviewOrderFragment.getShippingAddress());
			fragment.setBilingAddress(reviewOrderFragment.getBillingAddress());
			this.mCacheFragment.setFragment(fragment);
		}
	}
	
}

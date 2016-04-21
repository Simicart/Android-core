package com.simicart.plugins.locationpickup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.plugins.locationpickup.fragment.LocationPickupEditFragment;
import com.simicart.plugins.locationpickup.fragment.LocationPickupFragment;

public class LocationPickup {

	protected CacheFragment mCacheFragment;

	public LocationPickup(String method, CacheFragment caheFragment) {
		this.mCacheFragment = caheFragment;
		SimiFragment fragment;
		if (method.equals("addCreateView")) {
			fragment = LocationPickupFragment.newInstance();
			NewAddressBookFragment fragmentCache = (NewAddressBookFragment) caheFragment
					.getFragment();
			fragment.setArguments(fragmentCache.getArguments());

			this.mCacheFragment.setFragment(fragment);
		}
		if (method.equals("addEditView")) {
			fragment = LocationPickupEditFragment.newInstance();
			AddressBookDetailFragment fragmentCache = (AddressBookDetailFragment) caheFragment
					.getFragment();
			fragment.setArguments(fragmentCache.getArguments());
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

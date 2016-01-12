package com.simicart.plugins.locationpickup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.customer.entity.MyAddress;
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
			int afterControl = ((NewAddressBookFragment) caheFragment
					.getFragment()).getAfterControl();
			fragment = LocationPickupFragment.newInstance(afterControl, 0 , null, null);
			
//			((LocationPickupFragment) fragment).setAfterControler(afterControl);

//			fragment = new TestFragment();

			this.mCacheFragment.setFragment(fragment);
		}
		if (method.equals("addEditView")) {
			MyAddress myaddress = ((AddressBookDetailFragment) caheFragment
					.getFragment()).getAddressbook();
			fragment = LocationPickupEditFragment.newInstance(myaddress);
//			MyAddress myaddress = ((AddressBookDetailFragment) caheFragment
//					.getFragment()).getAddressbook();
//			((LocationPickupEditFragment) fragment).setAddressbook(myaddress);
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

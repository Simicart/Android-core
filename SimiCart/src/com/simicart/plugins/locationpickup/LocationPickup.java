package com.simicart.plugins.locationpickup;

import android.util.Log;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.controller.PopupCheckoutController;
import com.simicart.core.customer.controller.AddressBookController;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.plugins.locationpickup.fragment.LocationPickupEditFragment;
import com.simicart.plugins.locationpickup.fragment.LocationPickupFragment;

public class LocationPickup {
	
	protected CacheFragment mCacheFragment;

	public LocationPickup(String method, CacheFragment caheFragment) {
		this.mCacheFragment = caheFragment;
		SimiFragment fragment;
		Log.d("quang1", "==LocationPickup==");
		if (method.equals("addCreateView")) {
			int afterControl = ((NewAddressBookFragment) mCacheFragment
					.getFragment()).getAfterControl();
//			Log.d("quang1", "==afterControl==");
			fragment = LocationPickupFragment.newInstance();
			fragment.setArguments(PopupCheckoutController.bundle);
//			((LocationPickupFragment) fragment).setAfterControler(afterControl);
//			fragment = new TestFragment();

			this.mCacheFragment.setFragment(fragment);
		}
		if (method.equals("addEditView")) {
			Log.d("quangdd", "==LocationPickup==addressbook==1");
//			MyAddress myaddress = ((AddressBookDetailFragment) mCacheFragment
//					.getFragment()).getAddressbook();
			fragment = LocationPickupEditFragment.newInstance();
			fragment.setArguments(AddressBookController.bundleAddress);
//			MyAddress myaddress = ((AddressBookDetailFragment) caheFragment
//					.getFragment()).getAddressbook();
//			((LocationPickupEditFragment) fragment).setAddressbook(myaddress);
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

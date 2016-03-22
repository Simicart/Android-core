package com.simicart.plugins.locationpickup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.controller.AddressBookCheckoutController;
import com.simicart.core.checkout.controller.PopupCheckoutController;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.controller.AddressBookController;
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
//			int afterControl = ((NewAddressBookFragment) mCacheFragment
//					.getFragment()).getAfterControl();
			fragment = LocationPickupFragment.newInstance();
			if(Constants.getBundle ==1){
				fragment.setArguments(AddressBookCheckoutController.bundle);
			}else if(Constants.getBundle ==2){
				fragment.setArguments(PopupCheckoutController.bundle);
			}else {
				fragment.setArguments(AddressBookController.bundleAfter);
			}
			
//			((LocationPickupFragment) fragment).setAfterControler(afterControl);
//			fragment = new TestFragment();

			this.mCacheFragment.setFragment(fragment);
		}
		if (method.equals("addEditView")) {
			fragment = LocationPickupEditFragment.newInstance();
			fragment.setArguments(AddressBookController.bundleAddress);
//			MyAddress myaddress = ((AddressBookDetailFragment) caheFragment
//					.getFragment()).getAddressbook();
//			((LocationPickupEditFragment) fragment).setAddressbook(myaddress);
			this.mCacheFragment.setFragment(fragment);
		}
	}
}

package com.simicart.plugins.locator.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.locator.entity.StoreObject;

public interface StoreLocatorMapDelegate extends SimiDelegate {
	
	public void addMarkerToMap(ArrayList<StoreObject> listStores);
	
}

package com.simicart.plugins.locator.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.locator.entity.StoreObject;

import android.location.Location;

public interface StoreLocatorStoreListDelegate extends SimiDelegate {

	public void visibleSearchLayout(boolean visible);
	
	public void showLoadMore(boolean isLoading);
	
	public ArrayList<StoreObject> getListStore();
	
	public Location getCurrentLocation();
	
}

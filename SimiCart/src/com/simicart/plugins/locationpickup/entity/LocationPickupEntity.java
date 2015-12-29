package com.simicart.plugins.locationpickup.entity;

import com.simicart.core.customer.entity.MyAddress;

public class LocationPickupEntity extends MyAddress{
	private String mLatLng;

	public String getLatLng() {
		if(mLatLng == null){
			mLatLng = getData("latlng");
		}
		return mLatLng;
	}
	
	public void setLatLng(String mLatLng) {
		this.mLatLng = mLatLng;
	}
}

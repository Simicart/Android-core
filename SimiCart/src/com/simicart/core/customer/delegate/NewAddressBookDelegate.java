package com.simicart.core.customer.delegate;

import android.widget.Spinner;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.ProfileEntity;

public interface NewAddressBookDelegate extends SimiDelegate {
	public MyAddress getNewAddressBook();
	
	public ProfileEntity getProfileEntity();

	public void updateCountry(String country);

	public void updateState(String state);
	
	public void createView(int controll);
	public Spinner getGender();

}

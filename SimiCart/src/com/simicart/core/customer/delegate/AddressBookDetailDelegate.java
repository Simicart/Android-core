package com.simicart.core.customer.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;

public interface AddressBookDetailDelegate extends SimiDelegate {
	public MyAddress getAddressBookDetail();

	public void setCountry(int type, String mCountry,
			ArrayList<CountryAllowed> listCountry);

	public void setListCountry(ArrayList<CountryAllowed> listCountry);

}

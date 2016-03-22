package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.ShippingMethod;

public interface ShippingMethodDelegate extends SimiDelegate{

	public void updateListShippingMethod(ArrayList<ShippingMethod> listShipping);
	
}

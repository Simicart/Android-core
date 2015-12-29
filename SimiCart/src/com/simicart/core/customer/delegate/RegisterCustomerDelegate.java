package com.simicart.core.customer.delegate;

import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.customer.entity.RegisterCustomer;

public interface RegisterCustomerDelegate extends SimiDelegate{
	public RegisterCustomer getRegisterCustomer();
	public RelativeLayout getRelativeImage();
	public Spinner getSpinnerSex();
}

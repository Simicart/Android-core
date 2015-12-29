package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;

public interface ShippingDelegate extends SimiDelegate{
	void setShippingMethods(ArrayList<ShippingMethod> shippingMethods);
	public void goneView();
	public void setPaymentMethod(ArrayList<PaymentMethod> paymentMethods);
}

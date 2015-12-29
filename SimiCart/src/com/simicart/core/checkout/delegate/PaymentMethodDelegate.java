package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;

public interface PaymentMethodDelegate extends SimiDelegate {

	void setPaymentMethods(ArrayList<PaymentMethod> paymentMethods);
	public void setListShippingMethod (ArrayList<ShippingMethod> list);
	public void goneView();

}

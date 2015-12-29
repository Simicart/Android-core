package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;

public interface SelectedShippingMethodDelegate {

	public void updateTotalPrice(TotalPrice totalPrice);
	public void updatePaymentMethod(ArrayList<PaymentMethod> paymentMethod);
	public void updateShippingMethod(ShippingMethod shippingMethod);
}

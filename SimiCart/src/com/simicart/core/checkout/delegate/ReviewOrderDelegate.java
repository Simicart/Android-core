package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import android.widget.LinearLayout;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.Condition;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.customer.entity.MyAddress;

public interface ReviewOrderDelegate extends SimiDelegate {

	public void onUpdateAddress(MyAddress address);

	public void setShipingMethods(ArrayList<ShippingMethod> shippingMethods);

	public void setConditions(ArrayList<Condition> conditions);

	public void setTotalPrice(TotalPrice totalPrice);

	public void setShipingAddress(MyAddress mShippingAddress);

	public void setBillingAddress(MyAddress mBillingAddress);
	
	public void goneView();
	
	public void setInitViewShippingMethod(String shippingName);
	
	public void setInitViewPaymentMethod(String paymentName);
	
	public void scrollDown();
	public void scrollCenter();
	public LinearLayout getLayoutShipping();
	public LinearLayout getLayoutPayment();
}

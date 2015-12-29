package com.simicart.core.checkout.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.TotalPrice;

public interface CartDelegate extends SimiDelegate {

	public void onUpdateTotalPrice(TotalPrice totalPrice);

	public void showPopupCheckout();

	public void dismissPopupCheckout();

	public void setMessage(String message);

	public void visibleAllView();

	void setCheckoutWebView(String url);
}

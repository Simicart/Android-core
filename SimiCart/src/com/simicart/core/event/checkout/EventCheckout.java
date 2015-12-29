package com.simicart.core.event.checkout;

import com.simicart.core.event.base.SimiEvent;

public class EventCheckout extends SimiEvent {

	public void dispatchEvent(String eventName, CheckoutData checkoutData) {
		Object[] objects = new Object[1];
		objects[0] = checkoutData;
		super.disPatchEvent(eventName, objects, true);
	}
}

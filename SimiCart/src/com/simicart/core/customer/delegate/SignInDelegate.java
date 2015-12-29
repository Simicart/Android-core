package com.simicart.core.customer.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.material.ButtonRectangle;

public interface SignInDelegate extends SimiDelegate {

	public String getEmail();

	public String getPassword();

	public ButtonRectangle getSignIn();

	public void showNotify(String message);
}

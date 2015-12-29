package com.simicart.core.customer.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

public interface ForgotPasswordDelegate extends SimiDelegate {

	public String getEmail();

	public void showNotify(String message);
}

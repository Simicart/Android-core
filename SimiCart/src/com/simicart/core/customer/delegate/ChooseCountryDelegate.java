package com.simicart.core.customer.delegate;

public interface ChooseCountryDelegate {
	public void chooseCountry(int type, String country);

	public void setCurrentCountry(String country);

	public void setCurrentState(String state);
}

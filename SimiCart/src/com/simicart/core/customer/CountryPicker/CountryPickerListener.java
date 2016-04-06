package com.simicart.core.customer.CountryPicker;

/**
 * Inform the client which country has been selected
 *
 */
public interface CountryPickerListener {
	public void onSelectCountry(String name, String code);
}

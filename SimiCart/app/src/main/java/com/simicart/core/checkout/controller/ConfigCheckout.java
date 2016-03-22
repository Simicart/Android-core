package com.simicart.core.checkout.controller;

import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.TotalPrice;

public class ConfigCheckout {

	public static int TARGET_REVIEWORDER = 6789;
	public static int TARGET_LISTPRODUCT = 12345;
	public static boolean checkShippingMethod;
	public static boolean checkPaymentMethod;
	public static boolean checkCondition;
	// page address book

	public boolean statusAddressBook;
	public boolean addressBookFirstRequest = true;
	public SimiCollection collectionAddressBook;

	private static ConfigCheckout instance;

	public static ConfigCheckout getInstance() {
		if (instance == null) {
			instance = new ConfigCheckout();
		}
		return instance;
	}

	public void setStatusAddressBook(boolean status) {
		this.statusAddressBook = status;
	}

	public boolean getStatusAddressBook() {
		return this.statusAddressBook;
	}

	public void setCollectionAddressBook(SimiCollection collectionAddressBook) {
		this.collectionAddressBook = collectionAddressBook;
	}

	public SimiCollection getCollectionAddressBook() {
		return collectionAddressBook;
	}

	public void setAddressBookFirstRequest(boolean addressBookFirstRequest) {
		this.addressBookFirstRequest = addressBookFirstRequest;
	}

	public boolean getAddressBookFirstRequest() {
		return this.addressBookFirstRequest;
	}
	// end

}

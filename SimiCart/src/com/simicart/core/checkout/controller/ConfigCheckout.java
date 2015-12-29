package com.simicart.core.checkout.controller;

import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.TotalPrice;

public class ConfigCheckout {

	public static int TARGET_REVIEWORDER = 6789;
	public static int TARGET_LISTPRODUCT = 12345;
	public static boolean checkShippingMethod;
	public static boolean checkPaymentMethod;
	public static boolean checkCondition;
	// page cart
	public SimiCollection collectionCart;
	public boolean cartFirstRequest = true;
	public boolean statusCart;
	public TotalPrice totalPriceCart;
	public String messageCart;
	public int mQty;
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

	public void setCollectionCart(SimiCollection collectionCart) {
		this.collectionCart = collectionCart;
	}

	public SimiCollection getCollectionCart() {
		return collectionCart;
	}

	public void setCheckStatusCart(boolean status) {
		this.statusCart = status;
	}

	public boolean getStatusCart() {
		return this.statusCart;
	}

	public void setTotalPriceCart(TotalPrice totalPriceCart) {
		this.totalPriceCart = totalPriceCart;
	}

	public TotalPrice getTotalPriceCart() {
		return totalPriceCart;
	}

	public void setMessageCart(String messageCart) {
		this.messageCart = messageCart;
	}

	public String getMessageCart() {
		return messageCart;
	}

	public void setmQty(String qty) {

		int iQty = 0;

		try {
			iQty = Integer.parseInt(qty);
		} catch (Exception e) {
		}

		this.mQty = iQty;
	}

	public int getmQty() {
		return mQty;
	}

	public void setCartFirstRequest(boolean firstRequest) {
		this.cartFirstRequest = firstRequest;
	}

	public boolean getCartFirstRequest() {
		return cartFirstRequest;
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

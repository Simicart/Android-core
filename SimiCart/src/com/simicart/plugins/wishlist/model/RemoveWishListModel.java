package com.simicart.plugins.wishlist.model;


public class RemoveWishListModel extends MyWishListModel {

	@Override
	protected void setUrlAction() {
		url_action = "appwishlist/api/remove_product_from_wishlist";
	}
}

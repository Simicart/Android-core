package com.simicart.plugins.wishlist.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.catalog.product.entity.Product;
import com.simicart.plugins.wishlist.common.WishListConstants;

public class ProductWishList {
	Product product;
	String wishlist_item_id;

	public ProductWishList(Product product) {
		setProduct(product);
		if (product.getJSONObject().has(WishListConstants.WISHLIST_ITEM_ID)) {
			setWishlist_item_id(product
					.getData(WishListConstants.WISHLIST_ITEM_ID));
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getWishlist_item_id() {
		return wishlist_item_id;
	}

	public void setWishlist_item_id(String wishlist_item_id) {
		this.wishlist_item_id = wishlist_item_id;
		JSONObject json = product.getJSONObject();
		try {
			json.put(WishListConstants.WISHLIST_ITEM_ID, wishlist_item_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.simicart.plugins.wishlist.common;

import com.simicart.core.common.Utils;
import com.simicart.plugins.wishlist.WishList;

public class WishListManager {

	protected static WishListManager instance;
	protected WishList mWishList;

	public void setWishList(WishList wishList) {
		mWishList = wishList;
	}

	private WishListManager() {

	}

	public static WishListManager getInstance() {
		if (null == instance) {
			instance = new WishListManager();
		}
		return instance;
	}

	public void updateQtyWishList(String qty) {
		if (Utils.validateString(qty)) {
			try {
				int i_qty = Integer.valueOf(qty);
				if (null != mWishList) {
					mWishList.updateWishListQty(i_qty);
				}
			} catch (Exception e) {
				mWishList.updateWishListQty(0);
			}
		}
		else
		{
			mWishList.updateWishListQty(0);
		}
	}

}

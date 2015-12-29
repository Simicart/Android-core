package com.simicart.plugins.wishlist.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;

public interface MyWishListDelegate extends SimiDelegate {

	void setWishlist_qty(int wishlist_qty);
	public void updateData(ArrayList<ItemWishList> items);
	public void initView();
	public void showDetail(String id);
	public void requestShowNext();
	public boolean isShown();
}

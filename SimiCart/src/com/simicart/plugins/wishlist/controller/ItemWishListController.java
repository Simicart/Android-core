package com.simicart.plugins.wishlist.controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.DataLocal;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.delegate.RefreshWishlistDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;
import com.simicart.plugins.wishlist.model.AddToCartFromWishListModel;
import com.simicart.plugins.wishlist.model.RemoveWishListModel;

public class ItemWishListController {

	protected Context mContext;
	protected MyWishListDelegate mDelegate;
	protected SimiModel mModel;
	protected RefreshWishlistDelegate refreshWishlistDelegate;
	
	
	public void setRefreshWishlistDelegate(
			RefreshWishlistDelegate refreshWishlistDelegate) {
		this.refreshWishlistDelegate = refreshWishlistDelegate;
	}

	public ItemWishListController(MyWishListDelegate delegate, Context context) {
		mContext = context;
		mDelegate = delegate;
	}

	public void controllerRemoveItem(String deletedID) {
		mDelegate.showDialogLoading();
		mModel = new RemoveWishListModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
					if (null != entity && entity.size() > 0) {

						for (SimiEntity simiEntity : entity) {
							ItemWishList itemWishList = (ItemWishList) simiEntity;
							items.add(itemWishList);
						}

					}
					mDelegate.updateData(items);
					mDelegate.setWishlist_qty(((RemoveWishListModel) mModel)
							.getWishlist_qty());
					SimiManager.getIntance().showToast(
							"It's removed from Wishlist");
				} else {
					String mes = "Error! Please try again.";
					if (message != null) {
						mes = message;
					}
					SimiManager.getIntance().showToast(mes);
				}
			}
		});

		mModel.addParam(WishListConstants.WISHLIST_ITEM_ID, "" + deletedID);
		mModel.request();
	}

	public void controllerRemoveAndShowNext(String deletedID, String nextID) {
		controllerRemoveItem(deletedID);
		if (null != nextID) {
			mDelegate.showDetail(nextID);
		}
	}

	public void controllerAddToCart(String wishlist_item_id, final String product_id,final int position) {
		mDelegate.showDialogLoading();
		mModel = new AddToCartFromWishListModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					refreshWishlistDelegate.refreshWishlist(position);
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
					if (null != entity && entity.size() > 0) {

						for (SimiEntity simiEntity : entity) {
							ItemWishList itemWishList = (ItemWishList) simiEntity;
							items.add(itemWishList);
						}
					}
					int cart_qty = ((AddToCartFromWishListModel) mModel)
							.getCartQty();
					
					
					
					SimiManager.getIntance().onUpdateCartQty(
							String.valueOf(cart_qty));
					ConfigCheckout.getInstance().setmQty(String.valueOf(cart_qty));
					ConfigCheckout.getInstance().setCheckStatusCart(true);
					mDelegate.updateData(items);
					mDelegate
							.setWishlist_qty(((AddToCartFromWishListModel) mModel)
									.getWishlist_qty());
					SimiManager.getIntance().showToast(
							"Added to Cart and Removed from Wishlist");

				} else {
					if(DataLocal.isTablet){
						mDelegate.showDetail(product_id);
					}
					String mes = "Error! Please try again.";
					if (message != null) {
						mes = message;
					}
					SimiManager.getIntance().showToast(mes);
				}
			}
		});

		mModel.addParam(WishListConstants.WISHLIST_ITEM_ID, ""
				+ wishlist_item_id);
		mModel.request();
	}

	public void controllerAddAndShowNext(String addID, String showID, String product_id) {
		controllerAddToCart(addID, product_id,0);
		if (null != showID) {
			mDelegate.showDetail(showID);
		}
	}

	public void controllerShare(String sharing_mes) {
		if (sharing_mes != null && !sharing_mes.equals("")) {
			Intent intent2 = new Intent();
			intent2.setAction(Intent.ACTION_SEND);
			intent2.setType("text/plain");
			intent2.putExtra(Intent.EXTRA_TEXT, sharing_mes);
			MainActivity.context.startActivity(Intent.createChooser(intent2,
					"Share via"));
		}
	}

}

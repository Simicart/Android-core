package com.simicart.plugins.wishlist.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.catalog.product.controller.ProductController;
import com.simicart.core.catalog.product.model.ProductModel;
import com.simicart.core.config.Config;

@SuppressLint("ClickableViewAccessibility")
public class ProductWishListController extends ProductController {
	@Override
	public void onStart() {
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
					onUpdatePriceView();
					onUpdateOptionView();
				}
			}
		};
		mModel = new ProductModel();
		mModel.addParam("product_id", mID);
		mModel.addParam("width", "500");
		mModel.addParam("height", "500");
		mModel.setDelegate(delegate);
		mModel.request();

		mListenerAddToCart = new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {

					if (getProductFromCollection().getStock()) {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Color.GRAY);
						gdDefault.setCornerRadius(15);
						v.setBackgroundDrawable(gdDefault);
					}
					break;
				}
				case MotionEvent.ACTION_UP: {
					addtoCart();
				}
				case MotionEvent.ACTION_CANCEL: {

					if (getProductFromCollection().getStock()) {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Config.getInstance().getColorMain());
						gdDefault.setCornerRadius(15);
						v.setBackgroundDrawable(gdDefault);

					}
					break;
				}
				default:
					break;
				}
				return true;
			}
		};

		mListenerInfor = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		};

		mClickerRating = new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		};

		mClickerImage = new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		};
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
		onUpdatePriceView();
		onUpdateOptionView();

	}
}

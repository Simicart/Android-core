package com.simicart.plugins.wishlist.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;

public class ButtonAddWishList {

	FloatingActionButton imageAddWishList;
	boolean isEnable;
	Drawable icon;
	Drawable icon2;

	public FloatingActionButton getImageAddWishList() {
		return imageAddWishList;
	}

	public void setImageAddWishList(FloatingActionButton imageAddWishList) {
		this.imageAddWishList = imageAddWishList;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		if (isEnable) {
			getImageAddWishList().setIconDrawable(icon2);
		} else {
			getImageAddWishList().setIconDrawable(icon);
		}
		this.isEnable = isEnable;
	}
	
	public ButtonAddWishList(Context context) {
		icon = SimiManager
				.getIntance()
				.getCurrentContext()
				.getResources()
				.getDrawable(
						Rconfig.getInstance().drawable("plugins_wishlist_iconadd1"));

		icon2 = SimiManager
				.getIntance()
				.getCurrentContext()
				.getResources()
				.getDrawable(
						Rconfig.getInstance().drawable("plugins_wishlist_iconadd2"));

		imageAddWishList = new FloatingActionButton(context);
		imageAddWishList.setColorNormal(Color.parseColor("#FFFFFF"));
		imageAddWishList.setColorPressed(Color.parseColor("#f4f4f4"));
		imageAddWishList.setIconDrawable(icon);
		imageAddWishList.setColorFilter(Color.YELLOW);
	}

}

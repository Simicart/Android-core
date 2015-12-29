package com.simicart.plugins.locator;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.plugins.locator.entity.StoreObject;

@SuppressLint("NewApi")
public class DataLocator {
	public static String convertAddress(StoreObject object) {
		String address = object.getAddress() + ", " + object.getCity();
		if (!object.getState().equals("null") && !object.getState().equals("")) {
			address += ", " + object.getState();
		}
		if (!object.getZipcode().equals("null")
				&& !object.getZipcode().equals("")) {
			address += ", " + object.getZipcode();
		}
		address += ", " + object.getCountryName();
		return address;
	}

	public static void setColorDisable(ImageView image, int resource) {

		Drawable myIcon = SimiManager.getIntance().getCurrentContext()
				.getResources().getDrawable(resource);
		myIcon.setColorFilter(Color.parseColor("#BCBCBC"),
				PorterDuff.Mode.SRC_ATOP);
		image.setImageDrawable(myIcon);

	}

	public static void setColorEnable(TextView text, View view,
			ImageView image, int resource, String color) {
		int strokeWidth = 1;
		int strokeColor = Color.parseColor(color);
		GradientDrawable gd = new GradientDrawable();
		// gd.setColor(fillColor);
		gd.setStroke(strokeWidth, strokeColor);
		gd.setAlpha(60);
		view.setBackground(gd);
		text.setTextColor(Color.parseColor("#000000"));

		Drawable myIcon = SimiManager.getIntance().getCurrentContext()
				.getResources().getDrawable(resource);
		// myIcon.setColorFilter(Color.parseColor(color),
		// PorterDuff.Mode.SRC_ATOP);
		image.setImageDrawable(myIcon);
	}
}

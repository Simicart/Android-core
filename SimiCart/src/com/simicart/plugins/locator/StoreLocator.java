package com.simicart.plugins.locator;

import java.util.ArrayList;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.locator.fragment.StoreLocatorFragment;

public class StoreLocator {
	SlideMenuData mSlideMenuData;
	ArrayList<ItemNavigation> mItems;

	public StoreLocator(String methodName, SlideMenuData slideMenuData) {
		Log.e("StoreLocator", "MEHTOD: " + methodName);
		this.mSlideMenuData = slideMenuData;
		if (methodName.equals("addItem")) {
			mItems = mSlideMenuData.getItemNavigations();
			ItemNavigation mItemNavigation = new ItemNavigation();
			mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
			Drawable icon = MainActivity.context.getResources().getDrawable(
					Rconfig.getInstance().drawable("plugins_locator"));
			icon.setColorFilter(Config.getInstance().getColorMenu(),
					PorterDuff.Mode.SRC_ATOP);
			mItemNavigation.setName("Store Locator");
			mItemNavigation.setIcon(icon);
			mItems.add(mItemNavigation);

			StoreLocatorFragment fragment = new StoreLocatorFragment();
			mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
					fragment.getClass().getName());
		}
	}
}

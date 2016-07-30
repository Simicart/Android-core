package com.simicart.plugins.locator;

import java.util.ArrayList;

import com.simicart.MainActivity;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

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

			if(DataLocal.isTablet) {
				StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
				mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
						fragment.getClass().getName());
			} else {
				StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
				mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
						fragment.getClass().getName());
			}
		}
	}
}

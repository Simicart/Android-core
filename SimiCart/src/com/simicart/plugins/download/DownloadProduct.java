package com.simicart.plugins.download;

import java.util.ArrayList;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.download.fragment.DownloadFragment;

public class DownloadProduct {
	protected SlideMenuData mSlideMenuData;
	protected ArrayList<ItemNavigation> mItems;
	public static final String MY_DOWNLOAD_LABLE = "Manage Downloads";

	public DownloadProduct(String methodName, SlideMenuData slideMenuData) {
		this.mSlideMenuData = slideMenuData;
		if (methodName.equals("addItem")) {
			Log.e("Download product", "addItem");
			mItems = mSlideMenuData.getItemNavigations();
			ItemNavigation mItemNavigation = new ItemNavigation();
			mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
			Drawable icon = SimiManager
					.getIntance()
					.getCurrentContext()
					.getResources()
					.getDrawable(
							Rconfig.getInstance().drawable("plugin_downloadable_ic_down"));
			icon.setColorFilter(Config.getInstance().getColorMenu(),
					PorterDuff.Mode.SRC_ATOP);
			mItemNavigation.setName(Config.getInstance().getText(MY_DOWNLOAD_LABLE));
			mItemNavigation.setIcon(icon);
			mItems.add(mItemNavigation);

			Fragment fragment = null;
			if(DataLocal.isTablet) {
				fragment = DownloadFragment.newInstance();
			} else {
				fragment = DownloadFragment.newInstance();
			}
			mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
					fragment.getClass().getName());
		}
		
		if (methodName.equals("removeItem")) {
			Log.e("Download product", "removeItem");
			mItems = mSlideMenuData.getItemNavigations();
			for (ItemNavigation mItemNavigation : mItems) {
				if (mItemNavigation.getName().equals(MY_DOWNLOAD_LABLE)) {
					mItems.remove(mItemNavigation);
					mSlideMenuData.getPluginFragment().remove(
							mItemNavigation.getName());
				}
			}
		}
	}
}

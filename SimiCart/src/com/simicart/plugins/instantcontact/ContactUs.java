package com.simicart.plugins.instantcontact;

import java.util.ArrayList;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.simicart.MainActivity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.plugins.instantcontact.fragment.ContactUsFragment;

public class ContactUs {

	protected SlideMenuData mSlideMenuData;
	protected ArrayList<ItemNavigation> mItems;

	public ContactUs(String methodName, SlideMenuData slideMenuData) {
		Log.e("CONTACT US", "MEHTOD: " + methodName);
		this.mSlideMenuData = slideMenuData;
		if (methodName.equals("addItem")) {
			mItems = mSlideMenuData.getItemNavigations();
			ItemNavigation mItemNavigation = new ItemNavigation();
			mItemNavigation.setType(TypeItem.PLUGIN);
			mItemNavigation.setShowPopup(true);
			Drawable iconContactUs = MainActivity.context.getResources()
					.getDrawable(
							Rconfig.getInstance().drawable(
									"plugins_contactus_icon"));
			iconContactUs.setColorFilter(Config.getInstance().getColorMenu(),
					PorterDuff.Mode.SRC_ATOP);
			mItemNavigation.setName("Contact Us");
			mItemNavigation.setIcon(iconContactUs);
			mItems.add(mItemNavigation);

			ContactUsFragment fragment = ContactUsFragment.newInstance();
			mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
					fragment.getClass().getName());
		}
	}
}

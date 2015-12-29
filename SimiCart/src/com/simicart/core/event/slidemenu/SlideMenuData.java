package com.simicart.core.event.slidemenu;

import java.util.ArrayList;
import java.util.HashMap;

import com.simicart.core.slidemenu.entity.ItemNavigation;

public class SlideMenuData {
	protected ArrayList<ItemNavigation> mItemNavigations;
	protected ArrayList<ItemNavigation> mItemNavigationsFull;
	protected HashMap<String, String> mPluginFragment;

	public void setItemNavigations(ArrayList<ItemNavigation> mItemNavigations) {
		this.mItemNavigations = mItemNavigations;
	}

	public ArrayList<ItemNavigation> getItemNavigations() {
		return mItemNavigations;
	}

	public void setPluginFragment(HashMap<String, String> mPluginFragment) {
		this.mPluginFragment = mPluginFragment;
	}

	public HashMap<String, String> getPluginFragment() {
		return mPluginFragment;
	}

	public void setItemNavigationsFull(ArrayList<ItemNavigation> mItems) {
		this.mItemNavigationsFull = mItems;
	}

	public ArrayList<ItemNavigation> getmItemNavigationsFull() {
		return mItemNavigationsFull;
	}
}

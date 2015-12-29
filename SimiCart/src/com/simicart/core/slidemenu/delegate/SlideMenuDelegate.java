package com.simicart.core.slidemenu.delegate;

import java.util.ArrayList;

import com.simicart.core.slidemenu.entity.ItemNavigation;

public interface SlideMenuDelegate {
	public void onSelectedItem(int position);

	public void onRefresh();

	public void setAdapter(ArrayList<ItemNavigation> items);

	public void setUpdateSignIn(String name);
}

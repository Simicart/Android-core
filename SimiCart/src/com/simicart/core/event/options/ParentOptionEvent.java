package com.simicart.core.event.options;

import java.util.ArrayList;

import android.view.View;

import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.event.base.ItemMaster;
import com.simicart.core.event.base.SimiEvent;
import com.simicart.core.event.base.UtilsEvent;

public class ParentOptionEvent extends SimiEvent {

	ArrayList<ItemMaster> itemsList = UtilsEvent.itemsList;

	public void dispatchEvent(String eventName, View view,
			CacheOption cacheOption) {
		Object[] objects = new Object[2];
		objects[0] = view;
		objects[1] = cacheOption;
		super.disPatchEvent(eventName, objects, true);
	}

}

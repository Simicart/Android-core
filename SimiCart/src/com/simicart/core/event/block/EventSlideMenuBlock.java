package com.simicart.core.event.block;

import android.content.Context;
import android.view.View;

import com.simicart.core.event.base.SimiEvent;

public class EventSlideMenuBlock extends SimiEvent {

	public void dispatchEvent(String eventName, View rootView, Context context,
			CacheSlideMenuBlock cacheBlock) {
		Object[] objects = new Object[3];
		objects[0] = rootView;
		objects[1] = context;
		objects[2] = cacheBlock;
		super.disPatchEvent(eventName, objects, false);
	}

}

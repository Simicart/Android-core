package com.simicart.core.event.controller;

import android.content.Context;

import com.simicart.core.event.base.SimiEvent;
import com.simicart.core.slidemenu.block.SlideMenuBlock;

public class EventSlideMenuController extends SimiEvent {

	public void dispatchEvent(String eventName, SlideMenuBlock delegate,
			Context context, CacheSlideMenuController cache) {
		Object[] objects = new Object[3];
		objects[0] = delegate;
		objects[1] = context;
		objects[2] = cache;
		super.disPatchEvent(eventName, objects, false);
	}
}

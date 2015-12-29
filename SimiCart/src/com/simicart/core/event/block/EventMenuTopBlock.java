package com.simicart.core.event.block;

import android.content.Context;

import com.simicart.core.event.base.SimiEvent;

public class EventMenuTopBlock extends SimiEvent {

	public void dispatchEvent(String eventName, Context context,
			CacheMenuTopBlock cacheBlock) {
		Object[] objects = new Object[2];
		objects[0] = context;
		objects[1] = cacheBlock;
		super.disPatchEvent(eventName, objects, false);
	}

}

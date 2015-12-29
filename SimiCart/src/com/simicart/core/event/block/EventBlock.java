package com.simicart.core.event.block;

import android.content.Context;
import android.view.View;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.event.base.SimiEvent;

public class EventBlock extends SimiEvent {

	// event for hidden address
	public void dispatchEvent(String eventName) {
		Object[] objects = new Object[0];
		super.disPatchEvent(eventName, objects, true);
	}

	public void dispatchEvent(String eventName, CacheBlock cacheBlock) {
		Object[] objects = new Object[1];
		objects[0] = cacheBlock;
		super.disPatchEvent(eventName, objects, true);
	}

	public void dispatchEvent(String eventName, View view, SimiEntity entity) {
		Object[] objects = new Object[2];
		objects[0] = view;
		objects[1] = entity;
		super.disPatchEvent(eventName, objects, true);
	}

	// event for related product block
	public void dispatchEvent(String eventName, View view, Context context,
			CacheBlock cache) {
		Object[] objects = new Object[3];
		objects[0] = view;
		objects[1] = context;
		objects[2] = cache;
		super.disPatchEvent(eventName, objects, false);

	}
}

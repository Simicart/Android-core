/**
 * 
 * This class will dispatch an event for Google Analytics.
 */

package com.simicart.core.event.activity;

import com.simicart.core.event.base.SimiEvent;

public class EventActivity extends SimiEvent {

	public void dispatchEvent(String eventName, CacheActivity cacheActivity) {
		Object[] objects = new Object[1];
		objects[0] = cacheActivity;
		super.disPatchEvent(eventName, objects, true);
	}
}

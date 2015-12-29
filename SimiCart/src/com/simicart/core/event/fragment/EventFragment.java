package com.simicart.core.event.fragment;

import com.simicart.core.event.base.SimiEvent;

public class EventFragment extends SimiEvent {

	// dispatch edit or create new fragment
	public void dispatchEvent(String eventName, CacheFragment cacheFragment) {
		Object[] objects = new Object[1];
		objects[0] = cacheFragment;
		super.disPatchEvent(eventName, objects, true);
	}
}

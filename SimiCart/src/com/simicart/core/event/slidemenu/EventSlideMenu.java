package com.simicart.core.event.slidemenu;

import com.simicart.core.event.base.SimiEvent;

public class EventSlideMenu extends SimiEvent {

	// dispatch edit or create new fragment
	public void dispatchEvent(String eventName, SlideMenuData slideMenuData) {
		Object[] objects = new Object[1];
		objects[0] = slideMenuData;
		super.disPatchEvent(eventName, objects, true);
	}
}

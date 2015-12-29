package com.simicart.core.event.controller;

import com.simicart.MainActivity;
import com.simicart.core.event.base.SimiEvent;

public class EventController extends SimiEvent {

	// event for aotologin
//	public void dispatchEvent(String eventName, String typeSignIn,
//			UserNameDelegate mDelegate) {
//		Object[] objects = new Object[2];
//		objects[0] = typeSignIn;
//		objects[1] = mDelegate;
//		super.disPatchEvent(eventName, objects, true);
//	}

	// event for click banner
	public void dispatchEvent(String eventName, String banner) {
		Object[] objects = new Object[1];
		objects[0] = banner;
		super.disPatchEvent(eventName, objects, true);
	}

	// dippatchEvent for MainActivity
	public void dispatchEvent(String eventName, MainActivity mainActivity) {

		Object[] objects = new Object[1];
		objects[0] = mainActivity;
		super.disPatchEvent(eventName, objects, true);
	}

}

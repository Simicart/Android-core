package com.simicart.core.event.base;

import java.util.HashMap;
import java.util.Map;

public class EventListener {
	static public Map<String, String> listEvent = new HashMap<String, String>();

	public static void setEvent(String event) {
		if (!isEvent(event)) {
			listEvent.put(event, event);
		}
	}

	public static boolean isEvent(String event) {
		if (EventListener.listEvent.containsKey(event))
			return true;
		return false;
	}
}

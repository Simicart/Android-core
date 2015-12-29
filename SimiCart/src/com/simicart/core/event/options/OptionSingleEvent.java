package com.simicart.core.event.options;

import com.simicart.core.event.base.SimiEvent;

public class OptionSingleEvent extends SimiEvent {

	public void dispatchEvent(String eventName, DataOptionSingle data) {
		Object[] objects = new Object[1];
		objects[0] = data;
		super.disPatchEvent(eventName, objects, true);
	}

}

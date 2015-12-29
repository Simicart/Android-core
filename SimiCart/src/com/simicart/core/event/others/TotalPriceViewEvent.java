package com.simicart.core.event.others;

import android.util.Log;
import android.widget.TableLayout;

import com.simicart.core.event.base.SimiEvent;

public class TotalPriceViewEvent extends SimiEvent {

	public void dispatchEvent(String eventName,
			TotalPriceViewData totalPriceViewData, TableLayout tbl_price) {
		
		Log.e("TotalPriceViewEvent ", " dispatchEvent " + eventName);
		Object[] objects = new Object[2];
		objects[0] = totalPriceViewData;
		objects[1] = tbl_price;
		super.disPatchEvent(eventName, objects, false);
	}
}

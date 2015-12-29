package com.simicart.core.event.others;

import android.widget.TableLayout;

import com.simicart.core.event.base.SimiEvent;

public class AffilateCouponCodeEvent extends SimiEvent {

	public void dispatchEvent(String eventName,
			TotalPriceViewData totalPriceViewData, TableLayout tbl_price) {
		Object[] objects = new Object[2];
		objects[0] = totalPriceViewData;
		objects[1] = tbl_price;
		super.disPatchEvent(eventName, objects, true);
	}

}

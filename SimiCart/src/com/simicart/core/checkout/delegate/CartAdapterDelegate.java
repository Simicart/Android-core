package com.simicart.core.checkout.delegate;

import java.util.ArrayList;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public interface CartAdapterDelegate {
	public OnTouchListener getOnTouchListener(final int position);
	public OnClickListener getClickQtyItem(final int position, final int qty);
	public OnClickListener getClickItemCartListener(final int position, ArrayList<String> listID);
}

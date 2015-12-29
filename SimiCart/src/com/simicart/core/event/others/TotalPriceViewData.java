package com.simicart.core.event.others;

import android.content.Context;
import android.view.View;

import com.simicart.core.checkout.entity.TotalPrice;

public class TotalPriceViewData {
	Context context;
	TotalPrice totalPrice;
	View view;
	int textSize;
	String symbol;
	String colorLabel;
	String colorPrice;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public TotalPrice getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(TotalPrice totalPrice) {
		this.totalPrice = totalPrice;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public String getColorLabel() {
		return colorLabel;
	}

	public void setColorLabel(String colorLabel) {
		this.colorLabel = colorLabel;
	}

	public String getColorPrice() {
		return colorPrice;
	}

	public void setColorPrice(String colorPrice) {
		this.colorPrice = colorPrice;
	}

}

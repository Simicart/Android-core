package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.delegate.CreditCardDelegate;
import com.simicart.core.config.Config;

public class CreditCardController extends SimiController {

	protected CreditCardDelegate mDelegate;
	protected boolean isCheckedMethod;

	OnTouchListener onCLickSave;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onStart() {
		onCLickSave = new OnTouchListener() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					GradientDrawable gdDefault = new GradientDrawable();
					gdDefault.setColor(Color.GRAY);
					gdDefault.setCornerRadius(15);
					v.setBackgroundDrawable(gdDefault);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mDelegate.onCLickSave();
					SimiManager.getIntance().backPreviousFragment();
				}

				case MotionEvent.ACTION_CANCEL: {
					GradientDrawable gdDefault = new GradientDrawable();
					gdDefault.setColor(Config.getInstance().getColorMain());
					gdDefault.setCornerRadius(15);
					v.setBackgroundDrawable(gdDefault);
					break;
				}
				default:
					break;
				}
				return true;
			}
		};
	}

	@Override
	public void onResume() {

	}

	public void setDelegate(CreditCardDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	public void setIsCheckedMethod(boolean isCheckedMethod) {
		this.isCheckedMethod = isCheckedMethod;
	}

	public OnTouchListener getOnClickSave() {
		return onCLickSave;
	}

}

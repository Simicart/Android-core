package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.customer.fragment.SignInFragment;

@SuppressLint("ClickableViewAccessibility")
public class PopupCheckoutController extends SimiController {

	protected CartDelegate mBlockDelegate;
	protected OnTouchListener onCancel;
	protected OnTouchListener onExcustomer;
	protected OnTouchListener onNewcustomer;
	protected OnTouchListener onAsguest;

	public OnTouchListener getOnAsguest() {
		return onAsguest;
	}

	public OnTouchListener getOnCancel() {
		return onCancel;
	}

	public OnTouchListener getOnExcustomer() {
		return onExcustomer;
	}

	public OnTouchListener getOnNewcustomer() {
		return onNewcustomer;
	}

	@Override
	public void onStart() {
		cancelAction();
		exCustomerAction();
		newCustomerAction();
		guestAction();
	}

	@Override
	public void onResume() {

	}

	public void cancelAction() {
		onCancel = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					mBlockDelegate.dismissPopupCheckout();
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(0);
					break;
				}
				}
				return true;
			}
		};
	}

	public void exCustomerAction() {
		onExcustomer = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					mBlockDelegate.dismissPopupCheckout();
					SignInFragment fragment = SignInFragment.newInstance();
					fragment.setCheckout(true);
					if (DataLocal.isTablet) {
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						SimiManager.getIntance().replaceFragment(fragment);
					}
				}
				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(0);
					break;
				}
				}
				return true;
			}
		};
	}

	public void newCustomerAction() {
		onNewcustomer = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					mBlockDelegate.dismissPopupCheckout();

					NewAddressBookFragment fragment = NewAddressBookFragment
							.newInstance();
					fragment.setAfterControler(NewAddressBookFragment.NEW_CUSTOMER);
					if (DataLocal.isTablet) {
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						SimiManager.getIntance().replaceFragment(fragment);
					}
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(0);
					break;
				}
				}
				return true;
			}
		};
	}

	public void guestAction() {
		onAsguest = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					mBlockDelegate.dismissPopupCheckout();

					NewAddressBookFragment fragment = NewAddressBookFragment
							.newInstance();
					fragment.setAfterControler(NewAddressBookFragment.NEW_AS_GUEST);
					if (DataLocal.isTablet) {
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						SimiManager.getIntance().replaceFragment(fragment);
					}
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(0);
					break;
				}
				}
				return true;
			}
		};
	}

	public void setDelegate(CartDelegate mBlockDelegate) {
		this.mBlockDelegate = mBlockDelegate;
	}

}

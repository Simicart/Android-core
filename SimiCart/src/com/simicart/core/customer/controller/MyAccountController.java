package com.simicart.core.customer.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.OrderHistoryFragment;
import com.simicart.core.customer.fragment.ProfileFragment;

public class MyAccountController extends SimiController {
	OnClickListener mClickProfile;
	OnClickListener mClickAddress;
	OnClickListener mClickOrderHistory;
	OnClickListener mClickSignOut;

	protected SimiDelegate mDelegate;

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public OnClickListener getClickProfile() {
		return mClickProfile;
	}

	public OnClickListener getClickAddress() {
		return mClickAddress;
	}

	public OnClickListener getClickOrderHistory() {
		return mClickOrderHistory;
	}

	public OnClickListener getClickSignOut() {
		return mClickSignOut;
	}

	@Override
	public void onStart() {

		mDelegate.updateView(null);
		mClickProfile = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DataLocal.isTablet) {
					ProfileFragment fragment = ProfileFragment.newInstance();
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					ProfileFragment fragment = ProfileFragment.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		};

		mClickAddress = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DataLocal.isTablet) {
					AddressBookFragment fragment = AddressBookFragment
							.newInstance();
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					AddressBookFragment fragment = AddressBookFragment
							.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		};

		mClickOrderHistory = new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderHistoryFragment fragment = OrderHistoryFragment
						.newInstance();
				SimiManager.getIntance().removeDialog();
				SimiManager.getIntance().replaceFragment(fragment);
			}
		};

		mClickSignOut = new OnClickListener() {
			boolean isFirst = true;

			@Override
			public void onClick(View v) {
				if (isFirst) {
					isFirst = false;
					SimiManager.getIntance().removeDialog();
					SignOutController signout = new SignOutController();
					signout.setDelegate(mDelegate);
					signout.onStart();
				}
			}
		};
	}

	@Override
	public void onResume() {
	}
}

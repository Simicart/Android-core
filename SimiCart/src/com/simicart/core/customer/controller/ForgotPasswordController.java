package com.simicart.core.customer.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.customer.delegate.ForgotPasswordDelegate;
import com.simicart.core.customer.model.ForgotPasswordModel;

public class ForgotPasswordController extends SimiController {

	protected ForgotPasswordDelegate mDelegate;
	protected OnClickListener mClicker;

	public void setDelegate(ForgotPasswordDelegate delegate) {
		mDelegate = delegate;
	}

	public OnClickListener getClicker() {
		return mClicker;
	}

	@Override
	public void onStart() {
		mClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {

				onSend();
			}
		};

	}

	protected void onSend() {

		String email = mDelegate.getEmail();
		if (null == email || email.equals("")) {
			SimiManager.getIntance().showNotify(null,
					Config.getInstance().getText("Please enter an email."),
					Config.getInstance().getText("OK"));

			return;
		}
		mDelegate.showLoading();
		mModel = new ForgotPasswordModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					SimiManager.getIntance().showNotify(null, message,
							Config.getInstance().getText("OK"));
					SimiManager.getIntance().backPreviousFragment();
				} 
				else {
					mDelegate.showNotify(message);
				}

			}
		});
		mModel.addParam("user_email", email);
		mModel.request();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}

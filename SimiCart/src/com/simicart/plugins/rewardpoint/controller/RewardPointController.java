package com.simicart.plugins.rewardpoint.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.plugins.rewardpoint.model.ModelRewardPoint;

public class RewardPointController extends SimiController {
	

	@Override
	public void onStart() {
		mModel = new ModelRewardPoint();
		// mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					
					
				}else{
				}
			}
		};
		mModel.setDelegate(delegate);
		mModel.request();
	}

	@Override
	public void onResume() {

	}

}

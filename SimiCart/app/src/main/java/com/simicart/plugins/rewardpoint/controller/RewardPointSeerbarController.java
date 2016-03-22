package com.simicart.plugins.rewardpoint.controller;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ReviewOrderController;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.plugins.rewardpoint.model.ModelRewardSeerbar;

public class RewardPointSeerbarController extends ReviewOrderController {

	private int point;

	private View mView;
	private Context mContext;
	LinearLayout ll_layout_price;
	protected int mTextSize = 16;
	protected String mColorLabel = "#000000";
	protected String mColorPrice = "red";

	public RewardPointSeerbarController(int point, CacheBlock cacheBlock) {
		this.point = point;
		this.mDelegate = (ReviewOrderDelegate) cacheBlock.getBlock();
		this.mView = cacheBlock.getView();
		this.mContext = cacheBlock.getContext();
	}

	@Override
	public void onStart() {
		final ModelRewardSeerbar model = new ModelRewardSeerbar();
		mDelegate.showDialogLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					mtotalPrice = model.getTotalPrice();
					mDelegate.setTotalPrice(mtotalPrice);
				} else {
					SimiManager.getIntance().showNotify(message);
				}
			}
		};
		model.addParam("ruleid", "rate");
		model.addParam("usepoint", this.point + "");
		model.setDelegate(delegate);
		model.request();
	}

	@Override
	public void onResume() {

	}

}

package com.simicart.plugins.rewardpoint.model;

import com.simicart.core.checkout.model.CouponCodeModel;
import com.simicart.plugins.rewardpoint.utils.Constant;

public class ModelRewardSeerbar extends CouponCodeModel {

	@Override
	protected void setUrlAction() {
		url_action = Constant.CHANGE_SEERBAR;
	}

}

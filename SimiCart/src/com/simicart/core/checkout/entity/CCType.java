package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class CCType extends SimiEntity {

	private String mCode;
	private String mName;

	// }

	public String getCc_code() {
		if (null == mCode) {
			mCode = getData(Constants.CC_CODE);
		}
		return mCode;
	}

	public void setCc_code(String cc_code) {
		this.mCode = cc_code;
	}

	public String getCc_name() {
		if (null == mName) {
			mName = getData(Constants.CC_NAME);
		}
		return mName;
	}

	public void setCc_name(String cc_name) {
		this.mName = cc_name;
	}

}

package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class State extends SimiEntity {
	private String mID;
	private String mName;
	private String mCode;

	public String getState_id() {
		if (null == mID) {
			mID = getData(Constants.STATE_ID);
		}
		return mID;
	}

	public void setState_id(String state_id) {
		this.mID = state_id;
	}

	public String getState_name() {
		if (null == mName) {
			mName = getData(Constants.STATE_NAME);
		}
		return mName;
	}

	public void setState_name(String state_name) {
		this.mName = state_name;
	}

	public String getState_code() {
		if (null == mCode) {
			mCode = getData(Constants.STATE_CODE);
		}
		return mCode;
	}

	public void setState_code(String state_code) {
		this.mCode = state_code;
	}

}

package com.simicart.core.store.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Stores extends SimiEntity {
	protected String mID;
	protected String mName;
	protected String mCode;

	public String getStoreID() {
		if (null == mID) {
			mID = getData(Constants.STORE_ID);
		}
		return mID;
	}

	public void setStoreID(String store_id) {
		this.mID = store_id;
	}

	public String getStoreName() {
		if (null == mName) {
			mName = getData(Constants.STORE_NAME);
		}
		return mName;
	}

	public void setStoreName(String store_name) {
		this.mName = store_name;
	}

	public String getStoreCode() {

		if (null == mCode) {
			mCode = getData(Constants.STORE_CODE);
		}

		return mCode;
	}

	public void setStoreCode(String store_code) {
		this.mCode = store_code;
	}

}

package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class GenderConfig extends SimiEntity {
	protected String mLabel;
	protected String mValue;

	public String getLabel() {
		if (null == mLabel) {
			mLabel = getData(Constants.LABEL);
		}
		return mLabel;
	}

	public void setLabel(String label) {
		this.mLabel = label;
	}

	public String getValue() {
		if (null == mValue) {
			mValue = getData(Constants.VALUE);
		}
		return mValue;
	}

	public void setValue(String value) {
		this.mValue = value;
	}


}

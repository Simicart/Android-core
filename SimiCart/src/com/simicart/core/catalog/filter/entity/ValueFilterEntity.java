package com.simicart.core.catalog.filter.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.common.FilterConstant;
import com.simicart.core.common.Utils;

public class ValueFilterEntity extends SimiEntity {

	protected String mValue;
	protected boolean isSelected;
	protected String mLabel;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getmValue() {

		if (!Utils.validateString(mValue)) {
			mValue = getData(FilterConstant.VALUE);
		}

		return mValue;
	}

	public void setmValue(String mValue) {
		this.mValue = mValue;
	}

	public String getLabel() {

		if (!Utils.validateString(mLabel)) {
			mLabel = getData(FilterConstant.LABEL);
		}

		return mLabel;
	}

	public void setLabel(String mTitle) {
		this.mLabel = mTitle;
	}

}

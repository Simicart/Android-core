package com.simicart.core.catalog.filter.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.common.FilterConstant;

public class FilterState extends SimiEntity {

	protected String mAttribute;
	protected String mTitle;
	protected String mLabel;
	protected String mValue;

	public String getAttribute() {
		if (null == mAttribute) {
			mAttribute = getData(FilterConstant.ATTRIBUTE);
		}
		return mAttribute;
	}

	public void setAttribute(String mAttribute) {
		this.mAttribute = mAttribute;
	}

	public String getTitle() {
		if (null == mTitle) {
			mTitle = getData(FilterConstant.TITLE);
		}
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getLabel() {
		if (null == mLabel) {
			mLabel = getData(FilterConstant.LABEL);
		}
		return mLabel;
	}

	public void setLabel(String mLabel) {
		this.mLabel = mLabel;
	}

	public String getValue() {
		if (null == mValue) {
			mValue = getData(FilterConstant.VALUE);
		}
		return mValue;
	}

	public void setValue(String mValue) {
		this.mValue = mValue;
	}

}

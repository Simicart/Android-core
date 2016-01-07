package com.simicart.core.catalog.product.entity;

import java.io.Serializable;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Attributes extends SimiEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mTitle;
	private String mValue;

	public String getValue() {
		if (null == mValue) {
			mValue = getData(Constants.VALUE);
		} 
			return mValue;
	}

	public void setValue(String value) {
		this.mValue = value;
	}

	public String getTitle() {
		if(null == mTitle)
		{
			mTitle = getData(Constants.TITLE);
		}
		return this.mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}


}

package com.simicart.core.cms.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Cms extends SimiEntity {
	protected String mTitle;
	protected String mContent;
	protected String mIcon;

	public String getTitle() {

		if (null == mTitle) {
			mTitle = getData(Constants.TITLE);
		}
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getContent() {
		if(null == mContent)
		{
			mContent = getData(Constants.CONTENT);
		}
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public String getIcon() {
		if(null == mIcon)
		{
			mIcon = getData(Constants.ICON);
		}
		return mIcon;
	}

	public void setIcon(String icon) {
		this.mIcon = icon;
	}


}

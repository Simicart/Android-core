package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Condition extends SimiEntity {
	private String mTitle;
	private String mContent;
	private String mCheckText;
	private String mID;
	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public String getCheckText() {
		if (null == mCheckText) {
			mCheckText = getData(Constants.TITLE);
		}
		return mCheckText;
	}

	public void setCheckText(String checkText) {
		this.mCheckText = checkText;
	}

	public String getTitle() {
		if (null == mTitle) {
			mTitle = getData(Constants.NAME);
		}
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getContent() {
		if (null == mContent) {
			mContent = getData(Constants.CONTENT);
		}
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public String getId() {
		if (null == mID) {
			mID = getData(Constants.ID);
		}
		return mID;
	}

	public void setId(String id) {
		this.mID = id;
	}

}

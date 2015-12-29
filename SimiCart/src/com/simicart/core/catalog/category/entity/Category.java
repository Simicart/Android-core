package com.simicart.core.catalog.category.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Category extends SimiEntity {
	protected String mID;
	protected String mName;
	protected String mImage;
	protected String hasChild;
	
	public boolean hasChild() {
		if (null == hasChild) {
			hasChild = getData(Constants.HAS_CHILD);
		}
		if (null != hasChild && (hasChild.equals("YES") || hasChild.equals("yes")
				|| hasChild.equals("1"))) {
			return true;
		}
		return false;
	}

	public void setChild(boolean has_Child) {
		hasChild = String.valueOf(has_Child);
	}

	public String getCategoryImage() {
		if (null == mImage) {
			mImage = getData(Constants.CATEGORY_IMAGE);
		}

		return this.mImage;
	}
	public void setCategoryImage(String category_image) {
		this.mImage = category_image;
	}

	public String getCategoryName() {
		if (null == mName) {
			mName = getData(Constants.CATEGORY_NAME);
		}
		return this.mName;
	}

	public void setCategoryName(String category_name) {
		this.mName = category_name;
	}

	public String getCategoryId() {
		if (null == mID) {
			mID = getData(Constants.CATEGORY_ID);
		}
		return mID;
	}

	public void setCategoryId(String category_id) {

		this.mID = category_id;
	}

}

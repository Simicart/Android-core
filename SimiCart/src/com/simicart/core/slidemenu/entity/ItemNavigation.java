package com.simicart.core.slidemenu.entity;

import android.graphics.drawable.Drawable;

import com.simicart.core.base.model.entity.SimiEntity;

public class ItemNavigation extends SimiEntity {

	public enum TypeItem {
		NORMAL, CMS, PLUGIN
	}

	protected boolean isShowPopup = false;

	protected boolean isSparator;
	protected boolean isExtended;
	protected String mName;
	protected Drawable mIcon;
	protected String mUrl;
	protected TypeItem mType = TypeItem.NORMAL;

	public void setSparator(boolean sparator) {
		isSparator = sparator;
	}

	public boolean isShowPopup() {
		return isShowPopup;
	}

	public void setShowPopup(boolean isShowPopup) {
		this.isShowPopup = isShowPopup;
	}

	public boolean isSparator() {
		return isSparator;
	}

	public void setExtended(boolean extended) {
		isExtended = extended;
	}

	public boolean isExtended() {
		return isExtended;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setType(TypeItem type) {
		mType = type;
	}

	public TypeItem getType() {
		return mType;
	}

	public void setIcon(Drawable icon) {
		mIcon = icon;
	}

	public Drawable getIcon() {
		return mIcon;
	}

}

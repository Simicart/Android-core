package com.simicart.core.catalog.product.entity;

import java.io.Serializable;

import android.os.Parcel;

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

	public Attributes(Parcel in) {
		mTitle = in.readString();
		mValue = in.readString();
	}
	public Attributes() {
		// TODO Auto-generated constructor stub
	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(mTitle);
//		dest.writeString(mValue);
//	}
//
//	static final Parcelable.Creator<Attributes> CREATOR = new Parcelable.Creator<Attributes>() {
//
//		public Attributes createFromParcel(Parcel in) {
//			return new Attributes(in);
//		}
//
//		public Attributes[] newArray(int size) {
//			return new Attributes[size];
//		}
//	};
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//


}

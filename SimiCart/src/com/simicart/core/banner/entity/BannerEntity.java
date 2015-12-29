package com.simicart.core.banner.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class BannerEntity extends SimiEntity {
	protected String mImagePath;
	protected String uURL;
	protected String mType;
	protected String mCategoryId;
	protected String mCategoryName;
	protected String hasChild;
	protected String mProductId;
	
	public String getProductId() {
		if(null == mProductId){
			mProductId = getData("productID");
		}
		return this.mProductId;
	}
	
	public void setProductId(String mProductId) {
		this.mProductId = mProductId;
	}
	
	public String getHasChild() {
		if(null == hasChild){
			hasChild = getData(Constants.HAS_CHILD);
		}
		return this.hasChild;
	}
	
	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getUrl() {
		if(null == uURL)
		{
			uURL = getData(Constants.URL);
		}
		return this.uURL;
	}

	public void setUrl(String url) {
		this.uURL = url;
	}

	public String getImage() {
		if(null == mImagePath)
		{
			mImagePath = getData(Constants.IMAGE_PATH);
		}
		
		return this.mImagePath;
	}

	public void setImage(String image_path) {
		this.mImagePath = image_path;
	}

	public String getType() {
		if(null == mType){
			mType = getData("type");
			if(mType == null){
				mType = "-1";
			}
		}
		return this.mType;
	}
	
	public void setType(String mType) {
		this.mType = mType;
	}
	
	public String getCategoryName() {
		if(null == mCategoryName){
			mCategoryName = getData("categoryName");
		}
		return this.mCategoryName;
	}
	
	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}
	
	public String getCategoryId() {
		if(null == mCategoryId){
			mCategoryId = getData("categoryID");
		}
		return this.mCategoryId;
	}
	
	public void setCategoryId(String mCategoryId) {
		this.mCategoryId = mCategoryId;
	}
}

package com.simicart.theme.matrixtheme.home.entity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Constants;

public class Theme1Category extends SimiEntity {

	private String mCategoryID;
	private String mCategoryName;
	private boolean hasChild = false;
	private ArrayList<String> mUrlImages;
	private JSONObject mJsonData;

	public JSONObject getJsonData() {
		return mJsonData;
	}

	public Theme1Category(JSONObject json) {
		mJsonData = json;
		mUrlImages = new ArrayList<String>();
		try {
			parse(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Theme1Category(Category category) {
		mCategoryID = "" + category.getCategoryId();
		mCategoryName = category.getCategoryName();
	}

	private void parse(JSONObject json) throws JSONException {

		if (json.has(Constants.CATEGORY_ID)) {
			mCategoryID = json.getString(Constants.CATEGORY_ID);
		}
		if (json.has(Constants.CATEGORY_NAME)) {
			mCategoryName = json.getString(Constants.CATEGORY_NAME);
		}
		if (json.has(Constants.HAS_CHILD)) {

			String child = json.getString(Constants.HAS_CHILD);
			if (child.equals(Constants.YES)) {
				hasChild = true;
			}
		}
		if (json.has(Constants.IMAGES)) {
			String url = json.getString(Constants.IMAGES);
			url = url.replace("[", "");
			url = url.replace("]", "");
			String[] arrUrl = url.split(",");
			for (String string : arrUrl) {
				string = string.replace("\"", "");
				string = string.replace("\\/", "/");
				mUrlImages.add(string);
			}
		}
	}

	public String getCategoryID() {
		return mCategoryID;
	}

	public void setCategoryID(String mCategoryID) {
		this.mCategoryID = mCategoryID;
	}

	public String getCategoryName() {
		return mCategoryName;
	}

	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public ArrayList<String> getUrlImages() {
		return mUrlImages;
	}

	public void setUrlImages(ArrayList<String> mUrlImages) {
		this.mUrlImages = mUrlImages;
	}

}

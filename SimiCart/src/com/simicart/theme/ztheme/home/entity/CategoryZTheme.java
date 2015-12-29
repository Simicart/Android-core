package com.simicart.theme.ztheme.home.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.catalog.category.entity.Category;
import com.simicart.theme.ztheme.home.common.ConstantsZTheme;

public class CategoryZTheme extends Category {

	public static final int TYPE_CAT = 0;
	public static final int TYPE_SPOT = 1;

	int type = -1;
	protected String mTitle;
	ArrayList<CategoryZTheme> mCategories = new ArrayList<>();
	Category category = new Category();

	SpotProductZTheme spotProductZTheme;

	public ArrayList<CategoryZTheme> getmCategories() {
		try {
			if (mCategories.size() == 0 && hasChild()) {
				JSONArray array = getJSONObject().getJSONArray(
						ConstantsZTheme.CHILD_CAT);
				if (null != array && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						CategoryZTheme category = new CategoryZTheme();
						category.setJSONObject(object);
						mCategories.add(category);
					}
				}
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), "");
		}
		return mCategories;
	}

	public void setmCategories(ArrayList<CategoryZTheme> mCategories) {
		this.mCategories = mCategories;
	}

	public void setSpotProductZTheme(SpotProductZTheme spotProductZTheme) {
		this.spotProductZTheme = spotProductZTheme;
	}

	public SpotProductZTheme getSpotProductZTheme() {
		if (spotProductZTheme == null) {
			spotProductZTheme = new SpotProductZTheme();
			spotProductZTheme.setJSONObject(getJSONObject());
		}
		return spotProductZTheme;
	}

	public int getType() {
		if (type == -1) {
			String sType = getData("type");
			if (sType.equals("spot")) {
				type = TYPE_SPOT;
			} else {
				type = TYPE_CAT;
			}
		}
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		if (null == mTitle) {
			mTitle = getData(ConstantsZTheme.TITLE);
		}
		if (mTitle.equals("null")) {
			mTitle = "";
		}
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
}

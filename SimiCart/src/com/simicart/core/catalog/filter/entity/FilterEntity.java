package com.simicart.core.catalog.filter.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.common.FilterConstant;
import com.simicart.core.common.Utils;

public class FilterEntity extends SimiEntity {

	protected String mAttribute;

	protected String mTitle;

	protected ArrayList<ValueFilterEntity> mValueFilters;

	public String getmAttribute() {

		if (!Utils.validateString(mAttribute)) {
			mAttribute = getData(FilterConstant.ATTRIBUTE);
		}

		return mAttribute;
	}

	public void setmAttribute(String mAttribute) {
		this.mAttribute = mAttribute;
	}

	public String getmTitle() {

		if (!Utils.validateString(mTitle)) {
			mTitle = getData(FilterConstant.TITLE);
		}

		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public ArrayList<ValueFilterEntity> getmValueFilters() {

		if (null == mValueFilters) {
			try {
				JSONArray array = new JSONArray(getData(FilterConstant.FILTER));

				if (null != array && array.length() > 0) {
					mValueFilters = new ArrayList<ValueFilterEntity>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						ValueFilterEntity entity = new ValueFilterEntity();
						entity.setJSONObject(json);
						mValueFilters.add(entity);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mValueFilters;
	}

	public void setmValueFilters(ArrayList<ValueFilterEntity> mValueFilters) {
		this.mValueFilters = mValueFilters;
	}

}

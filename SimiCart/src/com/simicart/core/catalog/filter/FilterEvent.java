package com.simicart.core.catalog.filter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.filter.common.FilterConstant;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;

public class FilterEvent {
	protected FilterRequestDelegate mDelegate;
	protected JSONObject jsonData;
	protected boolean has_Data;

	public boolean hasData() {
		return has_Data;
	}

	public void setJSON(JSONObject json) {
		has_Data = true;
		jsonData = json;
	}

	public void setDelegate(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public FilterEvent(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public View initView(JSONObject json, Context context,String catName) {
		if (null != json) {
			if (json.has(FilterConstant.LAYEREDNAVIGATION)) {
				has_Data = true;
				try {
					JSONObject jsonFilter = json
							.getJSONObject(FilterConstant.LAYEREDNAVIGATION);
					// layer state

					ArrayList<FilterState> states = new ArrayList<FilterState>();
					if (jsonFilter.has(FilterConstant.LAYER_STATE)) {
						JSONArray array_state = jsonFilter
								.getJSONArray(FilterConstant.LAYER_STATE);
						if (null != array_state && array_state.length() > 0) {
							for (int i = 0; i < array_state.length(); i++) {
								JSONObject object = array_state
										.getJSONObject(i);
								FilterState state = new FilterState();
								state.setJSONObject(object);
								states.add(state);
							}
						}
					}

					// layer filter

					ArrayList<FilterEntity> filters = new ArrayList<FilterEntity>();
					if (jsonFilter.has(FilterConstant.LAYER_FILTER)) {
						JSONArray array_filter = jsonFilter
								.getJSONArray(FilterConstant.LAYER_FILTER);
						if (null != array_filter && array_filter.length() > 0) {
							for (int i = 0; i < array_filter.length(); i++) {
								JSONObject object = array_filter
										.getJSONObject(i);
								FilterEntity entity = new FilterEntity();
								entity.setJSONObject(object);
								filters.add(entity);
							}
						}
					}
					// initial filter view
					FilterView filterView = new FilterView(context, filters,
							mDelegate);
					filterView.setState(states);
					Button btn_filter = (Button) filterView.initView(catName) ;
					return btn_filter;
				} catch (JSONException e) {
					return null;
				}

			}
		}
		return null;
	}

	public View initView(Context context,String catName) {
		if (null != jsonData) {
			return initView(jsonData, context,catName);
		}
		return null;
	}

}

package com.simicart.core.catalog.product.model;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.CustomerReview;
import com.simicart.core.config.Constants;

public class CustomerReviewModel extends SimiModel {

	public CustomerReviewModel() {
		super();
	}

	@Override
	protected void setUrlAction() {
		// TODO Auto-generated method stub
		this.url_action = Constants.GET_PRODUCT_REVIEW;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			Log.e("CustomerReviewModel JSON ", mJSON.toString());
			if (null == collection) {
				collection = new SimiCollection();
			}
			for (int i = 0; i < list.length(); i++) {
				CustomerReview entity = new CustomerReview();
				entity.setJSONObject(list.getJSONObject(i));
				collection.addEntity(entity);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

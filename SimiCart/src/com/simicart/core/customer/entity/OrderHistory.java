package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class OrderHistory extends SimiEntity {
	private String mID;
	private String mStatus;
	private String mDate;
	private String mRecipent;
	private ArrayList<String> mItems;


	public String getOrder_id() {
		if (null == mID) {
			mID = getData(Constants.ORDER_ID);
		}
		return mID;
	}

	public void setOrder_id(String order_id) {
		this.mID = order_id;
	}

	public String getOrder_status() {
		if (null == mStatus) {
			mStatus = getData(Constants.ORDER_STATUS);
		}
		return mStatus;
	}

	public void setOrder_status(String order_status) {
		this.mStatus = order_status;
	}

	public String getOrder_date() {
		if (null == mDate) {
			mDate = getData(Constants.ORDER_DATE);
		}
		return mDate;
	}

	public void setOrder_date(String order_date) {
		this.mDate = order_date;
	}

	public String getRecipient() {
		if (null == mRecipent) {
			mRecipent = getData(Constants.RECIPIENT);
		}
		return mRecipent;
	}

	public void setRecipient(String recipient) {
		this.mRecipent = recipient;
	}

	public ArrayList<String> getOrder_items() {
		if (null == mItems || mItems.size() == 0) {
			mItems = new ArrayList<String>();
			try {
				JSONArray array = new JSONArray(getData(Constants.ORDER_ITEMS));
				if (null != array && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject nameJSON = array.getJSONObject(i);
						if (null != nameJSON) {
							String name = nameJSON
									.getString(Constants.PRODUCT_NAME);
							if (null != name && !name.isEmpty()
									&& !name.equals("null")) {
								mItems.add(name);
							}
						}

					}
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return mItems;
	}

	public void setOrder_items(ArrayList<String> order_items) {
		this.mItems = order_items;
	}

}

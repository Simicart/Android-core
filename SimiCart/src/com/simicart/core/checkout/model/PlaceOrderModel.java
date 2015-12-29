package com.simicart.core.checkout.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.notification.entity.NotificationEntity;

public class PlaceOrderModel extends SimiModel {

	protected String mInvoiceNumber;
	protected String enable = "0";
	protected NotificationEntity notificationEntity;
	protected JSONObject js_placeOrder;

	public String getEnable() {
		return enable;
	}

	public NotificationEntity getNotificationEntity() {
		return notificationEntity;
	}

	public String getInvoiceNumber() {
		return mInvoiceNumber;
	}
	
	public JSONObject getJs_placeOrder() {
		return js_placeOrder;
	}

	@Override
	protected void paserData() {
		try {

			Log.e("PlaceOrderModel JSON : ", mJSON.toString());

			JSONArray js_Data = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			js_placeOrder = js_Data.getJSONObject(0);
			mInvoiceNumber = js_placeOrder.getString(Constants.INVOICE_NUMBER);

			JSONObject js_notice = js_placeOrder.getJSONObject("notification");
			notificationEntity = new NotificationEntity();
			if (js_notice.has("show_popup")) {
				enable = js_notice.getString("show_popup");
			}
			if (js_notice.has(Constants.MESSAGE)) {
				notificationEntity.setMessage(js_notice
						.getString(Constants.MESSAGE));
			}
			if (js_notice.has(Constants.TITLE)) {
				notificationEntity.setTitle(js_notice
						.getString(Constants.TITLE));
			}
			if (js_notice.has(Constants.URL)) {
				notificationEntity.setUrl(js_notice.getString(Constants.URL));
			}
			if (js_notice.has("type")) {
				notificationEntity.setType(js_notice.getString("type"));
			}
			if (js_notice.has("categoryID")) {
				notificationEntity.setCategoryID(js_notice
						.getString("categoryID"));
			}
			if (js_notice.has("categoryName")) {
				notificationEntity.setCategoryName(js_notice
						.getString("categoryName"));
			}
			if (js_notice.has("has_child")) {
				notificationEntity
						.setHasChild(js_notice.getString("has_child"));
			}
			if (js_notice.has("imageUrl")) {
				notificationEntity.setImage(js_notice.getString("imageUrl"));
			}
			if (js_notice.has("productID")) {
				notificationEntity.setProductID(js_notice
						.getString("productID"));
			}
			if (js_notice.has("show_popup")) {
				notificationEntity.setShowPopup(js_notice
						.getString("show_popup"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.PLACE_ORDER;
	}

}

package com.simicart.plugins.barcode;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;

public class ScanCodeModel extends SimiModel {

	private String mProductID;

	public String getProductID() {
		return mProductID;
	}

	@Override
	protected void paserData() {
		try {
			JSONObject jsonData = mJSON.getJSONObject("data");
			if (jsonData.has("product_id")) {
				mProductID = jsonData.getString("product_id");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = "simibarcode/index/checkCode";
	}

}

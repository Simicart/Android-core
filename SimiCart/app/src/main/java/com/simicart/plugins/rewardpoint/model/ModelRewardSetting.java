package com.simicart.plugins.rewardpoint.model;

import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.rewardpoint.utils.Constant;

public class ModelRewardSetting extends SimiModel{

	protected int mQty;

	public int getQty() {
		return mQty;
	}

	@Override
	protected void paserData() {
		try {
			JSONObject object = this.mJSON;
			collection = new SimiCollection();
			JSONObject objectData = object.getJSONObject("data");
			// Passbook passbook = new Passbook();
			// passbook.setJSONObject(objectData);
			// collection.addEntity(passbook);
//			collection.setJSON(objectData);
			Collections.reverse(collection.getCollection());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = Constant.GET_SETTING;
	}
}

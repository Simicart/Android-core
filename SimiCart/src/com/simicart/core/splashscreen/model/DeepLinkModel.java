package com.simicart.core.splashscreen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class DeepLinkModel extends SimiModel {
	public void paserData() {
		try {
			JSONObject obj = this.getDataJSON().getJSONObject("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			if (null != obj) {
				SimiEntity entity = new SimiEntity();
				entity.setJSONObject(obj);
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

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.DEEP_LINK;
	}
}

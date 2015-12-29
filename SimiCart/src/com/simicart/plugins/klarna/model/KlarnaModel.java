package com.simicart.plugins.klarna.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;

public class KlarnaModel extends SimiModel {

	protected JSONArray jsDataKlarna;

	public JSONArray getDataKlarna() {
		return jsDataKlarna;
	}

	@Override
	protected void paserData() {
		try {
			jsDataKlarna = this.mJSON.getJSONArray("data");
		} catch (JSONException e) {

		}
	}

	@Override
	protected void setUrlAction() {
		url_action = KlarnaFragment.URL_GETPARAMS_KLARNA;
	}

}

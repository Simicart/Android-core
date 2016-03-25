package com.simicart.core.splashscreen.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModelCloud;
import com.simicart.core.base.networkcloud.request.multi.SimiRequest;

/**
 * Created by MSI on 18/01/2016.
 */
public class GetSKUPluginModel extends SimiModelCloud {

	private ArrayList<String> mSKU;
	private String public_plugins = "public_plugins";
	private String sku = "sku";

	public ArrayList<String> getListSKU() {
		return mSKU;
	}

	@Override
	protected void setUrlAction() {
		addDataExtendURL("public-plugins/");

	}

	@Override
	protected void setTypeMethod() {
		mTypeMethod = SimiRequest.Method.GET;
	}

	@Override
	protected void paserData() {
		super.paserData();
		Log.e("getSKUPlugin SUCCESS", "JSON: " + mJSONResult.toString());
		if (mJSONResult.has(public_plugins)) {
			try {
				mSKU = new ArrayList<String>();
				JSONArray array = mJSONResult.getJSONArray(public_plugins);
				if (null != array && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonPlugin = array.getJSONObject(i);
						if (jsonPlugin.has(sku)) {
							String sku_plugin = jsonPlugin.getString(sku);
							Log.e("GetSKUPluginModel ", "SKU " + sku_plugin);
							mSKU.add(sku_plugin);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

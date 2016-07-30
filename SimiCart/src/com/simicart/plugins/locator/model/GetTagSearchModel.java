package com.simicart.plugins.locator.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

import android.util.Log;

public class GetTagSearchModel extends SimiModel {
	
	protected ArrayList<String> listTags;
	
	public ArrayList<String> getTags() {
		return listTags;
	}

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		Log.e("GetTagList", mJSON.toString());
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			listTags = new ArrayList<>();
			for (int i = 0; i < list.length(); i++) {
				String tag = list.getString(i);
				listTags.add(tag);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		// TODO Auto-generated method stub
		url_action = "storelocator/api/get_tag_list";
	}

}

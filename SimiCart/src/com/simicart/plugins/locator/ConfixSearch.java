package com.simicart.plugins.locator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConfixSearch {
	public List<String> list;
	private String result;

	public List<String> getResult(JSONObject jsonObject) {
		try {
			result = jsonObject.getString("status");
			if (result.equals("SUCCESS")) {
				JSONArray array = jsonObject.getJSONArray("data");
				if (array != null && array.length() != 0) {
					list = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						String value = array.getString(i);
						list.add(value);
					}
				}
			}
		} catch (Exception e) {

		}
		return list;
	}
}

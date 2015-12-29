package com.simicart.plugins.locator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TagParser {
	public List<String> list_tag;
	private String result;

	public List<String> getResult(JSONObject jsonObject) {
		try {
			result = jsonObject.getString("status");
			if (result.equals("SUCCESS")) {
				list_tag = new ArrayList<String>();
				JSONArray array = jsonObject.getJSONArray("data");
				if (array != null && array.length() != 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String value = object.getString("value");
						list_tag.add(value);
					}
				}
			}
		} catch (Exception e) {

		}
		return list_tag;
	}
}

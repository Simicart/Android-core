package com.simicart.plugins.locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simicart.core.config.Config;
import com.simicart.plugins.locator.entity.CountryObject;

public class CountryParser {
	public List<CountryObject> list;
	private String result;

	public List<CountryObject> getResult(JSONObject jsonObject) {
		try {
			result = jsonObject.getString("status");
			if (result.equals("SUCCESS")) {
				JSONArray array = jsonObject.getJSONArray("data");
				if (array != null && array.length() != 0) {
					list = new ArrayList<CountryObject>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						CountryObject countryObject = new CountryObject();
						countryObject.setCountry_code(object
								.getString("country_code"));
						countryObject.setCountry_name(object
								.getString("country_name"));
						list.add(countryObject);
					}
					
					Collections.sort(list, new Comparator<CountryObject>(){

						@Override
						public int compare(CountryObject lhs, CountryObject rhs) {
							// TODO Auto-generated method stub
							return (lhs.getCountry_name().substring(0, 1).compareToIgnoreCase(rhs.getCountry_name().substring(0, 1)));
						}
					});
					
					CountryObject countryObjectf = new CountryObject();
					countryObjectf.setCountry_code("");
					countryObjectf.setCountry_name(Config.getInstance()
							.getText("None"));
					list.add(0, countryObjectf);
				}
			}
		} catch (Exception e) {

		}
		return list;
	}
}

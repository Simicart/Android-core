package com.simicart.plugins.locator.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.plugins.locator.entity.CountryObject;
import com.simicart.plugins.locator.parser.CountryParser;

public class GetSearchCountryModel extends SimiModel {
	
	protected ArrayList<CountryObject> listCountries;
	
	public ArrayList<CountryObject> getCountries() {
		return listCountries;
	}
	
	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			CountryParser countryParser = new CountryParser();
			listCountries = new ArrayList<>(countryParser.getResult(mJSON));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		url_action = "storelocator/api/get_country_list";
	}
	
	@Override
	protected void setEnableCache() {
		// TODO Auto-generated method stub
		enableCache = true;
	}

}

package com.simicart.core.customer.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class CountryAllowed extends SimiEntity {
	protected String country_code;
	protected String country_name;
	protected ArrayList<StateOfCountry> stateList;

	public CountryAllowed() {
		stateList = new ArrayList<StateOfCountry>();
	}

	public String getCountry_code() {
		if (null == country_code) {
			country_code = getData(Constants.COUNTRY_CODE);
		}
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCountry_name() {
		if (null == country_name) {
			country_name = getData(Constants.COUNTRY_NAME);
		}
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public ArrayList<StateOfCountry> getStateList() {
		if (null == stateList || stateList.size() <= 0) {
			try {
				JSONArray array = new JSONArray(getData(Constants.STATES));
				if (array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						StateOfCountry state = new StateOfCountry();
						state.setJSONObject(obj);
						stateList.add(state);
					}
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return stateList;
	}

	public void setStateList(ArrayList<StateOfCountry> stateList) {
		this.stateList = stateList;
	}

}

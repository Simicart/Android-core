package com.simicart.core.checkout.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Option extends SimiEntity {
	protected String mtitle;
	protected String mvalue;
	protected String mprice;

	public String getOption_title() {
		if (null == mtitle) {
			mtitle = getData(Constants.OPTION_TITLE);
		}
		return mtitle;
	}

	public void setOption_title(String option_title) {
		this.mtitle = option_title;
	}

	public String getOption_value() {
		if (null == mvalue) {
			mvalue = getData(Constants.OPTION_VALUE);
		}
		return mvalue;
	}

	public void setOption_value(String option_value) {
		this.mvalue = option_value;
	}

	public String getOption_price() {
		if (null == mprice) {
			mprice = getData(Constants.OPTION_PRICE);
		}
		return mprice;
	}

	public void setOption_price(String option_price) {
		this.mprice = option_price;
	}

	public boolean parse(JSONObject json) {
		try {
			if (json.has(Constants.OPTION_PRICE)) {
				mprice = json.getString(Constants.OPTION_PRICE);
			}

			if (json.has(Constants.OPTION_TITLE)) {
				mtitle = json.getString(Constants.OPTION_TITLE);
			}

			if (json.has(Constants.OPTION_VALUE)) {
				mvalue = json.getString(Constants.OPTION_VALUE);
			}
			return true;
		} catch (JSONException e) {
			return false;
		}

	}

}

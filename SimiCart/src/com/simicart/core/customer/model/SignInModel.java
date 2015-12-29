package com.simicart.core.customer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class SignInModel extends SimiModel {

	protected String mUserName;
	protected String mCartQty;

	public String getName() {
		return mUserName;
	}

	public String getCartQty() {
		return mCartQty;
	}

	@Override
	protected void paserData() {
		// TODO Auto-generated method stub
		super.paserData();
		try {
			DataLocal.dataJson.clear();
			JSONArray arr = mJSON.getJSONArray(Constants.DATA);
			JSONObject json = arr.getJSONObject(0);
			mUserName = json.getString(Constants.USER_NAME);
			mCartQty = json.getString(Constants.CART_QTY);
		} catch (JSONException e) {
			Log.e("SignInModel JSONException ", e.getMessage());
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.SIGN_IN;
	}

}

package com.simicart.plugins.hiddenaddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.ConfigCustomerAddress;

public class HiddenAddressModel extends SimiModel {

	@Override
	protected void paserData() {

		try {
			JSONArray list = this.mJSON.getJSONArray(Constants.DATA);
			collection = new SimiCollection();
			if (null != list && list.length() > 0) {
				JSONObject object = list.getJSONObject(0);
				ConfigCustomerAddress address = new ConfigCustomerAddress();
				address.setJSONObject(object);
				paserConfigAddress(address);
				collection.addEntity(address);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String getData(String key, JSONObject jsonObject) {
		if (jsonObject != null && jsonObject.has(key)) {
			try {
				return jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private void paserConfigAddress(ConfigCustomerAddress address) {
		address.setPrefix(address.getData(Constants.PREFIX));
		address.setSuffix(address.getData(Constants.SUFFIX));
		address.setCompany(address.getData(Constants.COMPANY));
		address.setStreet(address.getData(Constants.STREET));
		address.setCountry(address.getData(Constants.COUNTRY));
		address.setVat_id(address.getData(Constants.TAXVAT));
		address.setCity(address.getData(Constants.CITY));
		address.setState(address.getData(Constants.STATE));
		address.setZipcode(address.getData(Constants.zIP_CODE));
		address.setTelephone(address.getData(Constants.TELEPHONE));
		address.setFax(address.getData(Constants.FAX));
		address.setDob(address.getData(Constants.BIRTH_DAY));
		address.setGender(address.getData(Constants.GENDER));
		address.setGenderConfigs(DataLocal.ConfigCustomerProfile
				.getGenderConfigs());
	}

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void setUrlAction() {
		url_action = "hideaddress/api/get_address_show";
	}

}

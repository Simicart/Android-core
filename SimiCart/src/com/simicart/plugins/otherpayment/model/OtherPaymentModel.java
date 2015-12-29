package com.simicart.plugins.otherpayment.model;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.plugins.otherpayment.config.OtherPaymentConfig;
import com.simicart.plugins.otherpayment.entity.OtherPaymentEntity;

public class OtherPaymentModel extends SimiModel {

	@Override
	protected void setUrlAction() {
		url_action = "simicustompayment/api/get_custom_payments";
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			if (list.length() > 0) {
				for (int i = 0; i < list.length(); i++) {
					OtherPaymentEntity otherpayment = new OtherPaymentEntity();
					otherpayment.setJSONObject(list.getJSONObject(i));
					OtherPaymentConfig.getInstance().getListPayment()
							.add(otherpayment);
				}
			}
			for (int i = 0; i < OtherPaymentConfig.getInstance()
					.getListPayment().size(); i++) {
				Log.e("Payment", OtherPaymentConfig.getInstance()
						.getListPayment().get(i).getPaymentMethod());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setShowNotifi() {
		this.isShowNotify = false;
	}
}

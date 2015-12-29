package com.simicart.plugins.rewardpoint.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.plugins.rewardpoint.utils.Constant;

public class ItemPointData {

	private int loyalty_point;
	private int spending_point = 1;
	private int loyalty_card;
	private String spending_discount = "";
	private String earning_label = "";
	private String earning_policy = "";
	private String spending_label = "";
	private String spending_policy;
	private String start_discount = "";
	private String loyalty_image = "";

	private int is_notification;
	private int expire_notification;
	private String loyalty_redeem = "";
	private String passBookLogo = "";
	private int spending_mint = 1;
	private JSONArray array_policies;
	private String passbook_text = "";
	private String background_passbook = "";
	private String passbook_foreground = "";
	private String passbook_barcode = "";

	public ItemPointData getItemPointFromJson(JSONObject object) {
		ItemPointData pointData = new ItemPointData();
		try {
			JSONObject jsonObject = object.getJSONObject("data");
			if(jsonObject.has("policies")){
				pointData.setArray_policies(jsonObject.getJSONArray("policies"));
			}
			if (jsonObject.has(Constant.LOYALTY_POINT)
					&& jsonObject.getInt(Constant.LOYALTY_POINT) != -1) {
				pointData.setLoyalty_point(jsonObject
						.getInt(Constant.LOYALTY_POINT));
			}
			if (jsonObject.has(Constant.IS_NOTIFICATION)
					&& jsonObject.getInt(Constant.IS_NOTIFICATION) != -1) {
				pointData.setIs_notification(jsonObject
						.getInt(Constant.IS_NOTIFICATION));
			}
			if (jsonObject.has(Constant.EXPIRE_NOTIFICATION)
					&& jsonObject.getInt(Constant.EXPIRE_NOTIFICATION) != -1) {
				pointData.setExpire_notification(jsonObject
						.getInt(Constant.EXPIRE_NOTIFICATION));
			}
			if (jsonObject.has(Constant.LOYALTY_REDEEM)
					&& jsonObject.getString(Constant.LOYALTY_REDEEM) != null) {
				pointData.setLoyalty_redeem(jsonObject
						.getString(Constant.LOYALTY_REDEEM));
			}
			if (jsonObject.has(Constant.PASSBOOK_LOGO)) {
				pointData.setLoyalty_image(jsonObject
						.getString(Constant.PASSBOOK_LOGO));
			}
			if (jsonObject.has(Constant.SPENDING_DISCOUNT)
					&& jsonObject.getString(Constant.SPENDING_DISCOUNT) != null) {
				// spending_discount = jsonObject
				// .getString(Constant.SPENDING_DISCOUNT);
				pointData.setSpending_discount(jsonObject
						.getString(Constant.SPENDING_DISCOUNT));
				if (jsonObject.has(Constant.LOYALTY_CARD)) {
					pointData.setLoyalty_card(jsonObject
							.getInt(Constant.LOYALTY_CARD));
				}
			}
			if (jsonObject.has(Constant.EARNING_LABEL)
					&& jsonObject.getString(Constant.EARNING_LABEL) != null) {
				pointData.setEarning_label(jsonObject
						.getString(Constant.EARNING_LABEL));
			}
			if (jsonObject.has(Constant.EARNING_POLICY)
					&& jsonObject.getString(Constant.EARNING_POLICY) != null) {
				pointData.setEarning_policy(jsonObject
						.getString(Constant.EARNING_POLICY));
			}
			if (jsonObject.has(Constant.SPENDING_LABEL)
					&& jsonObject.getString(Constant.SPENDING_LABEL) != null) {
				pointData.setSpending_label(jsonObject
						.getString(Constant.SPENDING_LABEL));
			}
			if (jsonObject.has(Constant.SPENDING_POLICY)
					&& jsonObject.getString(Constant.SPENDING_POLICY) != null) {
				pointData.setSpending_policy(jsonObject
						.getString(Constant.SPENDING_POLICY));
			}
			if (jsonObject.has(Constant.START_DISCOUNT)) {
				pointData.setStart_discount(jsonObject
						.getString(Constant.START_DISCOUNT));
			}
			if (jsonObject.has(Constant.SPENDING_MIN)) {
				pointData.setSpending_mint(spending_mint = jsonObject
						.getInt(Constant.SPENDING_MIN));
			}
			if (jsonObject.has(Constant.SPENDING_POINT)) {
				pointData.setSpending_point(jsonObject
						.getInt(Constant.SPENDING_POINT));
			}
			if(jsonObject.has(Constant.PASSBOOK_TEXT)) {
				pointData.setPassbook_text(jsonObject
						.getString(Constant.PASSBOOK_TEXT));
				
			}
			if(jsonObject.has(Constant.BACKGROUND_PASSBOOK)){
				pointData.setBackground_passbook(jsonObject
						.getString(Constant.BACKGROUND_PASSBOOK));
			}
			if(jsonObject.has(Constant.PASSBOOK_FOREGROUND)){
				pointData.setPassbook_foreground(jsonObject
						.getString(Constant.PASSBOOK_FOREGROUND));
			}
			if(jsonObject.has(Constant.PASSBOOK_BARCODE)){
				pointData.setPassbook_barcode(jsonObject
						.getString(Constant.PASSBOOK_BARCODE));
			}
		} catch (Exception e) {
			Log.d("Error parse Json rewardpoint:", e.getStackTrace() + "");
		}
		return pointData;
	}

	public int getLoyalty_point() {
		return loyalty_point;
	}

	public void setLoyalty_point(int loyalty_point) {
		this.loyalty_point = loyalty_point;
	}

	public int getSpending_point() {
		return spending_point;
	}

	public void setSpending_point(int spending_point) {
		this.spending_point = spending_point;
	}

	public int getLoyalty_card() {
		return loyalty_card;
	}

	public void setLoyalty_card(int loyalty_card) {
		this.loyalty_card = loyalty_card;
	}

	public String getSpending_discount() {
		return spending_discount;
	}

	public void setSpending_discount(String spending_discount) {
		this.spending_discount = spending_discount;
	}

	public String getEarning_label() {
		return earning_label;
	}

	public void setEarning_label(String earning_label) {
		this.earning_label = earning_label;
	}

	public String getEarning_policy() {
		return earning_policy;
	}

	public void setEarning_policy(String earning_policy) {
		this.earning_policy = earning_policy;
	}

	public String getSpending_label() {
		return spending_label;
	}

	public void setSpending_label(String spending_label) {
		this.spending_label = spending_label;
	}

	public String getSpending_policy() {
		return spending_policy;
	}

	public void setSpending_policy(String spending_policy) {
		this.spending_policy = spending_policy;
	}

	public String getStart_discount() {
		return start_discount;
	}

	public void setStart_discount(String start_discount) {
		this.start_discount = start_discount;
	}

	public String getLoyalty_image() {
		return loyalty_image;
	}

	public void setLoyalty_image(String loyalty_image) {
		this.loyalty_image = loyalty_image;
	}

	public void setIs_notification(int is_notification) {
		this.is_notification = is_notification;
	}

	public int getIs_notification() {
		return is_notification;
	}

	public void setExpire_notification(int expire_notification) {
		this.expire_notification = expire_notification;
	}

	public int getExpire_notification() {
		return expire_notification;
	}

	public String getLoyalty_redeem() {
		return loyalty_redeem;
	}

	public void setLoyalty_redeem(String loyalty_redeem) {
		this.loyalty_redeem = loyalty_redeem;
	}

	public void setPassBookLogo(String passBookLogo) {
		this.passBookLogo = passBookLogo;
	}

	public String getPassBookLogo() {
		return passBookLogo;
	}

	public void setSpending_mint(int spending_mint) {
		this.spending_mint = spending_mint;
	}

	public int getSpending_mint() {
		return spending_mint;
	}
	public void setArray_policies(JSONArray array_policies) {
		this.array_policies = array_policies;
	}
	public JSONArray getArray_policies() {
		return array_policies;
	}
	
	public void setPassbook_text(String passbook_text) {
		this.passbook_text = passbook_text;
	}
	public String getPassbook_text() {
		return passbook_text;
	}
	public String getBackground_passbook() {
		return background_passbook;
	}
	public void setBackground_passbook(String background_passbook) {
		this.background_passbook = background_passbook;
	}
	public void setPassbook_foreground(String passbook_foreground) {
		this.passbook_foreground = passbook_foreground;
	}
	public String getPassbook_foreground() {
		return passbook_foreground;
	}
	public void setPassbook_barcode(String passbook_barcode) {
		this.passbook_barcode = passbook_barcode;
	}
	public String getPassbook_barcode() {
		return passbook_barcode;
	}
}

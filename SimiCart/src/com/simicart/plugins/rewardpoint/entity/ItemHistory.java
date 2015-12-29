package com.simicart.plugins.rewardpoint.entity;

import org.json.JSONObject;

public class ItemHistory {

	private String title;
	private String pointLabel;
	private String createTime;
	private String expirationTime;
	
	public ItemHistory() {
	}

	public ItemHistory(String title,String pointLabel,String createTime,String expirationTiem) {
		this.title = title;
		this.pointLabel = pointLabel;
		this.createTime = createTime;
		this.expirationTime = expirationTiem;
	}
	
	public ItemHistory getHistoryFromJson(JSONObject object){
		ItemHistory itemHistory = new ItemHistory();
		try {
			String title = object.getString("title");
			String pointLabel = object.getString("point_label");
			String createTime = object.getString("created_time");
			String expirationDate = object.getString("expiration_date");
			itemHistory.setTitle(title);
			itemHistory.setPointLabel(pointLabel);
			itemHistory.setCreateTime(createTime);
			itemHistory.setExpirationTime(expirationDate);
			return itemHistory;
		} catch (Exception e) {
		}
		return itemHistory;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPointLabel() {
		return pointLabel;
	}

	public void setPointLabel(String pointLabel) {
		this.pointLabel = pointLabel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

}

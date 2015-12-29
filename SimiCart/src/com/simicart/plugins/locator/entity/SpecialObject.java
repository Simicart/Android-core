package com.simicart.plugins.locator.entity;

public class SpecialObject {
	private String id;
	private String date = "2014-06-22";
	private String time_open = "06:00";
	private String time_close = "15:00";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime_open() {
		return time_open;
	}

	public void setTime_open(String time_open) {
		this.time_open = time_open;
	}

	public String getTime_close() {
		return time_close;
	}

	public void setTime_close(String time_close) {
		this.time_close = time_close;
	}

}

package com.simicart.plugins.locator.entity;

public class SearchObject {
	private String city;
	private String state;
	private String zipcode;
	private int tag;
	private String name_country;
	private int position_country;
	
	public SearchObject() {
		city = "";
		state = "";
		zipcode = "";
		tag = 0;
		name_country = "";
		position_country = 0;
	}
	
	public int getPosition_country() {
		return position_country;
	}
	
	public void setPosition_country(int position_country) {
		this.position_country = position_country;
	}
	
	public String getName_country() {
		return name_country;
	}
	
	public void setName_country(String name_country) {
		this.name_country = name_country;
	}
	
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public int getTag() {
		return tag;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}

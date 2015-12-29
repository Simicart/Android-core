package com.simicart.plugins.locator.entity;

import java.util.List;

public class StoreObject {
	private String storeID;
	private String name;
	private String address;
	private String city;
	private String country;
	private String zipcode;
	private String state;
	private String state_id;
	private String email;
	private String phone;
	private String fax;
	private String description;
	private String status;
	private String sort;
	private String link;
	private String latitude;
	private String longtitude;
	private String monday_status;
	private String monday_open;
	private String monday_close;
	private String tuesday_status;
	private String tuesday_open;
	private String tuesday_close;
	private String wednesday_status;
	private String wednesday_open;
	private String wednesday_close;
	private String thursday_status;
	private String thursday_open;
	private String thursday_close;
	private String friday_status;
	private String friday_open;
	private String friday_close;
	private String saturday_status;
	private String saturday_open;
	private String saturday_close;
	private String sunday_status;
	private String sunday_open;
	private String sunday_close;
	private String zoom_level;
	private String image_icon;
	private String distance;
	// haita
	private String country_name;
	// end
	private List<SpecialObject> list_special;
	private List<SpecialObject> list_holiday;

	public String getCountryName() {
		return this.country_name;
	}

	public void setCountryName(String country_name) {
		this.country_name = country_name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState_id() {
		return state_id;
	}

	public void setState_id(String state_id) {
		this.state_id = state_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getMonday_status() {
		return monday_status;
	}

	public void setMonday_status(String monday_status) {
		this.monday_status = monday_status;
	}

	public String getMonday_open() {
		return monday_open;
	}

	public void setMonday_open(String monday_open) {
		this.monday_open = monday_open;
	}

	public String getMonday_close() {
		return monday_close;
	}

	public void setMonday_close(String monday_close) {
		this.monday_close = monday_close;
	}

	public String getTuesday_status() {
		return tuesday_status;
	}

	public void setTuesday_status(String tuesday_status) {
		this.tuesday_status = tuesday_status;
	}

	public String getTuesday_open() {
		return tuesday_open;
	}

	public void setTuesday_open(String tuesday_open) {
		this.tuesday_open = tuesday_open;
	}

	public String getTuesday_close() {
		return tuesday_close;
	}

	public void setTuesday_close(String tuesday_close) {
		this.tuesday_close = tuesday_close;
	}

	public String getWednesday_status() {
		return wednesday_status;
	}

	public void setWednesday_status(String wednesday_status) {
		this.wednesday_status = wednesday_status;
	}

	public String getWednesday_open() {
		return wednesday_open;
	}

	public void setWednesday_open(String wednesday_open) {
		this.wednesday_open = wednesday_open;
	}

	public String getWednesday_close() {
		return wednesday_close;
	}

	public void setWednesday_close(String wednesday_close) {
		this.wednesday_close = wednesday_close;
	}

	public String getThursday_status() {
		return thursday_status;
	}

	public void setThursday_status(String thursday_status) {
		this.thursday_status = thursday_status;
	}

	public String getThursday_open() {
		return thursday_open;
	}

	public void setThursday_open(String thursday_open) {
		this.thursday_open = thursday_open;
	}

	public String getThursday_close() {
		return thursday_close;
	}

	public void setThursday_close(String thursday_close) {
		this.thursday_close = thursday_close;
	}

	public String getFriday_status() {
		return friday_status;
	}

	public void setFriday_status(String friday_status) {
		this.friday_status = friday_status;
	}

	public String getFriday_open() {
		return friday_open;
	}

	public void setFriday_open(String friday_open) {
		this.friday_open = friday_open;
	}

	public String getFriday_close() {
		return friday_close;
	}

	public void setFriday_close(String friday_close) {
		this.friday_close = friday_close;
	}

	public String getSaturday_status() {
		return saturday_status;
	}

	public void setSaturday_status(String saturday_status) {
		this.saturday_status = saturday_status;
	}

	public String getSaturday_open() {
		return saturday_open;
	}

	public void setSaturday_open(String saturday_open) {
		this.saturday_open = saturday_open;
	}

	public String getSaturday_close() {
		return saturday_close;
	}

	public void setSaturday_close(String saturday_close) {
		this.saturday_close = saturday_close;
	}

	public String getSunday_status() {
		return sunday_status;
	}

	public void setSunday_status(String sunday_status) {
		this.sunday_status = sunday_status;
	}

	public String getSunday_open() {
		return sunday_open;
	}

	public void setSunday_open(String sunday_open) {
		this.sunday_open = sunday_open;
	}

	public String getSunday_close() {
		return sunday_close;
	}

	public void setSunday_close(String sunday_close) {
		this.sunday_close = sunday_close;
	}

	public String getZoom_level() {
		return zoom_level;
	}

	public void setZoom_level(String zoom_level) {
		this.zoom_level = zoom_level;
	}

	public String getImage_icon() {
		return image_icon;
	}

	public void setImage_icon(String image_icon) {
		this.image_icon = image_icon;
	}

	public List<SpecialObject> getList_special() {
		return list_special;
	}

	public void setList_special(List<SpecialObject> list_special) {
		this.list_special = list_special;
	}

	public List<SpecialObject> getList_holiday() {
		return list_holiday;
	}

	public void setList_holiday(List<SpecialObject> list_holiday) {
		this.list_holiday = list_holiday;
	}

}

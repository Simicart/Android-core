package com.simicart.core.catalog.product.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class CacheOption {
	private String option_type_id = "-1";
	private int position = -1;
	private int number = 0;
	private int day = -1;
	private int month = -1;
	private int year = -1;
	private int hour = -1;
	private int minute = -1;
	private String text = "";
	private boolean is_required;
	private boolean complete_required;
	private String option_title;
	private String option_type;
	private float price_multi = 0;
	private ArrayList<ProductOption> options;
	protected HashMap<String, String> dependence_option_id_selected;

	public boolean isChecked() {
		if (null != options) {
			for (ProductOption option : options) {
				if (option.isChecked()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isDependence() {
		if (options.get(0) != null
				&& options.get(0).getDependence_options() != null) {
			return true;
		}
		return false;
	}

	public HashMap<String, String> getDependence_option_id_selected() {
		if (complete_required) {
			for (ProductOption option : options) {
				if (option.isChecked()
						&& option.getDependence_options() != null) {
					dependence_option_id_selected = null;
					dependence_option_id_selected = option
							.getDependence_options();
					return dependence_option_id_selected;
				}
			}
		}
		return null;
	}

	public CacheOption() {
		this.options = new ArrayList<ProductOption>();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getOptionValue() {
		switch (getOptionType()) {
		case "text":
			return text;
		case "date":
			String dayString = "" + day;
			String monthString = "" + month;
			if (day < 10) {
				dayString = "0" + day;
			}
			if (month < 10) {
				monthString = "0" + month;
			}
			if (day == -1) {
				return "";
			}
			return year + "-" + monthString + "-" + dayString;
		case "date_time":
			String hourString2 = "" + hour;
			String minuteString2 = "" + minute;
			String dayString2 = "" + day;
			String monthString2 = "" + month;
			if (day < 10) {
				dayString2 = "0" + day;
			}
			if (month < 10) {
				monthString2 = "0" + month;
			}
			if (hour < 10) {
				hourString2 = "0" + hour;
			}
			if (minute < 10) {
				minuteString2 = "0" + minute;
			}
			if (day == -1 || hour == -1) {
				return "";
			}
			return year + "-" + monthString2 + "-" + dayString2 + " "
					+ hourString2 + ":" + minuteString2 + ":01";
		case "time":
			String hourString = "" + hour;
			String minuteString = "" + minute;
			if (hour < 10) {
				hourString = "0" + hour;
			}
			if (minute < 10) {
				minuteString = "0" + minute;
			}
			if (hour == -1) {
				return "";
			}
			return hourString + ":" + minuteString + ":01";
		default:
			break;
		}
		return "";
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getPriceMulti() {
		return Math.round(price_multi * 100) / 100f;
	}

	public void setPriceMulti(float price_multi) {
		this.price_multi = price_multi;
	}

	public String getOptionTypeId() {
		return option_type_id;
	}

	public void setOptionTypeId(String option_type_id) {
		this.option_type_id = option_type_id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isRequired() {
		return is_required;
	}

	public void setRequired(boolean is_required) {
		this.is_required = is_required;
	}

	public boolean isCompleteRequired() {
		return complete_required;
	}

	public void setCompleteRequired(boolean complete_required) {
		this.complete_required = complete_required;
	}

	public String getOptionType() {
		return this.option_type;
	}

	public void setOptionType(String option_type) {
		this.option_type = option_type;
	}

	public String getOptionTitle() {
		return this.option_title;
	}

	public void setOptionTitle(String option_title) {
		this.option_title = option_title;
	}

	public void addOption(ProductOption option) {
		this.options.add(option);
	}

	public ArrayList<ProductOption> getAllOption() {
		return this.options;
	}

	public JSONObject toParameter() throws JSONException {
		JSONObject json = null;
		ArrayList<ProductOption> options = getAllOption();
		if (null != options && options.size() > 0) {
			for (ProductOption option : options) {

				String Optiontype = getOptionType();
				String value = getOptionValue();
				if ((option.getOptionCart() == null || option.getOptionCart()
						.isEmpty())
						&& (Optiontype.equals("text")
								|| Optiontype.equals("date")
								|| Optiontype.equals("time") || Optiontype
									.equals("date_time")) && !value.isEmpty()) {
					json = new JSONObject();
					json.put("option_id", option.getOptionId());
					json.put("option_type_id", option.getOptionTypeId());
					json.put("option_type", option.getOptionType());
					json.put("option_select_type", option.getOptionType());
					json.put("option_qty", getNumber());
					HashMap<String, String> dependOption = option
							.getDependence_options();
					if (dependOption != null && dependOption.size() != 0) {
						String depends = "";
						for (String dep : dependOption.keySet()) {
							depends += (dep + ",");
						}
						json.put("dependence_option_ids", depends);
					}

					json.put("option_value", value);
				}

				else if (option.isChecked() || getNumber() > 0) {
					json = new JSONObject();
					json.put("option_id", option.getOptionId());
					json.put("option_type_id", option.getOptionTypeId());
					json.put("option_type", option.getOptionType());
					json.put("option_select_type", option.getOptionType());
					json.put("option_qty", getNumber());
					HashMap<String, String> dependOption = option
							.getDependence_options();
					if (dependOption != null && dependOption.size() != 0) {
						String depends = "";
						for (String dep : dependOption.keySet()) {
							depends += (dep + ",");
						}
						json.put("dependence_option_ids", depends);
					}
				}
			}
		}
		return json;
	}

}

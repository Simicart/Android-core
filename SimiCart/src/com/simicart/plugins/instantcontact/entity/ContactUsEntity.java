package com.simicart.plugins.instantcontact.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.plugins.instantcontact.common.ContactUsConstant;

public class ContactUsEntity extends SimiEntity {

	private ArrayList<String> emails;
	private ArrayList<String> phones;
	private ArrayList<String> message;
	private String website;
	private String style;
	private String activecolor;
	private int imageContactUs;
	private String nameContactUs;
	
	public ContactUsEntity() {
		phones = new ArrayList<String>();
		emails = new ArrayList<String>();
		message = new ArrayList<String>();
	}

	public void setActiveColor(String color) {
		activecolor = color;
	}

	public String getActiveColor() {
		if (null == activecolor) {
			activecolor = getData(ContactUsConstant.ACTIVECOLOR);
		}
		return activecolor;
	}

	public ArrayList<String> getMessage() {
		if (message.size() == 0) {
			String value = getData(ContactUsConstant.MESSAGE);
			if (null != value) {
				try {
					JSONArray array = new JSONArray(value);
					for (int i = 0; i < array.length(); i++) {
						String mes = array.getString(i);
						if (null != mes && !mes.equals("")
								&& !mes.equals("null")) {
							message.add(mes);
						}
					}
				} catch (JSONException e) {
					return message;
				}
			}
		}
		return message;
	}

	public void setMessage(ArrayList<String> message) {
		this.message = message;
	}

	public ArrayList<String> getEmail() {
		if (emails.size() == 0) {
			String value = getData(ContactUsConstant.EMAIL);
			if (null != value) {
				try {
					JSONArray array = new JSONArray(value);
					if (null != array && array.length() > 0) {
						for (int i = 0; i < array.length(); i++) {
							String mail = array.getString(i);
							if (null != mail && !mail.equals("")
									&& !mail.equals("null")) {
								emails.add(mail);
							}
						}
					}
				} catch (JSONException e) {
					return emails;
				}
			}
		}

		return emails;
	}

	public void setEmail(ArrayList<String> email) {
		this.emails = email;
	}

	public ArrayList<String> getPhone() {
		if (phones.size() == 0) {
			String value = getData(ContactUsConstant.PHONE);

			if (null != value) {
				try {
					JSONArray array = new JSONArray(value);
					if (null != array && array.length() > 0) {
						for (int i = 0; i < array.length(); i++) {
							String phone = array.getString(i);
							if (null != phone && !phone.equals("")
									&& !phone.equals("null")) {
								phones.add(phone);
							}
						}
					}
				} catch (JSONException e) {
					return phones;
				}
			}
		}

		return phones;
	}

	public void setPhone(ArrayList<String> phone) {
		this.phones = phone;
	}

	public String getWebsite() {
		if (null == website) {
			website = getData(ContactUsConstant.WEB_SITE);
		}
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getStyle() {
		if (null == style) {
			style = getData(ContactUsConstant.STYLE);
		}
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	
	public ContactUsEntity(ArrayList<String> emails, ArrayList<String> phones,
			ArrayList<String> message, String website, String activecolor, String stype,
			int imageContactUs, String nameContactUs) {
		super();
		this.emails = emails;
		this.phones = phones;
		this.message = message;
		this.website = website;
		this.activecolor = activecolor;
		this.style = stype;
		this.imageContactUs = imageContactUs;
		this.nameContactUs = nameContactUs;
	}

	public ContactUsEntity(int imageContactUs, String nameContactUs) {
		super();
		this.imageContactUs = imageContactUs;
		this.nameContactUs = nameContactUs;
	}

	public int getImageContactUs() {
		return imageContactUs;
	}

	public void setImageContactUs(int imageContactUs) {
		this.imageContactUs = imageContactUs;
	}

	public String getNameContactUs() {
		return nameContactUs;
	}

	public void setNameContactUs(String nameContactUs) {
		this.nameContactUs = nameContactUs;
	}
	
}

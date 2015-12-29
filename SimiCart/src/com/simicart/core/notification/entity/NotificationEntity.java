package com.simicart.core.notification.entity;

import java.io.Serializable;

public class NotificationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String showPopup = "1";
	String title = "";
	String message;
	String url;
	String image;
	String type = "0";
	String productID;
	String categoryID;
	String categoryName;
	String hasChild;

	public String getTitle() {
		return title;
	}

	public String getShowPopup() {
		return showPopup;
	}

	public void setShowPopup(String showPopup) {
		this.showPopup = showPopup;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getProductID() {
		return productID;
		// return "172";
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public boolean equals(Object object) {
		if (!showPopup.equals(((NotificationEntity) object).getShowPopup())) {
			return false;
		}
		if (!title.equals(((NotificationEntity) object).getTitle())) {
			return false;
		}
		if (!message.equals(((NotificationEntity) object).getMessage())) {
			return false;
		}
		if (!image.equals(((NotificationEntity) object).getImage())) {
			return false;
		}
		if (!url.equals(((NotificationEntity) object).getUrl())) {
			return false;
		}
		if (!type.equals(((NotificationEntity) object).getType())) {
			return false;
		}
		if (!productID.equals(((NotificationEntity) object).getProductID())) {
			return false;
		}
		if (!categoryID.equals(((NotificationEntity) object).getCategoryID())) {
			return false;
		}
		return true;
	}
}

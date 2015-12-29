package com.simicart.core.catalog.product.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class Review extends SimiEntity {
	private String id;
	private String title;
	private String content;
	private String time;
	private String rate;
	private String customer_name;

	public String getId() {
		if (null == id) {
			id = getData(Constants.REVIEW_ID);
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		if (null == title) {
			title = getData(Constants.REVIEW_TITLE);
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		if (null == content) {
			content = getData(Constants.REVIEW_BODY);
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		if (null == time) {
			time = getData(Constants.REVIEW_TIME);
		}
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRate() {
		if (null == rate) {
			rate = getData(Constants.RATE_POINT);
		}

		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getCustomer_name() {
		if (null == customer_name) {
			customer_name = getData(Constants.CUSTOMER_NAME);
		}
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

}

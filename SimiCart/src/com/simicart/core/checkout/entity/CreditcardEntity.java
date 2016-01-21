package com.simicart.core.checkout.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CreditcardEntity implements Serializable {
	private String paymentType;
	private String paymentNumber;
	private String paymentMonth;
	private String paymentYear;
	private String paymentCvv;

	public CreditcardEntity() {
		super();
	}

	public CreditcardEntity(String paymentType, String paymentNumber,
			String paymentMonth, String paymentYear, String paymentCvv) {
		super();
		this.paymentType = paymentType;
		this.paymentNumber = paymentNumber;
		this.paymentMonth = paymentMonth;
		this.paymentYear = paymentYear;
		this.paymentCvv = paymentCvv;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public String getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	public String getPaymentYear() {
		return paymentYear;
	}

	public void setPaymentYear(String paymentYear) {
		this.paymentYear = paymentYear;
	}

	public String getPaymentCvv() {
		return paymentCvv;
	}

	public void setPaymentCvv(String paymentCvv) {
		this.paymentCvv = paymentCvv;
	}
}

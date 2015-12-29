package com.simicart.plugins.otherpayment.config;

import java.util.ArrayList;

import com.simicart.plugins.otherpayment.entity.OtherPaymentEntity;

public class OtherPaymentConfig {
	private static OtherPaymentConfig instance;
	private ArrayList<OtherPaymentEntity> mListPayment;
	
	public OtherPaymentConfig() {
		mListPayment = new ArrayList<OtherPaymentEntity>();
	}
	
	public static OtherPaymentConfig getInstance(){
		if (null == instance) {
			instance = new OtherPaymentConfig();
		}
		
		return instance;
	}
	
	public ArrayList<OtherPaymentEntity> getListPayment() {
		return mListPayment;
	}
	
	public void setListPayment(ArrayList<OtherPaymentEntity> mListPayment) {
		this.mListPayment = mListPayment;
	}
}

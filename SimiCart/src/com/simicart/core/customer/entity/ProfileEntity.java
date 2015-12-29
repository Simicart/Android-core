package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

public class ProfileEntity extends SimiEntity {

	private String mName;
	private String mEmail;
	private String mPrefix;
	private String mSuffix;
	private String mDay;
	private String mMonth;
	private String mYear;
	private String mGender;
	
	private String mTaxVat;

	private String mCurrentPass;
	private String mNewPass;
	private String mConfirmPass;

	public String getCurrentPass() {
		return mCurrentPass;
	}

	public void setCurrentPass(String _currentPass) {
		this.mCurrentPass = _currentPass;
	}

	public String getNewPass() {
		return mNewPass;
	}

	public void setNewPass(String _newPass) {
		this.mNewPass = _newPass;
	}

	public String getConfirmPass() {
		return mConfirmPass;
	}

	public void setConfirmPass(String _confirmPass) {
		this.mConfirmPass = _confirmPass;
	}
	
	public String getTaxVat(){
		if (null == mTaxVat) {
			mTaxVat = getData(Constants.TAXVAT);
		}
		return mTaxVat;
	}
	
	public void setTaxVat(String _taxVat) {
		this.mTaxVat = _taxVat;
	}

	public String getName() {
		if (null == mName) {
			mName = getData(Constants.USER_NAME);
		}
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getEmail() {
		if (null == mEmail) {
			mEmail = getData(Constants.USER_EMAIL);
		}
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getPrefix() {
		if (null == mPrefix) {
			mPrefix = getData(Constants.PREFIX);
		}
		return mPrefix;
	}

	public void setPrefix(String mPrefix) {
		this.mPrefix = mPrefix;
	}

	public String getSuffix() {
		if (null == mSuffix) {
			mSuffix = getData(Constants.SUFFIX);
		}
		return mSuffix;
	}

	public void setSuffix(String mSuffix) {
		this.mSuffix = mSuffix;
	}

	public String getDay() {
		if (null == mDay) {
			mDay = getData(Constants.DAY);
		}
		return mDay;
	}

	public void setDay(String mDay) {
		this.mDay = mDay;
	}

	public String getMonth() {
		if (null == mMonth) {
			mMonth = getData(Constants.MONTH);
		}
		return mMonth;
	}

	public void setMonth(String mMonth) {
		this.mMonth = mMonth;
	}

	public String getYear() {
		if (null == mYear) {
			mYear = getData(Constants.YEAR);
		}
		return mYear;
	}

	public void setYear(String mYear) {
		this.mYear = mYear;
	}

	public String getGender() {
		if (null == mGender) {
			mGender = Utils.getLabelGender(getData(Constants.GENDER));
		}
		return mGender;
	}

	public void setGender(String mGender) {
		this.mGender = mGender;
	}

}

package com.simicart.plugins.otherpayment.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class OtherPaymentEntity extends SimiEntity {
	protected String mPaymentMethod;
	protected String mUrlRedirect;
	protected String mUrlSuccess;
	protected String mUrlFail;
	protected String mUrlCancel;
	protected String mUrlError;
	protected String mMessageSuccess;
	protected String mMessageFail;
	protected String mMessageCancel;
	protected String mMessageError;
	protected String mCheckUrl;
	protected String mUrlCheck;
	protected String mTitleUrlAction;
	
	public String getUrlCheck() {
		if(mUrlCheck == null){
			mUrlCheck = getData("url_check");
		}
		return mUrlCheck;
	}
	
	public void setUrlCheck(String mUrlCheck) {
		this.mUrlCheck = mUrlCheck;
	}
	
	public void setTitleUrlAction(String mTitleUrlAction) {
		this.mTitleUrlAction = mTitleUrlAction;
	}
	
	public String getTitleUrlAction() {
		if(mTitleUrlAction == null){
			mTitleUrlAction = getData("title_url_action");
		}
		return mTitleUrlAction;
	}

	public String getPaymentMethod() {
		if (mPaymentMethod == null) {
			mPaymentMethod = getData("paymentmethod");
		}
		return mPaymentMethod;
	}

	public void setPaymentMethod(String mPaymentMethod) {
		this.mPaymentMethod = mPaymentMethod;
	}

	public String getUrlRedirect() {
		if (mUrlRedirect == null) {
			mUrlRedirect = getData("url_redirect");
		}
		return mUrlRedirect;
	}

	public void setUrlRedirect(String mUrlRedirect) {
		this.mUrlRedirect = mUrlRedirect;
	}

	public String getUrlSuccess() {
		if (mUrlSuccess == null) {
			mUrlSuccess = getData("url_success");
		}
		return mUrlSuccess;
	}

	public void setUrlSuccess(String mUrlSuccess) {
		this.mUrlSuccess = mUrlSuccess;
	}

	public String getUrlFail() {
		if (mUrlFail == null) {
			mUrlFail = getData("url_fail");
		}
		return mUrlFail;
	}

	public void setUrlFail(String mUrlFail) {
		this.mUrlFail = mUrlFail;
	}

	public String getUrlCancel() {
		if (mUrlCancel == null) {
			mUrlCancel = getData("url_cancel");
		}
		return mUrlCancel;
	}

	public void setUrlCancel(String mUrlCancel) {
		this.mUrlCancel = mUrlCancel;
	}

	public String getUrlError() {
		if (mUrlError == null) {
			mUrlError = getData("url_error");
		}
		return mUrlError;
	}

	public void setUrlError(String mUrlError) {
		this.mUrlError = mUrlError;
	}

	public String getMessageSuccess() {
		if (mMessageSuccess == null) {
			mMessageSuccess = getData("message_success");
		}
		return mMessageSuccess;
	}

	public void setMessageSuccess(String mMessageSuccess) {
		this.mMessageSuccess = mMessageSuccess;
	}

	public String getMessageFail() {
		if (mMessageFail == null) {
			mMessageFail = getData("message_fail");
		}
		return mMessageFail;
	}

	public void setMessageFail(String mMessageFail) {
		this.mMessageFail = mMessageFail;
	}

	public String getMessageCancel() {
		if (mMessageCancel == null) {
			mMessageCancel = getData("message_cancel");
		}
		return mMessageCancel;
	}

	public void setMessageCancel(String mMessageCancel) {
		this.mMessageCancel = mMessageCancel;
	}

	public String getMessageError() {
		if (mMessageError == null) {
			mMessageError = getData("message_error");
		}
		return mMessageError;
	}

	public void setMessageError(String mMessageError) {
		this.mMessageError = mMessageError;
	}

	public String getCheckUrl() {
		if (mCheckUrl == null) {
			mCheckUrl = getData("ischeckurl");
		}
		return mCheckUrl;
	}

	public void setCheckUrl(String mCheckUrl) {
		this.mCheckUrl = mCheckUrl;
	}
}

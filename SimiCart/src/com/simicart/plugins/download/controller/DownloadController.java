package com.simicart.plugins.download.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.plugins.download.delegate.DownloadDelegate;
import com.simicart.plugins.download.model.DownloadModel;

public class DownloadController extends SimiController{
	protected DownloadDelegate mDelegate;
	
	public void setDelegate(DownloadDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}
	
	@Override
	public void onStart() {
		requestGetDownLoad();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetDownLoad(){
		mDelegate.showLoading();
		mModel = new DownloadModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}else{
					mDelegate.getMessage(message);
				}
			}
		});
		mModel.request();
	}

}

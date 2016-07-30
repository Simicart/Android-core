package com.simicart.plugins.locator.controller;

import java.util.ArrayList;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.plugins.locator.delegate.StoreLocatorMapDelegate;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.model.ModelLocator;

public class StoreLocatorMapController extends SimiController {
	
	protected StoreLocatorMapDelegate mDelegate;
	
	public void setDelegate(StoreLocatorMapDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		//requestGetStore();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetStore() {
//		mDelegate.showDialogLoading();
		mModel = new ModelLocator();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
//				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				} else {
					SimiManager.getIntance().showNotify(Config.getInstance().getText(
									"No store match with your searching"));
				}
			}
		};
		mModel.setDelegate(delegate);
		mModel.addParam("limit", "5000");
		mModel.addParam("offset", "0");
		mModel.request();
	}
	
	public void addMarker(ArrayList<StoreObject> listStores) {
		mDelegate.addMarkerToMap(listStores);
	}

}

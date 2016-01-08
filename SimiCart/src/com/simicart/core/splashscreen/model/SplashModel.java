package com.simicart.core.splashscreen.model;

import org.json.JSONObject;

import android.content.Context;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.home.model.page.CMSModel;
import com.simicart.core.home.model.page.PluginModel;
import com.simicart.core.home.model.page.StoreViewModel;

public class SplashModel extends SimiModel {	
	private Context mContext;

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void paserData() {
		super.paserData();
		if (collection != null) {
			JSONObject jobs = collection.getCollection().get(0).getJSONObject();

			StoreViewModel storeViewM = new StoreViewModel();
			storeViewM.setData(jobs);

			CMSModel cmsM = new CMSModel();
			cmsM.setData(jobs);

			PluginModel pluginM = new PluginModel();
			pluginM.setContext(mContext);
			pluginM.setData(jobs);
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = "connector/config/get_splash_data";
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

}

package com.simicart.core.setting.controller;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.store.entity.Stores;

public class ListLanguageController extends SimiController {
	protected OnItemClickListener mClicker;
	ArrayList<String> list_lag;
	MyAddress addressBookDetail;
	ChooseCountryDelegate mDelegate;

	public OnItemClickListener getClicker() {
		return mClicker;
	}

	@Override
	public void onStart() {
		mClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
			}
		};
	}

	protected void selectItem(int position) {
		Collections.sort(list_lag);
		String language = list_lag.get(position).toString();
		String id = DataLocal.getStoreID();
		for (Stores store : DataLocal.listStores) {
			if (language.equals(store.getStoreName())) {
				if (!id.equals(store.getStoreID())) {

					if (Config.getInstance().getUse_store().equals("1")) {
						Stores oldStore = getCurrentStores(id);
						if (null != oldStore)
							changeBaseUrl(oldStore, store);
					}
					DataLocal.isLanguageRTL = false;
					DataLocal.listCms.clear();
					UtilsEvent.itemsList.clear();
					DataLocal.saveStoreID(store.getStoreID());
					SimiManager.getIntance().changeStoreView();
					SimiManager.getIntance().getRequestQueue().clearCacheL1();
					SimiManager.getIntance().getRequestQueueCloud()
							.clearCacheL1();
					SimiManager.getIntance().refreshSimiManager();
				}
			}
		}
	}

	protected void changeBaseUrl(Stores oldStore, Stores newStore) {
		String oldStoreCode = oldStore.getStoreCode();
		String newStoreCode = newStore.getStoreCode();

		Log.e("ListLanguageController ", "changeBaseUrl OLD STORE CODE "
				+ oldStoreCode + "NEW STORE CODE " + newStoreCode);

		String baseUrl = Config.getInstance().getBaseUrl();

		Log.e("ListLanguageController ", "changeBaseUrl BEFORE URL " + baseUrl);

		if (baseUrl.contains(oldStoreCode)) {
			baseUrl = baseUrl.replace(oldStoreCode, newStoreCode);
			Config.getInstance().setBase_url(baseUrl);
		}

		Log.e("ListLanguageController ", "changeBaseUrl AFTER URL "
				+ Config.getInstance().getBaseUrl());

	}

	protected Stores getCurrentStores(String id) {
		for (Stores store : DataLocal.listStores) {
			String _id = store.getStoreID();

			Log.e("ListLanguageController ", "changeBaseUrl ID " + id + " _ID "
					+ _id);

			if (id.equals(_id)) {
				return store;
			}
		}

		return null;
	}

	@Override
	public void onResume() {
	}

	public void setListLanguage(ArrayList<String> list_lag) {
		this.list_lag = list_lag;
	}
}

package com.simicart.core.setting.controller;

import java.util.ArrayList;
import java.util.Collections;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
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
					DataLocal.listCms.clear();
					UtilsEvent.itemsList.clear();
					DataLocal.saveStoreID(store.getStoreID());
					SimiManager.getIntance().changeStoreView();
				}
			}
		}
	}

	@Override
	public void onResume() {
	}

	public void setListLanguage(ArrayList<String> list_lag) {
		this.list_lag = list_lag;
	}
}

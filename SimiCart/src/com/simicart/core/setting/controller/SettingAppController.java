package com.simicart.core.setting.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.DataLocal;
import com.simicart.core.setting.delegate.SettingAppDelegate;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.setting.fragment.ListCurrencyFragment;
import com.simicart.core.setting.fragment.ListLanguageFragment;
import com.simicart.core.splashscreen.model.CurrencyModel;
import com.simicart.core.splashscreen.model.StoreModel;
import com.simicart.core.store.entity.Stores;

public class SettingAppController extends SimiController {

	// String currency;
	// String language;
	protected SettingAppDelegate mDelegate;
	private boolean isGetStore, isGetCurrency;
	protected OnClickListener on_click_notification;
	protected OnClickListener on_click_location;
	protected OnClickListener on_click_curency;
	protected OnClickListener on_click_language;

	@Override
	public void onStart() {
		mDelegate.showLoading();
		getStore();
		getCurrency();

		on_click_notification = new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDelegate.updateSettingNotification();
			}

		};

		on_click_location = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				SimiManager.getIntance().getCurrentActivity()
						.startActivity(viewIntent);
			}
		};

		on_click_curency = new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeCurrency();
			}
		};

		on_click_language = new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeLanguage();
			}
		};
	}

	public OnClickListener getOn_click_curency() {
		return on_click_curency;
	}

	public OnClickListener getOn_click_location() {
		return on_click_location;
	}

	public OnClickListener getOn_click_language() {
		return on_click_language;
	}

	public OnClickListener getOn_click_notification() {
		return on_click_notification;
	}

	private void getStore() {
		final StoreModel model = new StoreModel();
		DataLocal.listStores.clear();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				isGetStore = true;
				if (isSuccess) {
					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							Stores store = new Stores();
							store.setJSONObject(simiEntity.getJSONObject());
							DataLocal.listStores.add(store);
						}
					}
					afterStore();
				} else {
					Log.e("Splash Controller", "KHONG THANH CONG GETSTORE");
				}
			}
		});
		model.request();
	}

	private void getCurrency() {
		final CurrencyModel model = new CurrencyModel();
		DataLocal.listCurrency.clear();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				isGetCurrency = true;
				if (isSuccess) {
					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							CurrencyEntity currency = new CurrencyEntity();
							currency.setJSONObject(simiEntity.getJSONObject());
							DataLocal.listCurrency.add(currency);
						}

					}
					afterCurrency();
				}
			}
		});
		model.request();
	}

	protected void changeCurrency() {
		ListCurrencyFragment fragment = ListCurrencyFragment
				.newInstance(mDelegate.getCurrency());
		if (DataLocal.isTablet) {
			SimiManager.getIntance().replacePopupFragment(fragment);
		} else {
			SimiManager.getIntance().replaceFragment(fragment);
		}
	}

	protected void changeLanguage() {
		ListLanguageFragment fragment = ListLanguageFragment
				.newInstance(mDelegate.getLanguage());
		if (DataLocal.isTablet) {
			SimiManager.getIntance().replacePopupFragment(fragment);
		} else {
			SimiManager.getIntance().replaceFragment(fragment);
		}
	}

	void afterCurrency() {
		mDelegate.setCurrency();
		if (isGetStore == true && isGetCurrency == true) {
			mDelegate.successData();
		}
	}

	void afterStore() {
		mDelegate.setLanguage();
		if (isGetStore == true && isGetCurrency == true) {
			mDelegate.successData();
		}
	}

	@Override
	public void onResume() {
		mDelegate.setCurrency();
		mDelegate.setLanguage();
		mDelegate.successData();
	}

	public void setDelegate(SettingAppDelegate settingAppDelegate) {
		this.mDelegate = settingAppDelegate;
	}

}

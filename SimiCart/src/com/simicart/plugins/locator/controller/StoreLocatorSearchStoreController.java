package com.simicart.plugins.locator.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.plugins.locator.delegate.StoreLocatorSearchStoreDelegate;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;
import com.simicart.plugins.locator.model.GetSearchConfigModel;
import com.simicart.plugins.locator.model.GetSearchCountryModel;
import com.simicart.plugins.locator.model.GetTagSearchModel;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class StoreLocatorSearchStoreController extends SimiController {
	
	protected OnClickListener onSearchClick;
	protected OnClickListener onClearSearchClick;
	protected OnItemClickListener onSearchByTag;
	protected StoreLocatorSearchStoreDelegate mDelegate;
	protected GetSearchConfigModel configModel;
	protected GetSearchCountryModel countryModel;
	protected GetTagSearchModel tagModel;
	
	public void setDelegate(StoreLocatorSearchStoreDelegate delegate) {
		mDelegate = delegate;
	}
	
	public OnClickListener getOnSearchClick() {
		return onSearchClick;
	}
	
	public OnClickListener getOnClearSearchClick() {
		return onClearSearchClick;
	}
	
	public OnItemClickListener getOnSearchBytag() {
		return onSearchByTag;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		requestGetConfig();
		
		onSearchClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchObject search = mDelegate.getSearchObject();
				if(search != null) {
					if(DataLocal.isTablet) {
						StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
						fragment.setSearchObject(search);
						SimiManager.getIntance().replaceFragment(fragment);
					} else {
						StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
						fragment.setSearchObject(search);
						SimiManager.getIntance().replaceFragment(fragment);
					}
					SimiManager.getIntance().removeDialog();
					Utils.hideKeyboard(v);
				}
			}
		};
		
		onClearSearchClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(DataLocal.isTablet) {
					StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				} else {
					StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
					SimiManager.getIntance().replaceFragment(fragment);
				}
				SimiManager.getIntance().removeDialog();
				Utils.hideKeyboard(v);
			}
		};
		
		onSearchByTag = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SearchObject search = new SearchObject();
				search.setTag(position);
				if(DataLocal.isTablet) {
					StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
					fragment.setSearchObject(search);
					SimiManager.getIntance().replaceFragment(fragment);
				} else {
					StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
					fragment.setSearchObject(search);
					SimiManager.getIntance().replaceFragment(fragment);
				}
				SimiManager.getIntance().removeDialog();
			}
		};
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mDelegate.setListConfig(configModel.getConfigs());
		mDelegate.setListCountry(countryModel.getCountries());
		//mDelegate.setListTag(tagModel.getTags());
		mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetConfig() {
		mDelegate.showLoading();
		configModel = new GetSearchConfigModel();
		configModel.setDelegate(new ModelDelegate() {
			
			@Override
			public void callBack(String message, boolean isSuccess) {
				// TODO Auto-generated method stub
				if(isSuccess) {
					mDelegate.setListConfig(configModel.getConfigs());
					requestGetCountry();
				}
			}
		});
		configModel.request();
	}
	
	protected void requestGetCountry() {
		countryModel = new GetSearchCountryModel();
		countryModel.setDelegate(new ModelDelegate() {
			
			@Override
			public void callBack(String message, boolean isSuccess) {
				// TODO Auto-generated method stub
				mDelegate.dismissLoading();
				if(isSuccess) {
					mDelegate.setListCountry(countryModel.getCountries());
					requestGetTagSearch();
				}
			}
		});
		countryModel.request();
	}
	
	protected void requestGetTagSearch() {
		tagModel = new GetTagSearchModel();
		tagModel.setDelegate(new ModelDelegate() {
			
			@Override
			public void callBack(String message, boolean isSuccess) {
				// TODO Auto-generated method stub
				mDelegate.dismissLoading();
				if(isSuccess) {
					mDelegate.setListTag(tagModel.getTags());
					mDelegate.updateView(null);
				}
			}
		});
		tagModel.request();
	}

}

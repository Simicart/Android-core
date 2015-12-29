package com.simicart.core.customer.controller;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.entity.MyAddress;

public class CountryAllowedAController extends SimiController {
	protected OnItemClickListener mClicker;
	ArrayList<String> list_country;
	MyAddress addressBookDetail;
	ChooseCountryDelegate chooseDelegate;
	protected int type;

	public OnItemClickListener getClicker() {
		return mClicker;
	}

	public void setChooseDelegate(ChooseCountryDelegate chooseDelegate) {
		this.chooseDelegate = chooseDelegate;
	}

	public void setType(int type) {
		this.type = type;
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
		Collections.sort(list_country);
		String country = list_country.get(position).toString();
		Log.e("CountryAllowedAController selectItem", country);
		chooseDelegate.chooseCountry(type, country);
		SimiManager.getIntance().backPreviousFragment();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	}

	public void setList_country(ArrayList<String> list_country) {
		this.list_country = list_country;
	}
}

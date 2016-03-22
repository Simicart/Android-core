package com.simicart.core.customer.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.CountryAllowedBlock;
import com.simicart.core.customer.controller.CountryAllowedAController;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;

public class CountryFragment extends SimiFragment {
	protected boolean isCheckout = false;
	CountryAllowedBlock mBlock;
	CountryAllowedAController mController;
	ArrayList<String> mListCountry;
	ChooseCountryDelegate chooseDelegate;
	protected int type;

	public static CountryFragment newInstance(int type, ArrayList<String> list_country) {
		CountryFragment fragment = new CountryFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.TYPE, type, Constants.KeyData.TYPE_INT, bundle);
		setData(Constants.KeyData.LIST_COUNTRY, list_country, Constants.KeyData.TYPE_LIST_STRING, bundle);
		fragment.setArguments(bundle);
		return fragment;
	}

	public void setChooseDelegate(ChooseCountryDelegate chooseDelegate) {
		this.chooseDelegate = chooseDelegate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_choose_country"), container,
				false);
		Context context = getActivity();
		
		//getdata
		if(getArguments() != null){
		type = (int) getData(Constants.KeyData.TYPE, Constants.KeyData.TYPE_INT, getArguments());
		mListCountry = (ArrayList<String>) getData(Constants.KeyData.LIST_COUNTRY, Constants.KeyData.TYPE_LIST_STRING, getArguments());
		}
		
		mBlock = new CountryAllowedBlock(view, context);
		mBlock.setListContry(mListCountry);
		mController = new CountryAllowedAController();
		mController.onStart();
		mController.setList_country(mListCountry);
		mController.setType(type);
		mController.setChooseDelegate(chooseDelegate);
		mBlock.initView();
		mBlock.setOnItemClicker(mController.getClicker());
		return view;
	}
}

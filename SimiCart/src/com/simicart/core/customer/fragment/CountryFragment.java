package com.simicart.core.customer.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
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

	public static CountryFragment newInstance() {
		CountryFragment fragment = new CountryFragment();
		return fragment;
	}

	public void setFragmentType(boolean value) {
		isCheckout = value;
	}

	public void setList_country(ArrayList<String> list_country) {
		this.mListCountry = list_country;
	}

	public void setChooseDelegate(ChooseCountryDelegate chooseDelegate) {
		this.chooseDelegate = chooseDelegate;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_choose_country"), container,
				false);
		Context context = getActivity();
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

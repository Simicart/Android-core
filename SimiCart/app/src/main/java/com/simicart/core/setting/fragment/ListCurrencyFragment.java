package com.simicart.core.setting.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.setting.block.ListViewIndexableBlock;
import com.simicart.core.setting.controller.ListCurrencyController;
import com.simicart.core.setting.entity.CurrencyEntity;

public class ListCurrencyFragment extends SimiFragment {
	protected ListViewIndexableBlock mBlock;
	protected ListCurrencyController mController;
	protected ArrayList<String> mList;
	protected String current_item;

	public String getCurrent_item() {
		return current_item;
	}

	public static ListCurrencyFragment newInstance(String currentItem) {
		ListCurrencyFragment fragment = new ListCurrencyFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.CURRENT_ITEM, currentItem, Constants.KeyData.TYPE_STRING, bundle);
		fragment.setArguments(bundle);
		fragment.setListLanguage(DataLocal.listCurrency);
		return fragment;
	}

	public void setListLanguage(ArrayList<CurrencyEntity> listCurrency) {
		ArrayList<String> _list = new ArrayList<>();
		for (CurrencyEntity currency : listCurrency) {
			String title = currency.getTitle();
			Log.e("ListCurrencyFragment ", "setListLanguage " + title);
			_list.add(title);
		}
		this.mList = _list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_choose_country"), container,
				false);
		Context context = getActivity();
		if(getArguments() != null){
		current_item = (String) getData(Constants.KeyData.CURRENT_ITEM, Constants.KeyData.TYPE_STRING, getArguments());
		}

		mBlock = new ListViewIndexableBlock(view, context);
		mBlock.setList(mList);
		mBlock.setItemChecked(current_item);
		mController = new ListCurrencyController();
		mController.setDelegate(mBlock);
		mController.onStart();
		mController.setListCurrency(mList);
		mBlock.initView();
		mBlock.setOnItemClicker(mController.getClicker());

		return view;
	}
}

package com.simicart.core.setting.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.setting.block.ListViewIndexableBlock;
import com.simicart.core.setting.controller.ListLanguageController;
import com.simicart.core.store.entity.Stores;

public class ListLanguageFragment extends SimiFragment {
	protected ListViewIndexableBlock mBlock;
	protected ListLanguageController mController;
	protected ArrayList<String> mList;
	protected String current_item;

	public String getCurrent_item() {
		return current_item;
	}

	public void setCurrent_item(String current_item) {
		this.current_item = current_item;
	}

	public static ListLanguageFragment newInstance() {
		ListLanguageFragment fragment = new ListLanguageFragment();
		fragment.setListLanguage(DataLocal.listStores);
		return fragment;
	}

	public void setListLanguage(ArrayList<Stores> listStores) {
		ArrayList<String> _list = new ArrayList<>();
		for (Stores stores : listStores) {
			_list.add(stores.getStoreName());
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

		mBlock = new ListViewIndexableBlock(view, context);
		mBlock.setList(mList);
		mBlock.setItemChecked(current_item);
		mController = new ListLanguageController();
		mController.onStart();
		mController.setListLanguage(mList);
		mBlock.initView();
		mBlock.setOnItemClicker(mController.getClicker());

		return view;
	}
}

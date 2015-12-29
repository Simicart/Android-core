package com.simicart.core.catalog.category.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.category.block.SortBlock;
import com.simicart.core.catalog.category.controller.SortController;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class SortFragment extends SimiFragment {

	protected String mCategoryID;
	protected String mCategoryName;
	protected String mSortType = "";
	protected String mQuery = "";
	protected SortController mController;
	protected SortBlock mBlock;
	ArrayList<Sort> listSort;
	protected JSONObject jsonFilter;
	private String url_search ="";
	private String KEY = "";
	
	public void setUrl_search(String url_search) {
		this.url_search = url_search;
	}
	public void setKey(String key) {
		this.KEY = key;
	}

	protected String sort_tag;

	public static SortFragment newInstance(String name, String id) {
		SortFragment fragment = new SortFragment(name, id);
		return fragment;
	}
	

	public void setSort_tag(String sort_tag) {
		this.sort_tag = sort_tag;
	}

	public void setJSONFilter(JSONObject json) {
		jsonFilter = json;
	}

	public SortFragment(String name, String id) {
		mCategoryID = id;
		mCategoryName = name;
	}

	public void setQuery(String mQuery) {
		this.mQuery = mQuery;
	}

	public void setSortType(String sortType) {
		this.mSortType = sortType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_sort_layout"), container,
				false);
		Context context = getActivity();

		listSort = new ArrayList<Sort>();
		String key[] = { "None", "Price: Low to High", "Price: High to Low",
				"Name: A to Z", "Name: Z to A" };
		for (int i = 0; i < key.length; i++) {
			Sort item = new Sort();
			item.setId(i);
			item.setTitle(Config.getInstance().getText(key[i]));
			listSort.add(item);
		}
		mBlock = new SortBlock(view, context);
		mBlock.setSort_tag(sort_tag);
		mBlock.initView();

		if (null == mController) {
			mController = new SortController();
			mController.setCategoryID(mCategoryID);
			mController.setCategoryName(mCategoryName);
			mController.setSortType(mSortType);
			mController.setTag_sort(sort_tag);
			mController.setJSONFilter(jsonFilter);
			mController.setQuery(mQuery);
			mController.setKey(KEY);
			mController.setUrl_search(url_search);
			mController.setListSort(listSort);
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setSortOptionClick(mController.getItemClicker());

		return view;
	}

}

package com.simicart.core.catalog.category.fragment;

import java.util.ArrayList;

import org.json.JSONException;
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
import com.simicart.core.config.Constants;
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
	protected String sort_tag;

	public static SortFragment newInstance(String url, String id, String name, String sortTag, JSONObject json, String key, String query, String sortType) {
			SortFragment fragment = new SortFragment();
			Bundle bundle= new Bundle();
			setData(Constants.KeyData.URL, url, Constants.KeyData.TYPE_STRING, bundle);
			setData(Constants.KeyData.ID, id, Constants.KeyData.TYPE_STRING, bundle);
			setData(Constants.KeyData.NAME, name, Constants.KeyData.TYPE_STRING, bundle);
			setData(Constants.KeyData.SORT_TAG, sortTag, Constants.KeyData.TYPE_STRING, bundle);
			if(json != null){
			setData(Constants.KeyData.JSON_FILTER, json.toString(), Constants.KeyData.TYPE_JSONOBJECT, bundle);
			}
			setData(Constants.KeyData.KEY, key, Constants.KeyData.TYPE_STRING, bundle);
			setData(Constants.KeyData.QUERY, query, Constants.KeyData.TYPE_STRING, bundle);
			setData(Constants.KeyData.SORT_TYPE, sortType, Constants.KeyData.TYPE_STRING, bundle);
		    fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_sort_layout"), container,
				false);
		Context context = getActivity();
		if(getArguments() != null){
		mCategoryID = (String) getData(Constants.KeyData.ID, Constants.KeyData.TYPE_STRING, getArguments());
		mCategoryName = (String) getData(Constants.KeyData.NAME, Constants.KeyData.TYPE_STRING, getArguments());
		url_search = (String) getData(Constants.KeyData.URL, Constants.KeyData.TYPE_STRING, getArguments());
		KEY = (String) getData(Constants.KeyData.KEY, Constants.KeyData.TYPE_STRING, getArguments());
		sort_tag = (String) getData(Constants.KeyData.SORT_TAG, Constants.KeyData.TYPE_STRING, getArguments());
		String json = (String) getData(Constants.KeyData.JSON_FILTER, Constants.KeyData.TYPE_JSONOBJECT, getArguments());
		try {
			jsonFilter = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mQuery = (String) getData(Constants.KeyData.QUERY, Constants.KeyData.TYPE_STRING, getArguments());
		mSortType = (String) getData(Constants.KeyData.SORT_TYPE, Constants.KeyData.TYPE_STRING, getArguments());
		}
		
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

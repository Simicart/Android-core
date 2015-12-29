package com.simicart.core.catalog.category.controller;

import java.util.ArrayList;

import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.SortDelegate;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.config.DataLocal;

public class SortController extends SimiController {

	ArrayList<Sort> mListSort;
	protected String mCategoryID;
	protected String mCategoryName;
	protected String mSortType;
	protected String mQuery;
	protected SortDelegate mDelegate;
	protected OnItemClickListener mItemClicker;
	protected OnItemClickListener mCateItemClicker;
	protected JSONObject jsonFilter;
	protected String tag_sort;
	private String url_search = "";
	private String key =  "";

	public void setKey(String key) {
		this.key = key;
	}
	public void setUrl_search(String url_search) {
		this.url_search = url_search;
	}

	public void setJSONFilter(JSONObject json) {
		jsonFilter = json;
	}

	public void setTag_sort(String tag_sort) {
		this.tag_sort = tag_sort;
	}

	public void setDelegate(SortDelegate delegate) {
		this.mDelegate = delegate;
	}

	@Override
	public void onStart() {
		mDelegate.setSort_option(mSortType);
		mDelegate.setListSort(mListSort);

		mItemClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("Test Sort");
				ListProductFragment fragment = ListProductFragment
						.newInstance();
				fragment.setUrlSearch(url_search);
				fragment.setCategoryId(mCategoryID);
				fragment.setQuerySearch(mQuery);
				fragment.setJsonFilter(jsonFilter);
				fragment.setTag_search(tag_sort);
				fragment.setKey(key);
				fragment.setmSortID(mListSort.get(position).getId() +"");
//				fragment.setSortOption("" + mListSort.get(position).getId());
				SimiManager.getIntance().replaceFragment(fragment);
				mDelegate.setSort_option(mListSort.get(position).getTitle());
				if (DataLocal.isTablet) {
					SimiManager.getIntance().removeDialog();
				}
			}
		};
	}

	@Override
	public void onResume() {
	}

	public OnItemClickListener getItemClicker() {
		return mItemClicker;
	}

	public void setCategoryID(String mCategoryID) {
		this.mCategoryID = mCategoryID;
	}

	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public void setSortType(String mSortType) {
		this.mSortType = mSortType;
	}

	public void setQuery(String mQuery) {
		this.mQuery = mQuery;
	}

	public void setListSort(ArrayList<Sort> listSort) {
		this.mListSort = listSort;
	}
}

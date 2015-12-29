package com.simicart.core.catalog.search.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.search.block.SearchBlock;
import com.simicart.core.catalog.search.controller.SearchController;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;

public class ListProductFragment extends SimiFragment {
	protected View rootView;
	protected String mQuery;
	protected String tag_search;
	protected SearchHomeBlock mSearchHomeBlock;
	protected SearchBlock mSearchBlock;
	protected SearchController mSearchController;
	protected String mCatID = "-1";
	protected String mCatName = "";
	protected String mSortID = "None";
	protected JSONObject jsonFilter;
	FilterEvent filterEvent = null;
	private Map<String, String> list_param = new HashMap<String, String>();

	public static ListProductFragment newInstance() {
		ListProductFragment fragment = new ListProductFragment();
		fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_LISTPRODUCT);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_list_search_layout"),
				container, false);
		Context context = getActivity();
		if (tag_search == null) {
			if (DataLocal.isTablet) {
				tag_search = TagSearch.TAG_GRIDVIEW;
			} else {
				if (Config.getInstance().getDefaultList().equals("1")) {
					// neu gia tri 1: Gridview
					this.tag_search = TagSearch.TAG_GRIDVIEW;
				} else {
					this.tag_search = TagSearch.TAG_LISTVIEW;
				}
			}
			setScreenName("List Category ID:" + mCatID);
		}
		setScreenName("Search Screen");
		mSearchBlock = new SearchBlock(rootView, context);
		mSearchBlock.setTag_search(tag_search);
		mSearchBlock.setmQuery(mQuery);
		mSearchBlock.setCateName(mCatName);
		mSearchBlock.setCate_id(mCatID);
		mSearchBlock.initView();
		if (mSearchController == null) {
			mSearchController = new SearchController(mCatName, mCatID);
			filterEvent = new FilterEvent(mSearchController);
			mSearchBlock.setFilterEvent(filterEvent);
			mSearchController.setTag_search(tag_search);
			mSearchBlock.setmQuery(mQuery);
			mSearchController.setQuery(mQuery);
			mSearchBlock.setCateName(mCatName);
			mSearchController.setmSortType(mSortID);
			mSearchController.setDelegate(mSearchBlock);
			mSearchController.setJsonFilter(jsonFilter);
			mSearchController.setList_Param(list_param);
			mSearchController.onStart();
		} else {
			// if (!DataLocal.isTablet) {
			// mSearchController.setDelegate(mSearchBlock);
			// filterEvent = new FilterEvent(mSearchController);
			// if (type_search.equals(TagSearch.TYPE_SEARCH_QUERY)) {
			// mSearchBlock.setmQuery(mQuery);
			// }
			// mSearchBlock.setFilterEvent(filterEvent);
			// mSearchController.onResume();
			// }
		}
		if (!mCatID.equals("-1")) {
			mSearchBlock.setCateName(mCatName);
		}
		mSearchBlock.setScrollListView(mSearchController
				.getScrollListviewListener());
		mSearchBlock.setScrollGridView(mSearchController
				.getmScrollGridviewListener());
		mSearchBlock.setOnTourchChangeView(mSearchController
				.getmOnTouchChangeViewData());
		mSearchBlock.setOnTourchToFilter(mSearchController
				.getmOnTouchToFilter());
		mSearchBlock.setOnTourchToSort(mSearchController.getmOnTouchToSort());
		mSearchBlock.setOnTouchListenerGridview(mSearchController
				.getmOnTouchGridview());
		mSearchBlock.setOnItemListviewClick(mSearchController
				.getmListviewClick());
		// tablet
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mSearchController != null) {
			mSearchController.setDelegate(mSearchBlock);
			filterEvent = new FilterEvent(mSearchController);
			mSearchBlock.setmQuery(mQuery);
			mSearchBlock.setFilterEvent(filterEvent);
			mSearchController.onResume();
		}
	}

	public void setTag_search(String tag_search) {
		if (DataLocal.isTablet) {
			this.tag_search = TagSearch.TAG_GRIDVIEW;
		} else {
			if (Config.getInstance().getDefaultList().equals("1")) {
				// neu gia tri 1: Gridview
				this.tag_search = TagSearch.TAG_GRIDVIEW;
			} else {
				this.tag_search = TagSearch.TAG_LISTVIEW;
			}
			// this.tag_search = tag_search;
		}
	}

	public void setmSortID(String mSortID) {
		this.mSortID = mSortID;
	}

	public void setCatName(String mCatName) {
		this.mCatName = mCatName;
	}

	public void setJsonFilter(JSONObject jsonFilter) {
		this.jsonFilter = jsonFilter;
	}

	public void setmCatID(String mCatID) {
		this.mCatID = mCatID;
	}

	public void setQuery(String mQuery) {
		this.mQuery = mQuery;
	}

	// set param
	public void setListParam(String key, String value) {
		list_param.put(key, value);
	}

	public void setCategoryId(String categoryId) {
		setmCatID(categoryId);
		setListParam(ConstantsSearch.PARAM_CATEGORY_ID, categoryId);
	}

	public void setCategoryName(String categoryName) {
		setCatName(categoryName);
		setListParam(ConstantsSearch.PARAM_CATEGORY_NAME, categoryName);
	}

	public void setUrlSearch(String url) {
		setListParam(ConstantsSearch.PARAM_URL, url);
	}

	public void setQuerySearch(String query) {
		setQuery(query);
		setListParam(ConstantsSearch.PARAM_QUERY, query);
	}

	public void setKey(String key) {
		setListParam(ConstantsSearch.PARAM_KEY, key);
	}

	public void setOffset(String offset) {
		setListParam(ConstantsSearch.PARAM_OFFSET, offset);
	}

	public void setLimit(String limit) {
		setListParam(ConstantsSearch.PARAM_LIMIT, limit);
	}

	public void setSortOption(String sortOption) {
		setmSortID(sortOption);
		setListParam(ConstantsSearch.PARAM_SORT_OPTION, sortOption);
	}
}

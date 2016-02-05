package com.simicart.core.catalog.search.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;

public class ListProductFragment extends SimiFragment {
	protected View rootView;
	protected String mQuery;
	protected String tag_search = "";
	protected String url_search;
	protected String mKey;
	protected SearchHomeBlock mSearchHomeBlock;
	protected SearchBlock mSearchBlock;
	protected SearchController mSearchController;
	protected String mCatID = "-1";
	protected String mCatName = "";
	protected String mSortID = "None";
	protected JSONObject jsonFilter;
	FilterEvent filterEvent = null;
	private Map<String, String> list_param = new HashMap<String, String>();

	public static ListProductFragment newInstance(String url, String id,
			String tag, String key, String name, String query, String sortId,
			JSONObject jsonFilter) {
		ListProductFragment fragment = new ListProductFragment();
		fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_LISTPRODUCT);
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.URL, url, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.ID, id, Constants.KeyData.TYPE_STRING, bundle);
		setData(Constants.KeyData.TAG, tag, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.KEY, key, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.NAME, name, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.QUERY, query, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.SORT_ID, sortId,
				Constants.KeyData.TYPE_STRING, bundle);
		if (jsonFilter != null) {
			setData(Constants.KeyData.JSON_FILTER, jsonFilter.toString(),
					Constants.KeyData.TYPE_JSONOBJECT, bundle);
		}
		fragment.setArguments(bundle);
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

		// data
		if (getArguments() != null) {
			mSortID = (String) getData(Constants.KeyData.SORT_ID,
					Constants.KeyData.TYPE_STRING, getArguments());
			mCatName = (String) getData(Constants.KeyData.NAME,
					Constants.KeyData.TYPE_STRING, getArguments());
			mCatID = (String) getData(Constants.KeyData.ID,
					Constants.KeyData.TYPE_STRING, getArguments());
			String json = (String) getData(Constants.KeyData.JSON_FILTER,
					Constants.KeyData.TYPE_JSONOBJECT, getArguments());
			try {
				jsonFilter = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mQuery = (String) getData(Constants.KeyData.QUERY,
					Constants.KeyData.TYPE_STRING, getArguments());
			url_search = (String) getData(Constants.KeyData.URL,
					Constants.KeyData.TYPE_STRING, getArguments());
			mKey = (String) getData(Constants.KeyData.KEY,
					Constants.KeyData.TYPE_STRING, getArguments());
			tag_search = (String) getData(Constants.KeyData.TAG,
					Constants.KeyData.TYPE_STRING, getArguments());
			setTag_search(tag_search);
		}

		Log.d("quangdd1", "=murl_search :" + url_search + "=mSortID :"
				+ mSortID + "=mCatName :" + mCatName + "=mCatID :" + mCatID
				+ "=mQuery :" + mQuery + "=mKey :" + mKey + "=tag_search :"
				+ tag_search);
		setListParam(ConstantsSearch.PARAM_CATEGORY_ID, mCatID);
		setListParam(ConstantsSearch.PARAM_CATEGORY_NAME, mCatName);
		setListParam(ConstantsSearch.PARAM_URL, url_search);
		setListParam(ConstantsSearch.PARAM_QUERY, mQuery);
		setListParam(ConstantsSearch.PARAM_KEY, mKey);
		setListParam(ConstantsSearch.PARAM_SORT_OPTION, mSortID);

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
			if (!DataLocal.isTablet) {
				mSearchBlock.getEdittextSearch().setText("");
			}
			mSearchBlock.setFilterEvent(filterEvent);
			mSearchController.onResume();
		}
	}

	public void setTag_search(String tag_search) {
		if (DataLocal.isTablet) {
			this.tag_search = TagSearch.TAG_GRIDVIEW;
		} else {
			if (Config.getInstance().getDefaultList() != null && Config.getInstance().getDefaultList().equals("1")) {
				// neu gia tri 1: Gridview
				this.tag_search = TagSearch.TAG_GRIDVIEW;
			} else {
				this.tag_search = TagSearch.TAG_LISTVIEW;
			}
			// this.tag_search = tag_search;
		}
	}

	// set param
	public void setListParam(String key, String value) {
		list_param.put(key, value);
	}
}

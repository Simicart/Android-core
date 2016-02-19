package com.simicart.core.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.banner.block.BannerBlock;
import com.simicart.core.banner.controller.BannerController;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.CategoryHomeBlock;
import com.simicart.core.home.block.ProductListBlock;
import com.simicart.core.home.block.SearchHomeBlock;
import com.simicart.core.home.controller.CategoryHomeController;
import com.simicart.core.home.controller.ProductListController;
import com.simicart.core.home.model.HomeModel;

public class HomeFragment extends SimiFragment {

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	protected View rootView;
	protected SearchHomeBlock mSearchHomeBlock;

	protected BannerBlock mBannerBlock;
	protected BannerController mBannerController;

	protected CategoryHomeBlock mCategoryHomeBlock;
	protected CategoryHomeController mCategoryHomeController;

	protected ProductListBlock mProductListBlock;
	protected ProductListController mProductListController;
//	protected static boolean isCheckSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setScreenName("Home Screen");
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_home_layout"), container,
				false);
		Context context = getActivity();
		// init search
		View searchView = rootView.findViewById(Rconfig.getInstance().id(
				"rlt_search"));
		if (DataLocal.isTablet) {
			searchView.setVisibility(View.GONE);
		} else {
			mSearchHomeBlock = new SearchHomeBlock(searchView, context);
			mSearchHomeBlock.setTag(TagSearch.TAG_LISTVIEW);
			mSearchHomeBlock.initView();
		}

		// Max add code
		Intent i = getActivity().getIntent();
		String home_check = i.getStringExtra("home_check");
		boolean enable_request = false;
		if (home_check != null && home_check.equals("1")) {
			enable_request = true;
		}
		// end code Max

		// init banner
		RelativeLayout rlt_banner = (RelativeLayout) rootView
				.findViewById(Rconfig.getInstance().id("rlt_banner_home"));
		mBannerBlock = new BannerBlock(rlt_banner, context);
		mBannerBlock.initView();
		if (!enable_request) {
			if (null == mBannerController) {
				mBannerController = new BannerController(mBannerBlock);
				mBannerController.onStart();
			} else {
				mBannerController.setDelegate(mBannerBlock);
				mBannerController.onResume();
			}
		}

		// init category
		LinearLayout ll_category = (LinearLayout) rootView.findViewById(Rconfig
				.getInstance().id("ll_category"));
		mCategoryHomeBlock = new CategoryHomeBlock(ll_category, context);
		mCategoryHomeBlock.initView();
		if (!enable_request) {
			if (mCategoryHomeController == null) {
				mCategoryHomeController = new CategoryHomeController();
				mCategoryHomeController.setDelegate(mCategoryHomeBlock);
				mCategoryHomeController.onStart();
			} else {
				mCategoryHomeController.setDelegate(mCategoryHomeBlock);
				mCategoryHomeController.onResume();
			}
		}

		// init spotproduct
		LinearLayout ll_spotproduct = (LinearLayout) rootView
				.findViewById(Rconfig.getInstance().id("ll_spotproduct"));
		mProductListBlock = new ProductListBlock(ll_spotproduct, context);
		mProductListBlock.initView();
		if (!enable_request) {
			if (null == mProductListController) {
				mProductListController = new ProductListController();
				mProductListController.setDelegate(mProductListBlock);
				mProductListController.onStart();
			} else {
				mProductListController.setDelegate(mProductListBlock);
				mProductListController.onResume();
			}
		}

		// Max add 8/1/15
		if (enable_request) {
			final HomeModel homM = new HomeModel();
			final SimiBlock homeBlock = new SimiBlock(rootView, context);
			homeBlock.showLoading();
			homM.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					if (isSuccess) {
						homeBlock.dismissLoading();
						mBannerBlock.drawView(homM.getBannerData());

						mCategoryHomeBlock.drawView(homM.getHomeCateData());

						mProductListBlock.onUpdate(homM.getHomeSpotData());
					}
				}
			});
			homM.addParam("limit", "10");
			homM.addParam("width", "200");
			homM.addParam("height", "200");
			homM.request();
		}
		// end Max
		return rootView;
	}
}

package com.simicart.theme.matrixtheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.banner.block.BannerBlock;
import com.simicart.core.banner.controller.BannerController;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;
import com.simicart.theme.matrixtheme.home.block.BannerTheme1Block;
import com.simicart.theme.matrixtheme.home.block.CategoryHomeTheme1Block;
import com.simicart.theme.matrixtheme.home.block.SpotProductHomeTheme1Block;
import com.simicart.theme.matrixtheme.home.controller.BannerTheme1Controller;
import com.simicart.theme.matrixtheme.home.controller.CategoryHomeTheme1Controller;
import com.simicart.theme.matrixtheme.home.controller.SpotProductHomeTheme1Controller;

public class HomeTheme1Fragment extends SimiFragment {
	protected BannerBlock mBannerBlock;
	protected BannerController mBannerController;

	protected BannerTheme1Block mBannerTheme1Block;
	protected BannerTheme1Controller mBannerTheme1Controller;

	protected CategoryHomeTheme1Controller mCategoryHomeTheme1Controller;
	protected CategoryHomeTheme1Block mCategoryHomeTheme1Block;

	protected SpotProductHomeTheme1Controller mSpotProductHomeTheme1Controller;
	protected SpotProductHomeTheme1Block mSpotProductHomeTheme1Block;
	protected SearchHomeBlock mSearchHomeBlock;

	public static HomeTheme1Fragment newInstance() {
		HomeTheme1Fragment fragment = new HomeTheme1Fragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenName("Home Screen");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("theme1_home_layout"), null);

		Context context = getActivity();

		if (DataLocal.isTablet) {
			// initial banner
			View bannerView = rootView.findViewById(Rconfig.getInstance().id(
					"core_home_banner_relatvie"));
			mBannerTheme1Block = new BannerTheme1Block(bannerView, context);
			mBannerTheme1Block.setFragmentManager(getChildFragmentManager());
			mBannerTheme1Block.initView();
			if (null == mBannerController) {
				mBannerController = new BannerController(mBannerTheme1Block);
				mBannerController.onStart();
			} else {
				mBannerController.setDelegate(mBannerTheme1Block);
				mBannerController.onResume();
			}
		} else {
			// initial search
			View searchView = rootView.findViewById(Rconfig.getInstance().id(
					"rlt_search"));
			mSearchHomeBlock = new SearchHomeBlock(searchView, context);
			mSearchHomeBlock.setTag(TagSearch.TAG_LISTVIEW);
			mSearchHomeBlock.initView();

			// initial banner
			View bannerView = rootView.findViewById(Rconfig.getInstance().id(
					"core_home_banner_relatvie"));
			mBannerBlock = new BannerBlock(bannerView, context);
			mBannerBlock.initView();
			if (null == mBannerController) {
				mBannerController = new BannerController(mBannerBlock);
				mBannerController.onStart();
			} else {
				mBannerController.setDelegate(mBannerBlock);
				mBannerController.onResume();
			}
		}

		// category
		View categoryView = rootView.findViewById(Rconfig.getInstance().id(
				"ll_spotcategory"));
		mCategoryHomeTheme1Block = new CategoryHomeTheme1Block(categoryView,
				context);
		mCategoryHomeTheme1Block.initView();
		if (null == mCategoryHomeTheme1Controller) {
			mCategoryHomeTheme1Controller = new CategoryHomeTheme1Controller();
			mCategoryHomeTheme1Controller.setDelegate(mCategoryHomeTheme1Block);
			mCategoryHomeTheme1Controller.onStart();
		} else {
			mCategoryHomeTheme1Controller.setDelegate(mCategoryHomeTheme1Block);
			mCategoryHomeTheme1Controller.onResume();
		}

		// Spot Product
		View spotView = rootView.findViewById(Rconfig.getInstance().id(
				"ll_spot_product"));
		mSpotProductHomeTheme1Block = new SpotProductHomeTheme1Block(spotView,
				context);
		mSpotProductHomeTheme1Block.initView();
		if (null == mSpotProductHomeTheme1Controller) {
			mSpotProductHomeTheme1Controller = new SpotProductHomeTheme1Controller();
			mSpotProductHomeTheme1Controller
					.setDelegate(mSpotProductHomeTheme1Block);
			mSpotProductHomeTheme1Controller.onStart();
		} else {
			mSpotProductHomeTheme1Controller
					.setDelegate(mSpotProductHomeTheme1Block);
			mSpotProductHomeTheme1Controller.onResume();
		}

		return rootView;
	}
}

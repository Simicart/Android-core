package com.simicart.core.catalog.category.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.category.block.CategoryBlock;
import com.simicart.core.catalog.category.block.CategoryDetailBlock;
import com.simicart.core.catalog.category.controller.CategoryController;
import com.simicart.core.catalog.category.controller.CategoryDetailController;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class CategoryFragment extends SimiFragment {

	protected String mCategoryID;
	protected String mCategoryName;
	protected CategoryController mCategoryController;
	protected CategoryBlock mCategoryBlock;
	protected CategoryDetailController mCategoryDetailController;
	protected CategoryDetailBlock mCategoryDetailBlock;

	public static CategoryFragment newInstance(String name, String id) {
		CategoryFragment fragment = new CategoryFragment(name, id);
		return fragment;
	}

	public CategoryFragment() {
	}

	public CategoryFragment(String name, String id) {
		mCategoryID = id;
		mCategoryName = name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("Category Screen");
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_category_layout"),
				container, false);
		Context context = getActivity();

		// request ListProduct
		// LinearLayout ll_categoryParent = (LinearLayout) view
		// .findViewById(Rconfig.getInstance().id("ll_categoryParent"));
		mCategoryDetailBlock = new CategoryDetailBlock(view, context);
		mCategoryDetailBlock.setCategoryName(mCategoryID, mCategoryName);
		mCategoryDetailBlock.initView();

		if (null == mCategoryDetailController) {
			// Controller request ListProduct
			mCategoryDetailController = new CategoryDetailController();
			mCategoryDetailController.setDelegate(mCategoryDetailBlock);
			mCategoryDetailController.setCategoryID(mCategoryID);
			mCategoryDetailController.setCatename(mCategoryName);
			mCategoryDetailController.onStart();
		} else {
			mCategoryDetailController.setDelegate(mCategoryDetailBlock);
			mCategoryDetailController.onResume();
		}
		mCategoryDetailBlock.setClickViewMore(mCategoryDetailController
				.getClicker());
		mCategoryDetailBlock
				.setClickCategoryNameViewMore(mCategoryDetailController
						.getClicker());

		// list categories
		LinearLayout ll_categories = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("ll_categories"));
		mCategoryBlock = new CategoryBlock(ll_categories, context);
		mCategoryBlock.setCategoryName(mCategoryName);
		mCategoryBlock.initView();

		if (null == mCategoryController) {
			// controller request List Category,show loading
			mCategoryController = new CategoryController();
			mCategoryController.setDelegate(mCategoryBlock);
			mCategoryController.setCategoryID(mCategoryID);
			mCategoryController.onStart();
		} else {
			mCategoryController.setDelegate(mCategoryBlock);
			mCategoryController.onResume();
		}
		mCategoryBlock.setClicker(mCategoryController.getClicker());

		// save
		DataLocal.saveCateID(mCategoryID, mCategoryName);

		return view;
	}

}

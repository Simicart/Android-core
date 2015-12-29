package com.simicart.core.catalog.category.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.category.model.CategoryModel;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

public class CategoryController extends SimiController {
	protected SimiDelegate mDelegate;
	protected String mID;
	protected OnItemClickListener mClicker;

	@Override
	public void onStart() {
		requestListCategories();

		mClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selecteItem(position);
			}

		};

	}

	private void requestListCategories() {
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}

			}
		};
		mModel = new CategoryModel();
		((CategoryModel) mModel).setCategoryID(mID);
		mModel.setDelegate(delegate);
		if (!mID.equals("-1")) {
			((CategoryModel) mModel).setCategoryID(mID);
			mModel.addParam("category_id", mID);
		}
		mModel.request();

	}

	protected void selecteItem(int position) {
		Category category = (Category) mModel.getCollection().getCollection()
				.get(position);
		SimiFragment fragment = null;
		if (category.hasChild()) {
			if (DataLocal.isTablet) {
				fragment = CategoryFragment.newInstance(
						category.getCategoryName(), category.getCategoryId());
				CateSlideMenuFragment.getIntance().replaceFragmentCategoryMenu(
						fragment);
			} else {
				fragment = CategoryFragment.newInstance(
						category.getCategoryName(), category.getCategoryId());
				SimiManager.getIntance().addFragment(fragment);
			}
		} else {
			ListProductFragment searchFragment = ListProductFragment.newInstance();
			if (category.getCategoryId().equals("-1")) {
				searchFragment.setUrlSearch(Constants.GET_ALL_PRODUCTS);
			} else {
				searchFragment.setUrlSearch(Constants.GET_CATEGORY_PRODUCTS);
			}
			searchFragment.setCategoryId(category.getCategoryId());
			searchFragment.setCatName(category.getCategoryName());
			SimiManager.getIntance().replaceFragment(searchFragment);
		}
	}

	public OnItemClickListener getClicker() {
		return mClicker;
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public void setCategoryID(String id) {
		mID = id;
	}

}

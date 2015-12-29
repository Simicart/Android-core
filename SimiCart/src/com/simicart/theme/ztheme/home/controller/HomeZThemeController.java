package com.simicart.theme.ztheme.home.controller;

import java.util.ArrayList;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.ztheme.home.delegate.HomeZThemeDelegate;
import com.simicart.theme.ztheme.home.entity.CategoryZTheme;
import com.simicart.theme.ztheme.home.entity.SpotProductZTheme;
import com.simicart.theme.ztheme.home.model.HomeZThemeModel;

public class HomeZThemeController extends SimiController {
	protected HomeZThemeDelegate mDelegate;
	protected OnGroupClickListener mGroupExpand;
	protected OnChildClickListener mChildClick;
	ArrayList<CategoryZTheme> mCategories;

	@Override
	public void onStart() {
		onAction();

		mModel = new HomeZThemeModel();
		mDelegate.showLoading();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					
					if (null != entity && entity.size() > 0) {
						ArrayList<CategoryZTheme> categories = new ArrayList<CategoryZTheme>();
						for (SimiEntity simiEntity : entity) {
							CategoryZTheme category = (CategoryZTheme) simiEntity;
							categories.add(category);
						}
						mCategories = categories;
					}
					mDelegate.updateView(mModel.getCollection());
				}

			}
		});
		if (DataLocal.isTablet) {
			mModel.addParam(Constants.PHONE_TYPE, Constants.TABLET);
		}
		mModel.request();
	}

	protected void onAction() {
		mGroupExpand = new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				switch (mCategories.get(groupPosition).getType()) {
				case CategoryZTheme.TYPE_CAT:
					if (!mCategories.get(groupPosition).hasChild()) {
						selecteCat(mCategories.get(groupPosition));
					}
					break;
				case CategoryZTheme.TYPE_SPOT:
					selecteSpot(mCategories.get(groupPosition)
							.getSpotProductZTheme());
					break;
				default:
					break;
				}
				return false;
			}
		};

		mChildClick = new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				selecteCat(mCategories.get(groupPosition).getmCategories()
						.get(childPosition));
				return true;
			}
		};

	}

	protected void selecteSpot(SpotProductZTheme spotProductZTheme) {
		ListProductFragment fragment = ListProductFragment.newInstance();
		fragment.setUrlSearch(ConstantsSearch.url_spot_ztheme);
		fragment.setCategoryName(spotProductZTheme.getName());
		fragment.setKey(spotProductZTheme.getKey());
		if (DataLocal.isTablet) {
			fragment.setTag_search(TagSearch.TAG_GRIDVIEW);
		}
		SimiManager.getIntance().replaceFragment(fragment);
	}

	protected void selecteCat(Category category) {
		SimiFragment fragment = null;
		if (category.hasChild()) {
			fragment = CategoryFragment.newInstance(category.getCategoryName(),
					category.getCategoryId());
			if (DataLocal.isTablet) {
				CateSlideMenuFragment.getIntance().replaceFragmentCategoryMenu(
						fragment);
				CateSlideMenuFragment.getIntance().openMenu();
			} else {
				SimiManager.getIntance().replaceFragment(fragment);
			}
		} else {
			fragment = ListProductFragment.newInstance();
//			((ListProductFragment) fragment).setmCatID(category.getCategoryId());
			((ListProductFragment) fragment).setCategoryId(category.getCategoryId());
			((ListProductFragment) fragment).setCatName(category.getCategoryName());
			if (category.getCategoryId().equals("-1")) {
				((ListProductFragment) fragment).setUrlSearch(Constants.GET_ALL_PRODUCTS);
			} else {
				((ListProductFragment) fragment).setUrlSearch(Constants.GET_CATEGORY_PRODUCTS);
			}
			if (DataLocal.isTablet) {
				((ListProductFragment) fragment)
						.setTag_search(TagSearch.TAG_GRIDVIEW);
			}
			SimiManager.getIntance().removeDialog();
			SimiManager.getIntance().replaceFragment(fragment);
		}

	}

	public OnGroupClickListener getmGroupExpand() {
		return mGroupExpand;
	}

	public OnChildClickListener getmChildClick() {
		return mChildClick;
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

	public void setDelegate(HomeZThemeDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

}

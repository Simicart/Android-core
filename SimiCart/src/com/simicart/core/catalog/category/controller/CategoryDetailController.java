package com.simicart.core.catalog.category.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.model.ListProductModel;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class CategoryDetailController extends SimiController {
	protected SimiDelegate mDelegate;
	protected String mID;
	protected OnClickListener mClicker;
	protected String mCatename;

	@Override
	public void onStart() {
		if (DataLocal.isTablet) {
			mDelegate.updateView(null);
		} else {
			requestListProducts();
		}

		mClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ListProductFragment searchFragment = ListProductFragment
						.newInstance();
				searchFragment.setCategoryId(mID);
				searchFragment.setCategoryName(mCatename);
				if (mID.equals("-1")) {
					searchFragment.setUrlSearch(Constants.GET_ALL_PRODUCTS);
				} else {
					searchFragment
							.setUrlSearch(Constants.GET_CATEGORY_PRODUCTS);
				}
				SimiManager.getIntance().replaceFragment(searchFragment);
			}
		};

	}

	private void requestListProducts() {
		// mDelegate.showDialogLoading();
		mModel = new ListProductModel();
		((ListProductModel) mModel).setCategoryID(mID);
		mModel.addParam("category_id", mID);
		mModel.addParam("offset", "0");
		mModel.addParam("limit", "10");
		// mModel.addParam("sort_option", mSortType);
		mModel.addParam("width", "300");
		mModel.addParam("height", "300");
		// if (null != jsonFilter) {
		// mModel.addParam("filter", jsonFilter);
		// }
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				// mDelegate.dismissDialogLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}
			}
		});
		mModel.request();

	}

	public OnClickListener getClicker() {
		return mClicker;
	}

	@Override
	public void onResume() {
		if (DataLocal.isTablet) {
			mDelegate.updateView(null);
		} else {
			mDelegate.updateView(mModel.getCollection());
		}
	}

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public void setCategoryID(String id) {
		mID = id;
	}

	public void setCatename(String mCatename) {
		this.mCatename = mCatename;
	}

}

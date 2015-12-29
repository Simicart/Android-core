package com.simicart.core.catalog.product.controller;

import java.util.ArrayList;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.catalog.product.delegate.CustomerReviewDelegate;
import com.simicart.core.catalog.product.model.CustomerReviewModel;

public class CustomerReviewController extends SimiController {

	protected CustomerReviewDelegate mDelegate;
	protected String mID;
	protected ArrayList<Integer> mRatingStar;
	protected OnScrollListener mScrollListener;
	protected int mOffset = 0;
	protected boolean checkOnscroll = true;

	public OnScrollListener getScroller() {
		return mScrollListener;
	}

	public void setDelegate(CustomerReviewDelegate delegate) {
		mDelegate = delegate;
	}

	public void setProductId(String id) {
		mID = id;
	}

	public void setRatingStar(ArrayList<Integer> mRatingStar) {
		this.mRatingStar = mRatingStar;
	}

	@Override
	public void onStart() {
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.onUpdateHeaderView(mRatingStar);
					mDelegate.updateView(mModel.getCollection());
				}
			}
		};
		mModel = new CustomerReviewModel();
		mModel.addParam("product_id", mID);
		mModel.addParam("offset", "" + mOffset + "");
		mModel.addParam("limit", "" + 5 + "");
		mModel.setDelegate(delegate);
		mModel.request();

		mScrollListener = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				int threshold = 1;
				int count = view.getCount();
				int totalStars = sumListStars();
				if (scrollState == SCROLL_STATE_IDLE) {
					if ((view.getLastVisiblePosition() >= count - threshold)
							&& totalStars > count) {
						if (checkOnscroll) {
							mOffset += 5;
							requestListReview();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItem) {

			}
		};
	}

	public int sumListStars() {
		int sum = 0;
		for (int d : mRatingStar)
			sum += d;
		return sum;
	}

	protected void requestListReview() {
		checkOnscroll = false;
		// show load more
		mDelegate.addFooterView();

		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				Log.e("Done", "Done");
				checkOnscroll = true;
				mDelegate.removeFooterView();
				if (isSuccess) {
					
					mDelegate.updateView(mModel.getCollection());
					
				}
			}
		};
		
		mModel.addParam("product_id", mID);
		mModel.addParam("offset", "" + mOffset + "");
		mModel.addParam("limit", "" + 5 + "");
		mModel.setDelegate(delegate);
		mModel.request();

	}

	@Override
	public void onResume() {
		mDelegate.onUpdateHeaderView(mRatingStar);
		mDelegate.updateView(mModel.getCollection());
		
	}

}

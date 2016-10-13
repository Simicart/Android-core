package com.simicart.plugins.wishlist.controller;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.MainActivity;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;
import com.simicart.plugins.wishlist.model.MyWishListModel;

@SuppressLint("ClickableViewAccessibility")
public class MyWishListController extends SimiController {

	protected MyWishListDelegate mDelegate;

	protected OnTouchListener onTouchShare;
	protected OnScrollListener onListScroll;
	private int mCurrentOffset = 0;
	int limit = 10;
	protected int resultNumber;
	protected boolean isOnscroll = true;

	protected String mShareMessage = "";
	protected OnTouchListener mShareListener;
	protected OnTouchListener mTabletShareListener;

	public OnTouchListener getShareListener() {
		return mShareListener;
	}

	public OnTouchListener getTabletShareListener() {
		return mTabletShareListener;
	}

	public OnScrollListener getListScrollListener() {
		return onListScroll;
	}

	@Override
	public void onStart() {
		getMyWishList();

		createTabletTouchShare();

		createTouchShare();

		onListScroll = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = view.getCount();
				Log.e("Count :", count + "");
				// if (scrollState == SCROLL_STATE_IDLE) {
				if ((view.getLastVisiblePosition() == mCurrentOffset + limit - threshold)
						&& resultNumber > (count - threshold)) {
					if (isOnscroll) {
						mCurrentOffset += limit;
						isOnscroll = false;
						mDelegate.setIsLoadMore(true);
						getMyWishList();
					}
					// }
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		};

	}

	private void createTouchShare() {
		final Drawable icon = SimiManager.getIntance().getCurrentContext().getResources()
				.getDrawable(Rconfig.getInstance().drawable("wishlist_share_icon"));

		mShareListener = new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				TextView tv_shareAll = (TextView) v.findViewById(Rconfig.getInstance().id("tv_shareall"));
				ImageView im_shareAll = (ImageView) v.findViewById(Rconfig.getInstance().id("im_shareall"));
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					tv_shareAll.setTextColor(Color.GRAY);
					icon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
					im_shareAll.setBackgroundDrawable(icon);
					break;
				}
				case MotionEvent.ACTION_UP: {
					controllerShare(mShareMessage);
				}

				case MotionEvent.ACTION_CANCEL: {
					icon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
					im_shareAll.setBackgroundDrawable(icon);
					tv_shareAll.setTextColor(Color.BLACK);
					break;
				}
				default:
					break;
				}
				return true;
			}
		};
	}

	private void createTabletTouchShare() {

		mTabletShareListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					controllerShare(mShareMessage);
					break;
				}

				case MotionEvent.ACTION_CANCEL: {
					break;
				}
				default:
					break;
				}
				return true;
			}
		};
	}

	public void controllerShare(String sharing_mes) {
		if (sharing_mes != null && !sharing_mes.equals("")) {
			Intent intent2 = new Intent();
			intent2.setAction(Intent.ACTION_SEND);
			intent2.setType("text/plain");
			intent2.putExtra(Intent.EXTRA_TEXT, sharing_mes);
			MainActivity.context.startActivity(Intent.createChooser(intent2, "Share via"));
		}
	}

	private void getMyWishList() {
		if (mModel == null) {
			mDelegate.showLoading();
			mModel = new MyWishListModel();
		}
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mShareMessage = ((MyWishListModel) mModel).getShareMessage();

					mDelegate.setWishlist_qty(((MyWishListModel) mModel).getWishlist_qty());
					mDelegate.updateView(mModel.getCollection());

					ArrayList<SimiEntity> entity = mModel.getCollection().getCollection();
					if (null == entity || entity.size() <= 0) {
						ArrayList<ItemWishList> mWishLists = new ArrayList<ItemWishList>();
						mDelegate.updateData(mWishLists);
					}

					resultNumber = ((MyWishListModel) mModel).getWishlist_qty();
					isOnscroll = true;
					mDelegate.setIsLoadMore(false);

				} else {
					SimiManager.getIntance().showToast("Error! Please try again.");
				}

			}
		});
		mModel.addParam("limit", String.valueOf(limit));
		mModel.addParam("offset", String.valueOf(mCurrentOffset));
		mModel.request();
	}

	@Override
	public void onResume() {
		// mDelegate.setWishlist_qty(((MyWishListModel)
		// mModel).getWishlist_qty());
		// mDelegate.updateView(mModel.getCollection());
		Log.e("MyWishListController", "onResume");
		getMyWishList();
	}

	public void setDelegate(MyWishListDelegate mBlock) {
		mDelegate = mBlock;
	}

}

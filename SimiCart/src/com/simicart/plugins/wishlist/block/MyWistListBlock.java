package com.simicart.plugins.wishlist.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.common.AdapterMyWishList;
import com.simicart.plugins.wishlist.common.WishListManager;
import com.simicart.plugins.wishlist.controller.ItemWishListController;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;

public class MyWistListBlock extends SimiBlock implements MyWishListDelegate {

	public static final String TITLE = "My Wishlist";
	public static final String ITEMS = "Items";
	public static final String ITEM = "Item";
	public static final String SHARE_WISHLIST = "Share Wishlist";

	private ArrayList<ItemWishList> mWishLists;

	protected LinearLayout ll_share_wishlist;
	protected ImageView im_shareAll;
	protected TextView tv_shareAll;
	protected AdapterMyWishList mAdapter;
	protected RelativeLayout rlt_layout_top;

	public void setShareListener(OnTouchListener touchListener) {
		ll_share_wishlist.setOnTouchListener(touchListener);
	}

	public MyWistListBlock(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
		tv_shareAll = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_shareall"));
		tv_shareAll.setText(Config.getInstance().getText(SHARE_WISHLIST));

		ll_share_wishlist = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_share_wishlist"));
		im_shareAll = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"im_shareall"));
		final Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("wishlist_share_icon"));
		im_shareAll.setBackgroundDrawable(icon);
		rlt_layout_top = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rl_mywishlist_top"));
	}

	public void setWishLists(ArrayList<ItemWishList> wishLists) {
		if (null == mWishLists || mWishLists.size() == 0) {
			rlt_layout_top.setVisibility(View.GONE);
			LinearLayout ll_body = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_body"));
			ll_body.removeAllViewsInLayout();
			TextView tv_notify = new TextView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CENTER;
			tv_notify.setText(Config.getInstance().getText(
					"Your Wishlist is empty"));
			tv_notify.setGravity(Gravity.CENTER);
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			ll_body.addView(tv_notify, params);
			return;
		}
		ListView lv_mywishlist = (ListView) mView.findViewById(Rconfig
				.getInstance().id("lv_mywistlist"));

		ItemWishListController controller = new ItemWishListController(this,
				mContext);
		mAdapter = new AdapterMyWishList(mContext, this.mWishLists, controller);
		mAdapter.setDelegate(this);
		lv_mywishlist.setAdapter(mAdapter);
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			mWishLists = new ArrayList<ItemWishList>();
			for (SimiEntity simiEntity : entity) {
				ItemWishList itemWishList = (ItemWishList) simiEntity;
				mWishLists.add(itemWishList);
			}
			setWishLists(mWishLists);
		}
	}

	// MyWishListDelegate
	@Override
	public void setWishlist_qty(int wishlist_qty) {
		TextView tv_qtyItem = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_qtyItem"));
		WishListManager.getInstance().updateQtyWishList(
				String.valueOf(wishlist_qty));
		if (wishlist_qty < 2) {
			tv_qtyItem.setText(wishlist_qty + " "
					+ Config.getInstance().getText(ITEM));
		} else {
			tv_qtyItem.setText(wishlist_qty + " "
					+ Config.getInstance().getText(ITEMS));
		}
	}

	@Override
	public void updateData(ArrayList<ItemWishList> items) {
		setWishLists(items);
	}

	@Override
	public boolean isShown() {
		return mView.isShown();
	}

	@Override
	public void showDetail(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestShowNext() {
		// TODO Auto-generated method stub

	}
}

package com.simicart.plugins.wishlist.block;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.ColorButton;
import com.simicart.plugins.wishlist.common.AdapterMyWishListTab;
import com.simicart.plugins.wishlist.common.WishListManager;
import com.simicart.plugins.wishlist.controller.ItemWishListController;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;
import com.simicart.plugins.wishlist.fragment.ProductWishListFragment;

public class MyWishListBlockTablet extends SimiBlock implements
		MyWishListDelegate {

	protected ArrayList<ItemWishList> mWishLists;
	protected RelativeLayout rl_share_all_wishlist;
	protected ImageView im_shareAll;
	protected TextView tx_share_all;
	protected String mCurrentID = "";
	AdapterMyWishListTab mAdapter;
	GridView gv_mywishlist;
	protected ProductWishListFragment mProductWLFragment;
	protected ColorButton bt_addAllCart;

	public void setShareListener(OnTouchListener touchListener) {
		rl_share_all_wishlist.setOnTouchListener(touchListener);
	}

	public MyWishListBlockTablet(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
		tx_share_all = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tx_share_all"));
		tx_share_all.setText(Config.getInstance().getText(
				MyWistListBlock.SHARE_WISHLIST));
		rl_share_all_wishlist = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rl_share_all_wishlist"));
	}

	private void setWishLists(ArrayList<ItemWishList> wishLists) {
		this.mWishLists = wishLists;
		if (null == mWishLists || mWishLists.size() == 0) {
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
		gv_mywishlist = (GridView) mView.findViewById(Rconfig.getInstance().id(
				"grid_wishlist"));

		ItemWishListController controller = new ItemWishListController(this,
				mContext);
		mAdapter = new AdapterMyWishListTab(mContext, this.mWishLists,
				controller);
		mAdapter.setDelegate(this);
		gv_mywishlist.setAdapter(mAdapter);
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
		WishListManager.getInstance().updateQtyWishList(
				String.valueOf(wishlist_qty));
		if (wishlist_qty < 2) {
			Toast toast = Toast.makeText(mContext, wishlist_qty + " "
					+ Config.getInstance().getText(MyWistListBlock.ITEM),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
			toast.show();
		} else {
			Toast toast = Toast.makeText(mContext, wishlist_qty + " "
					+ Config.getInstance().getText(MyWistListBlock.ITEMS),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
			toast.show();
		}
	}

	@Override
	public void updateData(ArrayList<ItemWishList> items) {

		Log.e("MyWishListBlockTablet ", "updateData " + items.size());

		mWishLists = items;

		if (null == mWishLists || mWishLists.size() == 0) {
			RelativeLayout rlt_body = (RelativeLayout) mView
					.findViewById(Rconfig.getInstance().id("rlt_body"));
			rlt_body.removeAllViewsInLayout();
			TextView tv_notify = new TextView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CENTER;
			tv_notify.setText(Config.getInstance().getText(
					"Your Wishlist is empty"));
			tv_notify.setGravity(Gravity.CENTER);
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			rlt_body.addView(tv_notify, params);
			return;
		}

//		mAdapter.setWishItems(mWishLists);
//		mAdapter.notifyDataSetChanged();
//		gv_mywishlist.invalidate();
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

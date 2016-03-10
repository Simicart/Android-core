package com.simicart.plugins.wishlist;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.MainActivity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.material.LayoutRipple;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;
import com.simicart.plugins.rewardpoint.fragment.RewardPointFragment;
import com.simicart.plugins.wishlist.block.MyWistListBlock;
import com.simicart.plugins.wishlist.block.ProductWishlistBlock;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.common.WishListManager;
import com.simicart.plugins.wishlist.controller.ControllerAddWishList;
import com.simicart.plugins.wishlist.entity.ButtonAddWishList;
import com.simicart.plugins.wishlist.entity.ProductWishList;
import com.simicart.plugins.wishlist.fragment.MyWishListFragment;
import com.simicart.plugins.wishlist.fragment.MyWishListFragmentTablet;

public class WishList {
	public static final String MY_WISHLIST = "My WishList";
	public String MY_WISH_LIST = WishListConstants.MY_WISHLIST;
	public static String MY_WISH_LIST_OLD = WishListConstants.MY_WISHLIST;
	public static ButtonAddWishList bt_addWish;
	public static String product_ID = "";
	protected SlideMenuData mSlideMenuData;
	protected ArrayList<ItemNavigation> mItems;
	protected TextView tv_qtyWishList;

	public WishList(String methodName, SlideMenuData slideMenuData) {
		this.mSlideMenuData = slideMenuData;
		if (methodName.equals("addItem")) {
			mItems = mSlideMenuData.getItemNavigations();
			ItemNavigation mItemNavigation = new ItemNavigation();
			mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
			Drawable icon = SimiManager
					.getIntance()
					.getCurrentContext()
					.getResources()
					.getDrawable(
							Rconfig.getInstance().drawable("plugins_wishlist_iconmenu"));
			icon.setColorFilter(Config.getInstance().getColorMenu(),
					PorterDuff.Mode.SRC_ATOP);
			mItemNavigation.setName(MY_WISHLIST);
			mItemNavigation.setIcon(icon);
			mItems.add(mItemNavigation);

			Fragment fragment = null;
			if (DataLocal.isTablet) {
				fragment = MyWishListFragmentTablet.newInstance();
			} else {
				fragment =  MyWishListFragment.newInstance();
			}
			mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
					fragment.getClass().getName());
		}
		if (methodName.equals("removeItem")) {
			mItems = mSlideMenuData.getItemNavigations();
			for (ItemNavigation mItemNavigation : mItems) {
				if (mItemNavigation.getName().equals(MY_WISHLIST)) {
					mItems.remove(mItemNavigation);
					mSlideMenuData.getPluginFragment().remove(
							mItemNavigation.getName());
				}
			}
		}
	}

	public WishList(String method, String data) {
		if (method.equals("updateWishListQty")) {
			if (Utils.validateString(data)) {
				try {
					JSONObject json = new JSONObject(data);
					if (json.has(Constants.DATA)) {
						JSONArray array = json.getJSONArray(Constants.DATA);
						JSONObject js_signIn = array.getJSONObject(0);
						if (js_signIn.has("wishlist_items_qty")) {
							String wishlist_items_qty = js_signIn
									.getString("wishlist_items_qty");
							WishListManager.getInstance().updateQtyWishList(
									wishlist_items_qty);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
			{
				WishListManager.getInstance().updateQtyWishList(null);
			}

		}
	}

	private View mView;
	public WishList(String method, CacheBlock cache) {
		switch (method) {
		case "additem_wishlist":
			mView = cache.getView();
			addWishList();
			break;
		case "addButtonWishList":
			// add button wish list for product detail screen
			getDataAddButtonWishList(cache);
			break;
		case "addWishlistForMyAccount":
			// add wish list for my account screen
			Context context = cache.getContext();
			View view = cache.getView();
			addItemWishListForMyAccount(context, view);
			break;
		case "onChangeOption":
			try {
				bt_addWish.setEnable(true);
			} catch (Exception e) {
			}
			try {
				ProductWishlistBlock.bt_addWish.setEnable(true);
			} catch (Exception e) {
			}
			break;
		default:
			break;
		}
	}

	public WishList(String method, CacheFragment cache) {
		if (method.equals("afterSignIn") && !product_ID.equals("")) {
			ProductDetailParentFragment fragment = ProductDetailParentFragment.newInstance(product_ID,null);
//			fragment.setProductID(product_ID);
			product_ID = "";
			cache.setFragment(fragment);
		}
	}

	public void updateWishListQty(int qty) {
		if (null != tv_qtyWishList) {
			if (qty > 0) {
				tv_qtyWishList.setVisibility(View.VISIBLE);
				tv_qtyWishList.setText(String.valueOf(qty));
			} else {
				tv_qtyWishList.setVisibility(View.GONE);
			}

		}
	}
	
	protected void addWishList() {
		RelativeLayout rel_RewardPoint = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("rel_wishlist"));
		View v = MainActivity.instance.getLayoutInflater().inflate(
				Rconfig.getInstance().layout("plugins_rewardpoint_in_profile"),
				null);
		rel_RewardPoint.addView(v);
		final LayoutRipple rl_rewardPoint = (LayoutRipple) v
				.findViewById(Rconfig.getInstance().id("rl_rewardPoint"));
		TextView lb_reward_point = (TextView) rl_rewardPoint
				.findViewById(Rconfig.getInstance().id("lb_reward_point"));
		if (DataLocal.isLanguageRTL) {
			lb_reward_point.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		}
		ImageView im_reward_point = (ImageView) rl_rewardPoint
				.findViewById(Rconfig.getInstance().id("im_reward_point"));
		Drawable icon = SimiManager.getIntance().getCurrentContext().getResources().getDrawable(
				Rconfig.getInstance().drawable("plugins_wishlist_iconadd2"));
//		icon.setColorFilter(Config.getInstance().getContent_color(),
//				PorterDuff.Mode.SRC_ATOP);
		im_reward_point.setImageDrawable(icon);
		lb_reward_point.setText(Config.getInstance().getText(MY_WISHLIST));
		lb_reward_point.setTextColor(Config.getInstance().getContent_color());
		rl_rewardPoint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiFragment fragment = null;
				if (DataLocal.isTablet) {
					fragment = MyWishListFragmentTablet.newInstance();
				} else {
					fragment =  MyWishListFragment.newInstance();
				}
				SimiManager.getIntance().replacePopupFragment(fragment);
			}
		});
	}

	private void getDataAddButtonWishList(CacheBlock cache) {
		Context context = cache.getContext();
		View view = cache.getView();
		Product product = (Product) cache.getSimiEntity();
		ArrayList<FloatingActionButton> mListButtons = ((ProductMorePluginBlock) cache.getBlock()).getListButton();
		addButtonMyWishList(context, product, view, mListButtons);
	}

	protected void addButtonMyWishList(Context context, Product product,
			View view, ArrayList<FloatingActionButton> mListButtons) {
		FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) view.findViewById(Rconfig.getInstance().id("more_plugins_action"));
		bt_addWish = new ButtonAddWishList(context);
		for (int i = 0; i < mListButtons.size(); i++) {
			mMultipleActions.removeButton(mListButtons.get(i));
		}
		mListButtons.add((mListButtons.size() - 1), bt_addWish.getImageAddWishList());
		for (int i = 0; i < mListButtons.size(); i++) {
			mMultipleActions.addButton(mListButtons.get(i));
		}
		
		ProductWishList productWishList = new ProductWishList(product);
		
		Log.e("WishList ", "addButtonMyWishList " + product.getId());
		
		if (productWishList.getWishlist_item_id().equals("0")
				|| productWishList.getProduct().getOptions().size() > 0) {
			bt_addWish.setEnable(true);
		}
		ControllerAddWishList controllerAddWishList = new ControllerAddWishList(
				context, bt_addWish, productWishList);
		MyWistListBlock mDelegate = new MyWistListBlock(view, context);
		controllerAddWishList.setDelegate(mDelegate);
		controllerAddWishList.onAddToWishList();
	}

	protected void addItemWishListForMyAccount(Context context, View view) {
		RelativeLayout layout = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("rl_more"));
		TextView tv_myaccount = new TextView(context);
		tv_myaccount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tv_myaccount.setText(Config.getInstance().getText(
				WishListConstants.MY_WISHLIST));
		RelativeLayout.LayoutParams lp_tv = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getValueDp(40));
		lp_tv.addRule(RelativeLayout.CENTER_VERTICAL);
		lp_tv.setMargins(0, 0, Utils.getValueDp(20), 0);
		tv_myaccount.setLayoutParams(lp_tv); // causes layout update
		tv_myaccount.setGravity(Gravity.CENTER_VERTICAL);
		tv_myaccount.setPadding(Utils.getValueDp(20), 0, 0, 0);

		layout.addView(tv_myaccount);
		// create 1 view
		ImageView imageAddWishList = new ImageView(context);
		imageAddWishList.setImageResource(Rconfig.getInstance().drawable(
				"ic_action_expand"));
		int size = Utils.getValueDp(20);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size,
				size);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		imageAddWishList.setLayoutParams(lp); // causes layout update

		layout.addView(imageAddWishList);

		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DataLocal.isSignInComplete()) {
					SimiFragment fragment = null;
					if (DataLocal.isTablet) {
						fragment = MyWishListFragmentTablet.newInstance();
					} else {
						fragment = MyWishListFragment.newInstance();
					}
					SimiManager.getIntance().replaceFragment(fragment);
				} else {
					SignInFragment fragment = SignInFragment.newInstance(null, null, false);
					SimiManager.getIntance().replacePopupFragment(fragment);
				}
			}
		});
	}
}

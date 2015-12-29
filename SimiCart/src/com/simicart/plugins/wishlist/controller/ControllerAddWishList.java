package com.simicart.plugins.wishlist.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.catalog.product.fragment.OptionFragment;
import com.simicart.core.common.options.ProductOptionParentView;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.delegate.OptionProductDelegate;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.plugins.wishlist.WishList;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.common.WishListManager;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ButtonAddWishList;
import com.simicart.plugins.wishlist.entity.ItemWishList;
import com.simicart.plugins.wishlist.entity.ProductWishList;
import com.simicart.plugins.wishlist.model.AddWishListModel;
import com.simicart.plugins.wishlist.model.RemoveWishListModel;

@SuppressLint("DefaultLocale")
public class ControllerAddWishList implements OptionProductDelegate {
	View view;
	ButtonAddWishList bt_addWishList;
	ProductWishList productWishList;
	Context mContext;
	MyWishListDelegate mDelegate;
	boolean isUpdateWishList = false;
	protected ArrayList<CacheOptionView> mOptionView;

	public void setUpdateWishList(boolean isUpdateWishList) {
		this.isUpdateWishList = isUpdateWishList;
	}

	public void setDelegate(MyWishListDelegate delegate) {
		mDelegate = delegate;
	}

	public ControllerAddWishList(Context context, ButtonAddWishList button,
			ProductWishList product) {
		mContext = context;
		bt_addWishList = button;
		productWishList = product;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public ButtonAddWishList getBt_addWishList() {
		return bt_addWishList;
	}

	public void setBt_addWishList(ButtonAddWishList bt_addWish) {
		this.bt_addWishList = bt_addWish;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ProductWishList getProductWishList() {
		return productWishList;
	}

	public void setProductWishList(ProductWishList productWishList) {
		this.productWishList = productWishList;
	}

	public void onAddToWishList() {
		bt_addWishList.getImageAddWishList().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Animator zoomAnimation = (AnimatorSet) AnimatorInflater
								.loadAnimator(mContext, Rconfig.getInstance()
										.getId("zoom_animation", "anim"));
						zoomAnimation.setTarget(bt_addWishList
								.getImageAddWishList());
						zoomAnimation.start();
						if (DataLocal.isSignInComplete()) {
							if (bt_addWishList.isEnable()) {
								checkOption(productWishList.getProduct(), view);
							} else {
								removeFromWishList();
							}
						} else {
							SignInFragment fragment = SignInFragment
									.newInstance();
							SimiManager.getIntance().addPopupFragment(fragment);
							WishList.product_ID = productWishList.getProduct()
									.getId();
						}
					}
				});
	}

	public void checkOption(Product product, View productDetailView) {
		ProductOptionParentView option_parent = new ProductOptionParentView(
				product, this);
		mOptionView = option_parent.getOptionView();
		View view = option_parent.initOptionView();

		if (!checkSelectedAllOption(product)) {
			this.showDialog(view, product, productDetailView);
		} else {
			// add to wish list
			addToWishList();
		}
	}

	public void showDialog(View viewOption, final Product product,
			final View productDetailView) {

		OnClickListener onDoneOption = new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiManager.getIntance().getManager().popBackStack();
				if (!checkSelectedAllOption(product)) {
				} else {
					// add to wish list
					addToWishList();
				}
			}
		};

		if (null != viewOption) {
			OptionFragment fragment = OptionFragment.newInstance(viewOption,
					onDoneOption);
			FragmentTransaction frt = SimiManager.getIntance().getManager()
					.beginTransaction();
			frt.setCustomAnimations(
					Rconfig.getInstance().getId("abc_fade_in", "anim"), Rconfig
							.getInstance().getId("abc_fade_out", "anim"));
			SimiManager.getIntance().removeDialog();
			frt.add(Rconfig.getInstance().id("container"), fragment);
			frt.addToBackStack(null);
			frt.commit();
		}

	}

	protected boolean checkSelectedAllOption(Product product) {
		if (product.getStock()) {
			ArrayList<CacheOption> options = getCacheOptions();
			if ((null != options) && !checkSelectedCacheOption(options)) {

				return false;
			}
		}
		return true;
	}

	protected boolean checkSelectedCacheOption(ArrayList<CacheOption> options) {
		for (CacheOption cacheOption : options) {
			if (cacheOption.isRequired() && !cacheOption.isCompleteRequired()) {
				return false;
			}
		}

		return true;
	}

	protected ArrayList<CacheOption> getCacheOptions() {
		ArrayList<CacheOption> options = null;
		if (mOptionView.size() > 0) {
			options = new ArrayList<CacheOption>();
			for (CacheOptionView option : mOptionView) {
				options.add(option.getCacheOption());
			}
		}

		return options;
	}

	public void addToWishList() {
		final AddWishListModel model = new AddWishListModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					bt_addWishList.setEnable(false);
					ItemWishList item = null;
					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (null != entity && entity.size() > 0) {
						item = (ItemWishList) entity.get(0);
						Log.e("WishList_ID", item.getWishlist_item_id());
						productWishList.setWishlist_item_id(item
								.getWishlist_item_id());
						ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
						if (null != entity && entity.size() > 0) {
							for (SimiEntity simiEntity : entity) {
								ItemWishList itemWishList = (ItemWishList) simiEntity;
								items.add(itemWishList);
							}
						}
						WishListManager.getInstance().updateQtyWishList(
								String.valueOf(model.getWishlist_qty()));
						if (isUpdateWishList) {
							mDelegate.updateData(items);

							Log.e("ControllerAddWishList ", "addToWishList "
									+ model.getWishlist_qty());

							mDelegate.setWishlist_qty(model.getWishlist_qty());

							mDelegate.updateView(model.getCollection());
						}
					}
					// update wish list tablet
					SimiManager.getIntance().showToast("Added to WishList");
				} else {
					SimiManager.getIntance().showToast(message);
				}

			}
		});
		Product product = productWishList.getProduct();
		model.addParam(Constants.PRODUCT_ID, product.getId());
		model.addParam(Constants.PRODUCT_QTY, String.valueOf(product.getQty()));

		ArrayList<CacheOption> options = product.getOptions();
		if (null != options) {
			try {
				JSONArray array = convertCacheOptionToParams(options);
				model.addParam("options", array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mDelegate.showDialogLoading();
		model.request();
	}

	protected JSONArray convertCacheOptionToParams(
			ArrayList<CacheOption> options) throws JSONException {
		JSONArray array = new JSONArray();
		for (CacheOption cacheOption : options) {
			array.put(cacheOption.toParameter());
		}
		return array;
	}

	public void removeFromWishList() {

		final RemoveWishListModel model = new RemoveWishListModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					SimiManager.getIntance().showToast("Removed from Wishlist");
					bt_addWishList.setEnable(true);
					productWishList.setWishlist_item_id("0");
					if (isUpdateWishList) {
						ArrayList<SimiEntity> entity = model.getCollection()
								.getCollection();
						ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
						if (null != entity && entity.size() > 0) {
							for (SimiEntity simiEntity : entity) {
								ItemWishList itemWishList = (ItemWishList) simiEntity;
								items.add(itemWishList);
							}

						}
						mDelegate.updateData(items);
						mDelegate.requestShowNext();
					}
				} else {
					SimiManager.getIntance().showToast(message);
				}

			}
		});
		model.addParam(WishListConstants.WISHLIST_ITEM_ID,
				"" + productWishList.getWishlist_item_id());
		mDelegate.showDialogLoading();
		model.request();
	}

	@Override
	public void updatePrice(ProductOption cacheOption, boolean isAdd) {
		if (cacheOption.getDependence_options() != null) {
			if (productWishList.getProduct().getType().equals("configurable")) {
				productWishList.getProduct().updateCurrentListDependence();
			}
		}
	}

}
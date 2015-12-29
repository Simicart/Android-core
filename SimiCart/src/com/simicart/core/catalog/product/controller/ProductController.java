package com.simicart.core.catalog.product.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.catalog.product.model.AddToCartModel;
import com.simicart.core.catalog.product.model.ProductModel;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.options.ProductOptionParentView;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.delegate.OptionProductDelegate;
import com.simicart.core.common.price.ProductPriceViewDetail;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;

@SuppressLint({ "DefaultLocale", "ClickableViewAccessibility" })
public class ProductController extends SimiController implements
		OptionProductDelegate {
	protected ProductDelegate mDelegate;
	protected String mID;
	protected ArrayList<CacheOptionView> mOptionView;
	protected ProductPriceViewDetail mPriceView;
	protected OnTouchListener mListenerAddToCart;
	protected OnClickListener mClickerRating;
	protected OnClickListener mClickerImage;
	protected OnTouchListener mListenerInfor;

	public ProductController() {
		mOptionView = new ArrayList<CacheOptionView>();
	}

	public void setDelegate(ProductDelegate delegate) {
		mDelegate = delegate;
	}

	public void setProductId(String id) {
		mID = id;
	}

	public OnTouchListener getListenerAddToCart() {
		return mListenerAddToCart;
	}

	public OnTouchListener getListenerInfor() {
		return mListenerInfor;
	}

	public OnClickListener getClickerRating() {
		return mClickerRating;
	}

	public OnClickListener getClickerImage() {
		return mClickerImage;
	}

	@Override
	public void onStart() {
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
					onUpdatePriceView();
					onUpdateOptionView();
					if (DataLocal.isTablet) {
						boolean isFocus = mDelegate.isShown();
						if (isFocus) {
							// showInforDetail();
						}
					}
				}
			}
		};
		mModel = new ProductModel();
		mModel.addParam("product_id", mID);
		mModel.setDelegate(delegate);
		mModel.request();

		mListenerAddToCart = new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {

					if (getProductFromCollection().getStock()) {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Color.GRAY);
						gdDefault.setCornerRadius(15);
						v.setBackgroundDrawable(gdDefault);
					}
					break;
				}
				case MotionEvent.ACTION_UP: {
					addtoCart();
				}
				case MotionEvent.ACTION_CANCEL: {

					if (getProductFromCollection().getStock()) {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Config.getInstance().getColorMain());
						gdDefault.setCornerRadius(15);
						v.setBackgroundDrawable(gdDefault);

					}
					break;
				}
				default:
					break;
				}
				return true;
			}
		};

		mListenerInfor = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					// showInforDetail();
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

		mClickerRating = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showReview();
			}
		};

		mClickerImage = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showImage();
			}
		};
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
		onUpdatePriceView();
		onUpdateOptionView();

		if (DataLocal.isTablet) {
			// showInforDetail();
		}

	}

	protected void onUpdateOptionView() {
		View view = onShowOptionView();
		if (null != view) {
			mDelegate.onUpdateOptionView(view);
		}
	}

	protected void onUpdatePriceView() {
		Product product = getProductFromCollection();
		if (null != product) {
			View view = onShowPriceView(product);
			if (null != view) {
				mDelegate.onUpdatePriceView(view);
			}
		}
	}

	protected View onShowOptionView() {
		Product product = getProductFromCollection();

		if (null == product) {
			return null;
		}

		ProductOptionParentView parent_view = new ProductOptionParentView(
				product, this);

		View view = parent_view.initOptionView();

		mOptionView = parent_view.getOptionView();

		return view;

	}

	protected Product getProductFromCollection() {
		Product product = null;
		ArrayList<SimiEntity> entity = mModel.getCollection().getCollection();
		if (null != entity && entity.size() > 0) {
			product = (Product) entity.get(0);
		}
		return product;
	}

	protected View onShowPriceView(Product product) {
		LinearLayout ll_price = new LinearLayout(SimiManager.getIntance()
				.getCurrentContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mPriceView = new ProductPriceViewDetail(product);

		View view = mPriceView.getViewPrice();
		if (null != view) {
			ll_price.addView(view, params);
		}
		return ll_price;
	}

	@Override
	public void updatePrice(ProductOption cacheOption, boolean isAdd) {
		if (null != mPriceView) {
			View view = mPriceView.updatePriceWithOption(cacheOption, isAdd);
			if (null != view) {
				mDelegate.onUpdatePriceView(view);
			}
		}
	}

	protected void addtoCart() {
		if (!checkSelectedAllOption()) {
			SimiManager.getIntance().showToast(
					Config.getInstance().getText("Please select all options"));
			return;
		}
		onShowOptionView();
		ArrayList<CacheOption> options = getCacheOptions();
		mDelegate.showDialogLoading();
		final AddToCartModel model = new AddToCartModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					int mQty = getCartQtyFromJsonobject(model.getDataJSON());
					ConfigCheckout.getInstance().setmQty(String.valueOf(mQty));
					SimiManager.getIntance().onUpdateCartQty(mQty + "");

					SimiManager.getIntance().showToast(
							Config.getInstance().getText("Added to Cart"));
					ConfigCheckout.getInstance().setCheckStatusCart(true);
				} else {
					// SimiManager.getIntance().showNotify(message);
					SimiManager.getIntance().showToast(message);
					// View view_option = onShowOptionView();
					// mDelegate.onUpdateOptionView(view_option);
				}

			}
		});

		model.addParam("product_id", getProductFromCollection().getId());
		if (getProductFromCollection().getData("variant_id") != null) {
			model.addParam("variant_id",
					getProductFromCollection().getData("variant_id"));
		}
		if (getProductFromCollection().getData("product_type") != null) {
			model.addParam("product_type",
					getProductFromCollection().getData("product_type"));
		}
		model.addParam("product_qty",
				String.valueOf(getProductFromCollection().getQty()));

		if (null != options) {
			try {
				JSONArray array = convertCacheOptionToParams(options);
				if (array != null && array.length() > 0) {
					model.addParam("options", array);
				}
			} catch (JSONException e) {
				mDelegate.dismissLoading();
				SimiManager.getIntance().showNotify("Cannot convert data");
				e.printStackTrace();
				return;
			}
		}
		model.request();
	}

	private int getCartQtyFromJsonobject(JSONObject jsonObject) {
		int mQty = 0;
		if (jsonObject != null) {
			if (jsonObject.has("data")) {
				try {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = (JSONObject) jsonArray.get(i);
							if (object.has("product_qty")) {
								int qty = object.getInt("product_qty");
								mQty += qty;
							}
						}
					}
				} catch (Exception e) {
					Log.e("ProductController - getCartQtyFromJsonobject",
							e.getMessage());
				}
			}
		}
		return mQty;
	}

	protected boolean checkSelectedAllOption() {
		if (getProductFromCollection().getStock()) {
			ArrayList<CacheOption> options = getCacheOptions();
			if ((null != options) && !checkSelectedCacheOption(options)) {
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

	protected boolean checkSelectedCacheOption(ArrayList<CacheOption> options) {
		for (CacheOption cacheOption : options) {
			if (cacheOption.isRequired() && !cacheOption.isCompleteRequired()) {
				return false;
			}
		}

		return true;
	}

	protected JSONArray convertCacheOptionToParams(
			ArrayList<CacheOption> options) throws JSONException {
		JSONArray array = new JSONArray();
		for (CacheOption cacheOption : options) {
			if (cacheOption.toParameter() != null) {
				array.put(cacheOption.toParameter());
			}
		}
		return array;
	}

	public void onClickViewImage(ImageView imv_image, final String[] images) {
		imv_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ShowImageFragment fragment = ShowImageFragment.newInstance();
				// fragment.setImages(images);
				// SimiManager.getIntance().replaceFragment(fragment);
			}
		});
	}

}

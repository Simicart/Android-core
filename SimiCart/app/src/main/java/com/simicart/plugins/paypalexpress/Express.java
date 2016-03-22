package com.simicart.plugins.paypalexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.model.AddToCartModel;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.core.style.ColorButton;
import com.simicart.plugins.paypalexpress.fragment.WebFragment;
import com.simicart.plugins.paypalexpress.model.RequestStartModel;

public class Express {
	Context context;
	View rootView;
	Product product;
	SimiBlock mDelegate;
	Cart cart;
	ProgressDialog pd_loading;

	public Express(String method, CheckoutData checkoutData) {
		Log.e(getClass().getName(), "Method: " + method);
		this.context = MainActivity.context;
		if (method.equals("callPayPalExpressServer")) {
			if (checkoutData.getPaymentMethod().getPayment_method()
					.toUpperCase().equals("PAYPAL_EXPRESS")) {
				if (rootView == null) {
					pd_loading = ProgressDialog.show(context, null, null, true,
							false);
					pd_loading.setContentView(Rconfig.getInstance().layout(
							"core_base_loading"));
					pd_loading.setCanceledOnTouchOutside(false);
					pd_loading.setCancelable(false);
					pd_loading.show();
				}
				placeOrder();
			}
		}
	}

	public Express(String method, CacheBlock cacheBlock) {
		this.context = cacheBlock.getContext();
		this.rootView = cacheBlock.getView();
		if (method.equals("addButtonPayPalToCart")) {
			SimiCollection collection = cacheBlock.getSimiCollection();
			JSONObject js_other = collection.getJSONOther();
			String isPaypal = "";
			if (js_other != null) {
				try {
					isPaypal = js_other.getString("is_paypal_express");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (isPaypal != null && isPaypal.equals("1")) {
				this.addButtonPayPalToCart();
			}
		}
		if (method.equals("addButtonPayPalToDetail")) {
			SimiCollection collection = cacheBlock.getSimiCollection();
			this.product = (Product) collection.getCollection().get(0);
			String isPaypal = this.product.getData("is_paypal_express");
			if (isPaypal != null && isPaypal.equals("1")) {
				this.addButtonPayPalToDetail();
			}
		}
	}

	private void addButtonPayPalToDetail() {
		Button b_express = new Button(context);
		GradientDrawable gdDefault = new GradientDrawable();
		b_express.setBackgroundDrawable(gdDefault);
		b_express.setBackgroundResource(Rconfig.getInstance().drawable(
				"plugin_paypal_express_product"));
		int size = Utils.getValueDp(40);
		LinearLayout.LayoutParams lp_express = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, size);
		lp_express.setMargins(0, 0, 0, Utils.getValueDp(5));
		if (DataLocal.isTablet) {
			size = Utils.getValueDp(49);
			lp_express = new LinearLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, size);
			lp_express.gravity = Gravity.CENTER_VERTICAL;
			lp_express.setMargins(0, 0, 0, Utils.getValueDp(10));
		}
		lp_express.gravity = Gravity.CENTER_HORIZONTAL;

		b_express.setLayoutParams(lp_express);
		LinearLayout ll_botton = (LinearLayout) rootView.findViewById(Rconfig
				.getInstance().id("ll_paypal_express"));
		ll_botton.removeAllViews();
		ll_botton.addView(b_express);
		b_express.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					if (checkOption(product, rootView)) {
						showDialog(rootView);
					} else {
						// add to cart
						addToCart();
					}
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
		});
	}

	public void addButtonPayPalToCart() {
		
		int size1 = Utils.getValueDp(42);

		ColorButton b_express = new ColorButton(context);
		b_express.setStyle(3, Color.YELLOW);
		b_express.setBackgroundResource(Rconfig.getInstance().drawable(
				"plugin_paypal_express_product"));

		RelativeLayout.LayoutParams lp_express = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, size1);
		lp_express.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		Button b_checkout = (Button) rootView.findViewById(Rconfig
//				.getInstance().id("checkout"));
//		lp_express.addRule(RelativeLayout.BELOW, b_checkout.getId());
		lp_express.setMargins(0, Utils.getValueDp(5), 0, 0);
		b_express.setLayoutParams(lp_express);
		b_express.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_UP: {

					mDelegate = new SimiBlock(rootView, context);
					mDelegate.showDialogLoading();
					placeOrder();
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
		});
		RelativeLayout rl = (RelativeLayout) rootView.findViewById(Rconfig
				.getInstance().id("fcart_rl_bottom"));
		rl.removeAllViews();
		rl.addView(b_express);
	}

	public boolean checkOption(Product product, View productDetailView) {
		ArrayList<CacheOption> options = product.getOptions();
		boolean is_alert = false;
		for (CacheOption option : options) {
			if (option.isRequired()) {
				if (!option.isCompleteRequired()) {
					is_alert = true;
				}
			}
		}
		return is_alert;
	}

	public void showDialog(View productDetailView) {
		AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
				productDetailView.getContext());
		alertboxDowload.setTitle(Config.getInstance().getText("REQUIRED")
				.toUpperCase());
		alertboxDowload.setMessage(Config.getInstance().getText(
				"Required options are not selected."));
		alertboxDowload.setCancelable(false);
		alertboxDowload.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		alertboxDowload.show();
	}

	public void addToCart() {
		if (product.getStock()) {
			ArrayList<CacheOption> options = product.getOptions();
			if ((null != options) && !checkSelectedCacheOption(options)) {
				SimiManager.getIntance().showNotify(
						Config.getInstance().getText(
								"Please select all options"));
				return;
			}
			AddToCartModel mModel = new AddToCartModel();
			mDelegate = new SimiBlock(rootView, context);
			mDelegate.showDialogLoading();
			mModel.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					if (isSuccess) {
						placeOrder();
					} else {
						mDelegate.dismissDialogLoading();
						SimiManager.getIntance().showToast(message);
					}
				}
			});
			mModel.addParam("product_id", product.getId());
			mModel.addParam("product_qty", String.valueOf(product.getQty()));

			if (null != options) {
				try {
					JSONArray array = convertCacheOptionToParams(options);
					mModel.addParam("options", array);
				} catch (JSONException e) {
					mDelegate.dismissLoading();
					SimiManager.getIntance().showNotify("Cannot convert data");
					e.printStackTrace();
					return;
				}
			}
			mModel.request();
		}
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
			array.put(cacheOption.toParameter());
		}
		return array;
	}

	public void placeOrder() {
		final RequestStartModel mModel = new RequestStartModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (mDelegate != null) {
					mDelegate.dismissDialogLoading();
				}
				if (pd_loading != null) {
					pd_loading.dismiss();
				}
				if (isSuccess) {
					WebFragment fragment = WebFragment.newInstance(
							mModel.getUrl(), mModel.getReview_address());
					SimiManager.getIntance().addPopupFragment(fragment);
					Log.e("AAAAAAAAAAAA",
							"BBBBBBBBB" + mModel.getReview_address());
				} else {
					SimiManager.getIntance().showToast(message);
				}
			}
		});
		mModel.request();
	}
}

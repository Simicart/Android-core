package com.simicart.plugins.mobileanalytics;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.plugins.mobileanalytics.model.GaIDModel;

public class AppAnalytics {

	private static GoogleAnalytics mGa;
	private static Tracker mTracker;

	/*
	 * Google Analytics configuration values.
	 */
	// Placeholder property ID.
	private static String GA_PROPERTY_ID = "UA-52928750-1";

	// Dispatch period in seconds.
	private static final int GA_DISPATCH_PERIOD = 30;

	// Prevent hits from being sent to reports, i.e. during testing.
	private static final boolean GA_IS_DRY_RUN = false;

	// GA Logger verbosity.
	private static final LogLevel GA_LOG_VERBOSITY = LogLevel.INFO;

	// Key used to store a user's tracking preferences in SharedPreferences.
	private static final String TRACKING_PREF_KEY = "trackingPreference";

	public AppAnalytics(String method, CacheActivity cacheActivity) {
		Context context = cacheActivity.getActivity().getBaseContext();
		Log.e(getClass().getName(), "method : " + method);
		if (method.equals("requestGetGA_ID")) {
			requestGetGA_ID(context);
		}
	}

	public AppAnalytics(String method, CacheFragment cacheFragment) {
		SimiFragment simiFragment = cacheFragment.getFragment();
		Log.e(getClass().getName(), "method : " + method);
		if (method.equals("sendScreen")
				&& !simiFragment.getScreenName().equals("")) {
			sendScreen(simiFragment.getScreenName());
		}
	}

	public AppAnalytics(String method, String url_banner) {
		Log.e(getClass().getName(), "method : " + method);
		if (method.equals("sendEvent")) {
			sendEvent("ui_action", "banner_press", url_banner);
		}
	}

	public AppAnalytics(String method, CheckoutData checkoutData)
			throws JSONException {
		Log.e(getClass().getName(), "method : " + method);
		// send
		TotalPrice totalPrice = checkoutData.getTotal_priceAll();
		Double shipping_priceDouble = Double
				.parseDouble(getShippingHandling(totalPrice));
		Double taxDouble = Double.parseDouble(totalPrice.getTax());
		Double totalDouble = Double.parseDouble(checkoutData.getTotal_price());

		ArrayList<Cart> mListCart = new ArrayList<>();
		mListCart = DataLocal.listCarts;
		mListCart.size();

		if (method.equals("sendOrder")) {
			sendOrder(checkoutData.getInvoice_number(), shipping_priceDouble,
					taxDouble, totalDouble, mListCart);
		}
	}

	public String getShippingHandling(TotalPrice mtotalPrice) {
		String shippingHandling = "0";
		if (mtotalPrice.getShipping_handling() != null
				&& !mtotalPrice.getShipping_handling().equals("null")
				&& !mtotalPrice.getShipping_handling().equals("")
				&& !mtotalPrice.getShipping_handling().equals("0")) {
			shippingHandling = mtotalPrice.getShipping_handling();
			return shippingHandling;
		}
		if (mtotalPrice.getShipping_handling_incl_tax() != null
				&& !mtotalPrice.getShipping_handling_incl_tax().equals("null")
				&& !mtotalPrice.getShipping_handling_incl_tax().equals("")
				&& !mtotalPrice.getShipping_handling_incl_tax().equals("0")) {
			shippingHandling = mtotalPrice.getShipping_handling_incl_tax();
			return shippingHandling;
		}
		if (mtotalPrice.getShipping_handling_excl_tax() != null
				&& !mtotalPrice.getShipping_handling_excl_tax().equals("null")
				&& !mtotalPrice.getShipping_handling_excl_tax().equals("")
				&& !mtotalPrice.getShipping_handling_excl_tax().equals("0")) {
			shippingHandling = mtotalPrice.getShipping_handling_excl_tax();
			return shippingHandling;
		}
		return shippingHandling;
	}

	// Create a Tracker with GA_ID
	private void createTracker(final Context context) {
		mGa = GoogleAnalytics.getInstance(context);
		String ga_id = getGaPropertyId();
		mTracker = mGa.getTracker(ga_id);

		// Set dispatch period.
		GAServiceManager.getInstance().setLocalDispatchPeriod(
				GA_DISPATCH_PERIOD);

		// Set dryRun flag.
		mGa.setDryRun(GA_IS_DRY_RUN);

		// Set Logger verbosity.
		mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

		// Set the opt out flag when user updates a tracking preference.
		SharedPreferences userPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		userPrefs
				.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
					@Override
					public void onSharedPreferenceChanged(
							SharedPreferences sharedPreferences, String key) {
						if (key.equals(TRACKING_PREF_KEY)) {
							GoogleAnalytics.getInstance(context).setAppOptOut(
									sharedPreferences.getBoolean(key, false));
						}
					}
				});

	}

	private void requestGetGA_ID(final Context context) {
		final GaIDModel model = new GaIDModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					setGaPropertyId(model.getGa_id());
					createTracker(context);
				}
			}
		});
		model.request();
	}

	// screenName = getClass().toString();
	private void sendScreen(String screenName) {

		mTracker.send(MapBuilder.createAppView()
				.set(Fields.SCREEN_NAME, screenName).build());

		HashMap<String, String> campaignData = new HashMap<String, String>();
		campaignData.put(Fields.CAMPAIGN_SOURCE, "email");
		campaignData.put(Fields.CAMPAIGN_MEDIUM, "email marketing");
		campaignData.put(Fields.CAMPAIGN_NAME, "summer_campaign");
		campaignData.put(Fields.CAMPAIGN_CONTENT, "email_variation_1");

		MapBuilder paramMap = MapBuilder.createAppView();

		// Campaign data sent with this hit.
		// Note that the campaign data is set on the Map, not the tracker.
		mTracker.send(paramMap.setAll(campaignData).build());

		Log.e("TEST SEND SCREEN", screenName);
	}

	// send event click banner
	private void sendEvent(String categoryAction, String eventAction,
			String eventLabel) {

		mTracker.send(MapBuilder.createEvent(categoryAction, eventAction,
				eventLabel, null).build());

		Log.e("TEST SEND EVENT", categoryAction + "AA" + eventAction + "AA"
				+ eventLabel);

		// mTracker.send(MapBuilder.createEvent("ui_action", "button_press",
		// "addtocart_button", null).build());
	}

	// send order Ecommerce Tracking
	private void sendOrder(String tranID, Double shipping_priceDouble,
			Double taxDouble, Double totalDouble, ArrayList<Cart> mListCart) {

		String currency_code = Config.getInstance().getCurrency_code();

		mTracker.send(MapBuilder.createTransaction(tranID, // (String)Transaction
															// ID
				"In-app Store", // (String) Affiliation
				totalDouble, // (Double) Order revenue
				taxDouble, // (Double) Tax
				shipping_priceDouble, // (Double) Shipping
				currency_code) // (String) Currency code
				.build());

		for (Cart cart : mListCart) {
			Double product_price = (double) cart.getProduct_price();
			Long poduct_qty = (long) cart.getQty();
			mTracker.send(MapBuilder.createItem(tranID, // (String)Transaction
					// ID
					cart.getProduct_name(), // (String) Product name
					("" + cart.getProduct_id()), // (String) Product SKU
					null, // (String) Product category (no)
					product_price, // (Double) Product price
					poduct_qty, // (Long) Product quantity
					currency_code) // (String) Currency code
					.build());

			Log.e("TEST SEND ORDER", "Product Name:" + cart.getProduct_name()
					+ "\nSKU:" + cart.getProduct_id() + "\nProductPrice:"
					+ product_price + "\nQty:" + poduct_qty);
		}

		Log.e("TEST SEND ORDER", "Total:" + totalDouble + "\nTax:" + taxDouble
				+ "\nShipping:" + shipping_priceDouble + "\nTransactionID:"
				+ tranID);
	}

	public static String getGaPropertyId() {
		return GA_PROPERTY_ID;
	}

	public static void setGaPropertyId(String ga_id) {
		if (Utils.validateString(ga_id)) {
			GA_PROPERTY_ID = ga_id;
		}
		Log.e("GA_PROPERTY_ID", "GA_PROPERTY_ID:" + ga_id);
	}
}

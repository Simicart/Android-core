package com.simicart.plugins.mobileanalytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.plugins.mobileanalytics.model.GaIDModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class AppAnalytics {

    private static GoogleAnalytics mGa;
    private static Tracker mTracker;

    /*
     * Google Analytics configuration values.
     */
    // Placeholder property ID.
    private static String GA_PROPERTY_ID = "UA-68764476-1";

    // Dispatch period in seconds.
    private static final int GA_DISPATCH_PERIOD = 30;

    // Prevent hits from being sent to reports, i.e. during testing.
    private static final boolean GA_IS_DRY_RUN = false;

    // GA Logger verbosity.
    private static final int GA_LOG_VERBOSITY = LogLevel.INFO;

    // Key used to store a user's tracking preferences in SharedPreferences.
    private static final String TRACKING_PREF_KEY = "trackingPreference";

    public AppAnalytics(String method, CacheActivity cacheActivity) {
        Context context = cacheActivity.getActivity().getBaseContext();
        Log.e(getClass().getName(), "method : " + method);
        if (method.equals("createTracker")) {
            createTracker(context);
        }
        if (method.equals("requestGetGA_ID")) {
            requestGetGA_ID();
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
        mTracker = mGa.newTracker(ga_id);

        // Set dispatch period.
        mGa.setLocalDispatchPeriod(
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

    private void requestGetGA_ID() {
        final GaIDModel model = new GaIDModel();
        model.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                if (isSuccess) {
                    setGaPropertyId(model.getGa_id());
                }
            }
        });
        model.request();
    }

    // screenName = getClass().toString();
    private void sendScreen(String screenName) {

        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        HashMap<String, String> campaignData = new HashMap<String, String>();
        campaignData.put("utm_source", "email");
        campaignData.put("utm_medium", "email marketing");
        campaignData.put("utm_campaign", "summer_campaign");
        campaignData.put("utm_content", "email_variation_1");

        //MapBuilder paramMap = MapBuilder.createAppView();

        // Campaign data sent with this hit.
        // Note that the campaign data is set on the Map, not the tracker.
        mTracker.send(new HitBuilders.EventBuilder().setAll(campaignData).build());

        Log.e("TEST SEND SCREEN", screenName);
    }

    // send event click banner
    private void sendEvent(String categoryAction, String eventAction,
                           String eventLabel) {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryAction)
                .setAction(eventAction)
                .setLabel(eventLabel)
                .build());

        Log.e("TEST SEND EVENT", categoryAction + "AA" + eventAction + "AA"
                + eventLabel);

        // mTracker.send(MapBuilder.createEvent("ui_action", "button_press",
        // "addtocart_button", null).build());
    }

    // send order Ecommerce Tracking
    private void sendOrder(String tranID, Double shipping_priceDouble,
                           Double taxDouble, Double totalDouble, ArrayList<Cart> mListCart) {

        String currency_code = Config.getInstance().getCurrency_code();

        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(tranID)
                .setTransactionAffiliation("In-app Store")
                .setTransactionRevenue(totalDouble)
                .setTransactionTax(taxDouble)
                .setTransactionShipping(shipping_priceDouble)
                .setTransactionCouponCode(currency_code);

        mTracker.send(new HitBuilders.ScreenViewBuilder().setProductAction(productAction).build());

        for (Cart cart : mListCart) {
            Double product_price = (double) cart.getProduct_price();
            Long poduct_qty = (long) cart.getQty();
            Product product = new Product()
                    .setId(cart.getProduct_id())
                    .setName(cart.getProduct_name())
                    .setPrice(product_price)
                    .setCouponCode("APPARELSALE")
                    .setQuantity(cart.getQty());
            mTracker.send(new HitBuilders.ScreenViewBuilder().addProduct(product).build());

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
        GA_PROPERTY_ID = ga_id;
        Log.e("GA_PROPERTY_ID", "GA_PROPERTY_ID:" + ga_id);
    }
}

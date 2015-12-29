package com.simicart.plugins.paypalexpress.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.plugins.paypalexpress.model.RequestAddressModel;
import com.simicart.plugins.paypalexpress.model.RequestReturnModel;

@SuppressLint("SetJavaScriptEnabled")
public class FragmentWeb extends SimiFragment {
	public String Url = "";
	public String review_address = "1";
	protected boolean check = true;
	SimiDelegate mDelegate;
	public static FragmentWeb newInstance(String url, String address) {
		FragmentWeb fragmetWeb = new FragmentWeb();
		fragmetWeb.setUrl(url);
		fragmetWeb.setReviewAddress(address);
		return fragmetWeb;
	}

	public void setUrl(String url) {
		this.Url = url + "#m";
	}

	public void setReviewAddress(String check) {
		this.review_address = check;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_webview_layout"), container,
				false);
		mDelegate = new SimiBlock(rootView, MainActivity.context);
		final WebView webview = (WebView) rootView.findViewById(Rconfig
				.getInstance().id("webview_Ad"));
		final View mImageView = inflater.inflate(
				Rconfig.getInstance().layout("core_base_loading"), null, false);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);

		mImageView.setLayoutParams(lp);
		// add loading View
		webview.addView(mImageView);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings()
		.setUserAgentString(
				"Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
		
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// webview.addView(mImageView);
				Log.e("Paypal Express", "URL: " + url);
				mDelegate.showDialogLoading();
				if (url.contains("return") && check == true) {
					check = false;
					if (review_address.equals("1")) {
						final RequestAddressModel mModel = new RequestAddressModel();
						mModel.setDelegate(new ModelDelegate() {

							@Override
							public void callBack(String message,
									boolean isSuccess) {
								mDelegate.dismissDialogLoading();
								if (isSuccess) {
									MyAddress shippingAddress = mModel
											.getShippingAddress();
									MyAddress billingAddress = mModel
											.getBillingAddress();
									FragmentAddress fragment = new FragmentAddress(
											shippingAddress, billingAddress);
									SimiManager.getIntance().addPopupFragment(
											fragment);
								} else {
									SimiManager.getIntance().showToast(message);
								}
							}
						});
						mModel.request();
					} else {
						final RequestReturnModel mModel = new RequestReturnModel();
						mModel.setDelegate(new ModelDelegate() {

							@Override
							public void callBack(String message,
									boolean isSuccess) {
								mDelegate.dismissDialogLoading();
								if (isSuccess) {
									FragmentShipping fShipping = new FragmentShipping();
									ArrayList<SimiEntity> entity = mModel
											.getCollection().getCollection();
									ArrayList<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();
									for (SimiEntity simiEntity : entity) {
										ShippingMethod shippingMethod = (ShippingMethod) simiEntity;
										shippingMethods.add(shippingMethod);
									}
									fShipping
											.setShippingMethodList(shippingMethods);
									SimiManager.getIntance().addPopupFragment(
											fShipping);
								} else {
									SimiManager.getIntance().showToast(message);
								}
							}
						});
						mModel.request();
					}

					// webview.removeView(mImageView);
					webview.stopLoading();
				}else{
					mDelegate.dismissDialogLoading();
				}
				if (url.contains("cancel")) {
					SimiManager.getIntance().showToast("Cancel this purchase");
					SimiManager.getIntance().backPreviousFragment();
					mDelegate.dismissDialogLoading();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}

		});
		webview.loadUrl(this.Url);
		return rootView;
	}
}

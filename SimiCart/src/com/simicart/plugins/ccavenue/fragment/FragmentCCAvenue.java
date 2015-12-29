package com.simicart.plugins.ccavenue.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.simicart.MainActivity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class FragmentCCAvenue extends SimiFragment {
	public static final String SUCCESS = "onepage/success";
	public static final String REVIEW = "onepage/review";
	public static final String FAIL = "onepage/failure";
	public static final String ERROR = "simiavenue/api/index";

	public static final String MES_SUCCESS = "Complete order Successfully. Thank your for purchase";
	public static final String MES_REVIEW = "The order changes to reviewed";
	public static final String MES_FAIL = "Failure: Your order has been canceled";
	public static final String MES_ERROR = "Have some errors, please try again";

	String Url = "";
	String invoice_number;

	public static FragmentCCAvenue newInstance() {
		FragmentCCAvenue fragment = new FragmentCCAvenue();
		return fragment;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_webview_layout"), container,
				false);
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
		webview.getSettings().setLayoutAlgorithm(
				WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		String url_site = Url;
		webview.loadUrl(url_site);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.contains(SUCCESS)) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(MES_SUCCESS);
				} else if (url.contains(REVIEW)) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(MES_REVIEW);
				} else if (url.contains(FAIL)) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(MES_FAIL);
				} else if (url.contains(ERROR)) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(MES_ERROR);
				} else {
					Log.e(getClass().getName(), "RUNNING:" + url);
					// backtoHomeScreen();
					// showToastMessage(MES_SUCCESS);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}

		});

		return rootView;
	}

	public void showToastMessage(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
	}
}

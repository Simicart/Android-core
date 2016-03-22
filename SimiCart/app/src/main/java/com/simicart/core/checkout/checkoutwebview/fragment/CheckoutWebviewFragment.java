package com.simicart.core.checkout.checkoutwebview.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class CheckoutWebviewFragment extends SimiFragment {
	protected String Url = "";
	private boolean isCheckout = false;

	public static CheckoutWebviewFragment newInstanse(String url,
			boolean isCheckout) {
		CheckoutWebviewFragment fragment = new CheckoutWebviewFragment();
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.URL, url, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.CHECK_BOO, isCheckout,
				Constants.KeyData.TYPE_BOOLEAN, bundle);
		fragment.setArguments(bundle);
		return fragment;
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
		if (getArguments() != null) {
			Url = (String) getData(Constants.KeyData.URL,
					Constants.KeyData.TYPE_STRING, getArguments());
		}
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
		String url_site = "";
		String url_cp = "";

		if (DataLocal.isSignInComplete()) {
			if (Url.contains("email_value")) {

				url_site = Url.replace("email_value", DataLocal.getEmail());
				url_cp = url_site.replace("password_value",
						DataLocal.getPassword());

				webview.loadUrl(url_cp);
			}
		} else {
			webview.loadUrl(Url);
		}

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return false;
			}

		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isCheckout == false) {
			SimiManager.getIntance().backToHomeFragment();
		}
		isCheckout = false;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		SimiManager.getIntance().backToHomeFragment();
	}
}

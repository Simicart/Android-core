package com.simicart.core.notification.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;

public class WebviewFragment extends SimiFragment {

	public static WebviewFragment newInstance(String url) {
		WebviewFragment fragment = new WebviewFragment(url);
		return fragment;
	}

	public String Url;

	public WebviewFragment(String url) {
		this.Url = url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
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
		webview.loadUrl(Url);
		webview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}
		});

		return rootView;
	}
}

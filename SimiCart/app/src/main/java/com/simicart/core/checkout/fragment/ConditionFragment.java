package com.simicart.core.checkout.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class ConditionFragment extends SimiFragment {
	protected String mContent;

	public static ConditionFragment newInstance(String content) {
		ConditionFragment fragment = new ConditionFragment();
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.CONTENT, content,
				Constants.KeyData.TYPE_STRING, bundle);
		fragment.setArguments(bundle);
		return fragment;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);
		if (getArguments() != null) {
			mContent = (String) getData(Constants.KeyData.CONTENT,
					Constants.KeyData.TYPE_STRING, getArguments());
		}

		WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance()
				.id("webview"));

		WebSettings setting = webView.getSettings();
		webView.setBackgroundColor(Config.getInstance().getApp_backrground());
		setting.setJavaScriptEnabled(true);
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		setting.setUseWideViewPort(true);
		setting.setDisplayZoomControls(true);
		setting.setLoadsImagesAutomatically(true);
		setting.setTextSize(TextSize.LARGEST);
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);

		webView.loadDataWithBaseURL(null, mContent, "text/html", "UTF-8", null);

		return rootView;
	}
}

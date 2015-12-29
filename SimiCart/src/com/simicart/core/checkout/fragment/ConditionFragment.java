package com.simicart.core.checkout.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;

public class ConditionFragment extends SimiFragment {
	protected String mContent;

	public void setContent(String content) {
		mContent = content;
	}

	public static ConditionFragment newInstance() {
		ConditionFragment fragment = new ConditionFragment();
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

		LinearLayout l_scrollView = (LinearLayout) rootView
				.findViewById(Rconfig.getInstance().id("l_scrollView"));

		WebView webView = new WebView(getActivity());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		webView.setLayoutParams(lp);
		WebSettings setting = webView.getSettings();

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
		// webView.loadData(mContent, "text/html", "UTF-8");
		l_scrollView.addView(webView);
		return rootView;
	}
}

package com.simicart.core.catalog.product.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

@SuppressLint("SetJavaScriptEnabled")
public class DescriptionFragment extends SimiFragment {

	protected String mDescription;

//	public void setDescription(String description) {
//		mDescription = description;
//	}

	public static DescriptionFragment newInstance(String description) {
		DescriptionFragment fragment = new DescriptionFragment();
		
		Bundle args = new Bundle();
	    setData(Constants.KeyData.DESCRIPTION, description, Constants.KeyData.TYPE_STRING, args);
	    fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);
		mDescription = (String) getData(Constants.KeyData.DESCRIPTION, Constants.KeyData.TYPE_STRING, getArguments());
		Log.d("quangdd","mDescription"+mDescription);
		
		LinearLayout l_scrollView = (LinearLayout) rootView
				.findViewById(Rconfig.getInstance().id("l_scrollView"));

		WebView webView = new WebView(getActivity());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		webView.setLayoutParams(lp);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDisplayZoomControls(true);
		webView.getSettings().setLoadsImagesAutomatically(true);
		if (mDescription.contains("<div")) {
			String text = "<html><body style=\"color:black;font-family:Helvetica;font-size:42px;font-weight: lighter;\"'background-color:transparent' >"
					+ "<p align=\"justify\" style=\"font-weight: normal\">"
					+ mDescription + "</p>" + "</body></html>";
			webView.getSettings().setTextZoom(400);
			webView.loadDataWithBaseURL(null, text, "text/html",
					"charset=UTF-8", null);
		} else {
			webView.getSettings().setTextZoom(300);
			// webView.loadData(mDescription, "text/html", "UTF-8");
			webView.loadDataWithBaseURL(null, mDescription, "text/html",
					"charset=UTF-8", null);
		}

		l_scrollView.addView(webView);
		return rootView;
	}
}

package com.simicart.core.cms.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

@SuppressLint("SetJavaScriptEnabled")
public class CMSFragment extends SimiFragment {

	protected String mContent;

	public static CMSFragment newInstance(String content) {
		CMSFragment fragment = new CMSFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.CONTENT, content, Constants.KeyData.TYPE_STRING, bundle);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);
		if(getArguments() != null){
		mContent = (String) getData(Constants.KeyData.CONTENT, Constants.KeyData.TYPE_STRING, getArguments());
		}

		WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance().id("webview"));
		webView.setBackgroundColor(Config.getInstance().getApp_backrground());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setSupportZoom(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.loadDataWithBaseURL(
				null,
				StringHTML("<html><body style=\"color:"+Config.getInstance().getContent_color_string()+";font-family:Helvetica;font-size:40px;\"'background-color:transparent' >"
						+ "<p align=\"justify\">"
						+ mContent
						+ "</p>"
						+ "</body></html>"), "text/html", "charset=UTF-8", null);
		return rootView;
	}

	private String StringHTML(String html) {
		String head = "<head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type' /><style type='text/css'>body{font-size: 16px; font-family: 'Helvetica'}</style></head>";
		String HtmlString = "<html>" + head + "<body>" + html
				+ "</body></html>";
		return HtmlString;
	}
}

package com.simicart.plugins.otherpayment.fragment;

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
import com.simicart.plugins.otherpayment.entity.OtherPaymentEntity;

public class OtherPaymentFragment extends SimiFragment {
	protected OtherPaymentEntity mPayment;
	protected String mInvoiceNumber;
	protected String mUrlAction = "";

	public void setInvoiceNumber(String mInvoiceNumber) {
		this.mInvoiceNumber = mInvoiceNumber;
	}

	public void setUrlAction(String mUrlAction) {
		this.mUrlAction = mUrlAction;
	}

	public void setPayment(OtherPaymentEntity mPayment) {
		this.mPayment = mPayment;
	}

	public static OtherPaymentFragment newInstance() {
		OtherPaymentFragment fragment = new OtherPaymentFragment();
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
		webview.getSettings().setDomStorageEnabled(true);

		String url_site = mUrlAction;
		webview.loadUrl(url_site);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.e("URL_REDIRECT", url);
				if(url.contains(mPayment.getUrlRedirect())){
					webview.removeView(mImageView);
				}
				if(url.contains("api/success")){
					SimiManager.getIntance().showToast(mPayment.getMessageSuccess());
					SimiManager.getIntance().backToHomeFragment();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if(url.contains(mPayment.getUrlSuccess())){
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageSuccess());
				}else if(url.contains(mPayment.getUrlFail())){
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageFail());
				}else if(url.contains(mPayment.getUrlCancel())){
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageCancel());
				}else if(url.contains(mPayment.getUrlError())){
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageError());
				}
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

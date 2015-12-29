package com.simicart.plugins.checkout.com.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.plugins.checkout.com.model.UpdatePaymentModel;

public class FragmentCheckoutCom extends SimiFragment {

	String data = "";
	String Url = "";
	String url_back = "";
	String url_action = "";

	String invoice_number;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public String getUrl_back() {
		return url_back;
	}

	public void setUrl_back(String url_back) {
		this.url_back = url_back;
	}

	public String getUrl_action() {
		return url_action;
	}

	public void setUrl_action(String url_action) {
		this.url_action = url_action;
	}

	public static FragmentCheckoutCom newInstance(String data, String url) {
		FragmentCheckoutCom fragmentCheckoutCom = new FragmentCheckoutCom();
		fragmentCheckoutCom.setData(data);
		fragmentCheckoutCom.setUrl(url);
		return fragmentCheckoutCom;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(
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

		String url_site = Url + data;
		webview.loadUrl(url_site);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.contains(url_back)) {
					Log.e(getClass().getName(), "Url :" + url);
					String[] array = url.split("\\?");
					String path = array[1];
					Log.e(getClass().getName(), "path121 :" + path);

					String[] array1 = path.split("&responsecode=");
					path = array1[0];
					Log.e(getClass().getName(), "path :" + path);
					String[] array2 = path.split("&result=");
					Log.e(getClass().getName(), array2[0] + "path :"
							+ array2[1]);
					String result = array2[1];
					Log.e(getClass().getName(), "result :" + result);

					path = array[1];
					array1 = path.split("&authcode=");
					path = array1[0];
					array2 = path.split("&tranid=");
					String orderId = array2[1];
					Log.e(getClass().getName(), "orderId :" + orderId);

					if (!orderId.equals("")) {
						// request update payment statuts
						final SimiBlock mDelegate = new SimiBlock(rootView,
								getActivity());
						mDelegate.showLoading();
						UpdatePaymentModel mModel = new UpdatePaymentModel();
						mModel.setDelegate(new ModelDelegate() {

							@Override
							public void callBack(String message,
									boolean isSuccess) {
								mDelegate.dismissLoading();
								SimiManager.getIntance().showToast(message);
								if (isSuccess) {
									HomeFragment fragment = HomeFragment.newInstance();
									SimiManager.getIntance().replaceFragment(
											fragment);
								}

							}
						});
						mModel.addParam("invoice_number", invoice_number);
						mModel.addParam("transaction_id", orderId);
						if (result.toLowerCase().equals("successful")) {
							mModel.addParam("payment_status", "1");
						} else {
							mModel.addParam("payment_status", "0");
						}
						mModel.setUrlAction(url_action);
						mModel.request();

						webview.removeView(mImageView);
						webview.stopLoading();
						return;
					}
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}

		});

		return rootView;
	}
}

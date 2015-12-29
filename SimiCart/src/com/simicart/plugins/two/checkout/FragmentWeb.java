package com.simicart.plugins.two.checkout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.plugins.two.checkout.model.UpdatePaymentModel;

public class FragmentWeb extends SimiFragment {
	public String Url = "";

	public String data = "";
	protected String url_action = "";
	protected String url_back = "";
	protected String invoice_number = "";

	protected View rootView;

	public FragmentWeb() {
		// TODO Auto-generated constructor stub
	}

	public static FragmentWeb newInstance(String data, String url) {
		FragmentWeb fragmetWeb = new FragmentWeb();
		fragmetWeb.setData(data);
		fragmetWeb.setUrl(url);
		return fragmetWeb;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public void setUrlAction(String url) {
		this.url_action = url;
	}

	public void setUrlBack(String url) {
		this.url_back = url;
	}

	public void setInvoiceNumber(String invoice_number) {
		this.invoice_number = invoice_number;
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
		String url_site = Url + data;
		webview.loadUrl(url_site);
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (Utils.validateString(url_back) && url.contains(url_back)) {
					String[] array = url.split("\\?");
					String path = array[1];
					String[] array1 = path.split("&order_number=");
					path = array1[1];
					String[] array2 = path.split("&");
					String orderId = array2[0];
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
									HomeFragment fragment = HomeFragment
											.newInstance();
									SimiManager.getIntance().replaceFragment(
											fragment);
								}

							}
						});
						mModel.addParam("invoice_number", invoice_number);
						mModel.addParam("transaction_id", orderId);
						mModel.addParam("payment_status", "1");
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

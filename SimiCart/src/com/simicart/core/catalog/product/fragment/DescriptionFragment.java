package com.simicart.core.catalog.product.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.BusEntity;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

@SuppressLint("SetJavaScriptEnabled")
public class DescriptionFragment extends SimiFragment {

	protected String mDescription;

	public static DescriptionFragment newInstance() {
		DescriptionFragment fragment = new DescriptionFragment();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);

		final WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance()
				.id("webview"));
		webView.setBackgroundColor(Config.getInstance().getApp_backrground());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDisplayZoomControls(true);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
			    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    AlertDialog alertDialog = builder.create();
			    String message = "Certificate error.";
			    switch (error.getPrimaryError()) {
			        case SslError.SSL_UNTRUSTED:
			            message = "The certificate authority is not trusted.";
			            break;
			        case SslError.SSL_EXPIRED:
			            message = "The certificate has expired.";
			            break;
			        case SslError.SSL_IDMISMATCH:
			            message = "The certificate Hostname mismatch.";
			            break;
			        case SslError.SSL_NOTYETVALID:
			            message = "The certificate is not yet valid.";
			            break;
			    }
			    message += " Do you want to continue anyway?";
			    alertDialog.setTitle("SSL Certificate Error");
			    alertDialog.setMessage(message);
			    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            // Ignore SSL certificate errors
			            handler.proceed();
			        }
			    });
			    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            handler.cancel();
			            webView.clearSslPreferences();
			        }
			    });
			    alertDialog.show();
			    }
		});
		if (mDescription.contains("<div")) {
			String text = "<html><body style=\"color:black;font-family:Helvetica;font-size:"
					+ convertDpToPixel(12, getActivity())
					+ "px;font-weight: lighter;\"'background-color:transparent' >"
					+ "<p align=\"justify\" style=\"font-weight: normal\">"
					+ mDescription + "</p>" + "</body></html>";
			// webView.getSettings().setTextZoom(400);
			webView.loadDataWithBaseURL(null, text, "text/html",
					"charset=UTF-8", null);
		} else {
			webView.getSettings().setTextZoom(300);
			webView.loadDataWithBaseURL(null, mDescription, "text/html",
					"charset=UTF-8", null);
		}
		return rootView;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp
				* ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

	@Override
	public void onEvent(BusEntity event) {
		super.onEvent(event);
		if (event.getKey().toString().equals(Constants.KeyBus.PRODUCT)) {
			Product product = (Product) event.getValue();
			mDescription = product.getDecripition();
		}
	}
}

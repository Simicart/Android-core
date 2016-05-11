package com.simicart.core.notification.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class WebviewFragment extends SimiFragment {

	public static WebviewFragment newInstance(String url) {
		WebviewFragment fragment = new WebviewFragment();
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.URL, url, Constants.KeyData.TYPE_STRING, bundle);
		fragment.setArguments(bundle);
		return fragment;
	}

	public String url;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_webview_layout"), container,
				false);
		if(getArguments() != null){
		url = (String) getData(Constants.KeyData.URL, Constants.KeyData.TYPE_STRING, getArguments());
		}
		
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
		webview.loadUrl(url);
		webview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				webview.removeView(mImageView);
			}
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
			            webview.clearSslPreferences();
			        }
			    });
			    alertDialog.show();
			    }
		});

		return rootView;
	}
}
